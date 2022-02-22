package com.example.redpackets.service.impl;

import com.example.redpackets.dto.RedPacketDto;
import com.example.redpackets.service.IRedPacketService;
import com.example.redpackets.service.IRedService;
import com.example.redpackets.utils.RedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedPacketService implements IRedPacketService {
    private static final String redisKeyPrefix = "red:";

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private IRedService redService;


    @Override
    public String handOut(RedPacketDto dto) {
        if (dto.getTotal() <= 0 || dto.getAmount() <= 0) {
            throw new IllegalArgumentException("发红包异常，参数不合法");
        }

        List<Integer> list = RedPacketUtil.divideRedPackage(dto.getAmount(), dto.getTotal());
        String timestamp = String.valueOf(System.nanoTime());

        // 随机金额列入存入缓存list
        String redPacket = redisKeyPrefix + dto.getUserId() + ":" + timestamp;
        redisTemplate.opsForList().leftPushAll(redPacket, list);

        // 红包总数存入缓存
        String redPacketTotalKey = redPacket + ":total";
        redisTemplate.opsForValue().set(redPacketTotalKey, dto.getTotal());

        // 异步存入数据库
        redService.recordRedPacket(dto, redPacket, list);

        return redPacket;
    }

    @Override
    public BigDecimal rob(Integer userId, String redPacket) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        // 用户抢红包之前，要判断一下当前用户是否已经抢过红包
        Object obj = valueOperations.get(redPacket + ":" + userId + ":rob");
        if (obj != null) {
            return new BigDecimal(obj.toString());
        }

        // 点红包
        Boolean res = click(redPacket);

        if (res) {
            // 上分布式锁
            String lockKey = redPacket + ":" + userId + ":lock";
            Boolean lock = valueOperations.setIfAbsent(lockKey, redPacket);
            redisTemplate.expire(lockKey, 24L, TimeUnit.HOURS);

            if (lock) {
                // 拆红包，从随机金额列表中弹出一个随机金额
                Object value = redisTemplate.opsForList().rightPop(redPacket);
                if (value != null) {
                    log.info("用户抢到红包了，userId=[{}],redPacket=[{}],amount=[{}]", userId, redPacket, value);
                    // 当前用户抢到一个红包，更新缓存，并记录到数据库
                    String redPacketTotalKey = redPacket + ":total";
                    // 更新缓存中剩余红包个数
                    Integer curTotal = valueOperations.get(redPacketTotalKey) != null ? (Integer) valueOperations.get(redPacketTotalKey) : 0;
                    valueOperations.set(redPacketTotalKey, curTotal - 1);

                    // 将当前抢到红包的用户设置到缓存，用于表示当前用户已经抢过红包了
                    valueOperations.set(redPacket + ":" + userId + ":rob", value, 24L, TimeUnit.HOURS);

                    // 将抢红包记录写入数据库-异步
                    redService.recordRobRedPacket(userId, redPacket, new BigDecimal(value.toString()));

                    return new BigDecimal(value.toString());
                }
            }
        }

        // 返回null表示用户没有抢到红包
        return null;
    }

    /**
     * 点击红包，返回true表明还有红包，返回false表示红包已经被抢光了
     *
     * @param redPacket
     * @return
     */
    private Boolean click(String redPacket) {
        String redPacketTotalKey = redPacket + ":total";
        Object total = redisTemplate.opsForValue().get(redPacketTotalKey);

        if (total != null && Integer.valueOf(total.toString()) > 0) {
            return true;
        }
        return false;
    }


}
