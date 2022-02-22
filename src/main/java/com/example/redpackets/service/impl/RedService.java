package com.example.redpackets.service.impl;

import com.example.redpackets.domain.RedDetail;
import com.example.redpackets.domain.RedRecord;
import com.example.redpackets.domain.RedRobRecord;
import com.example.redpackets.dto.RedPacketDto;
import com.example.redpackets.mapper.RedDetailMapper;
import com.example.redpackets.mapper.RedRecordMapper;
import com.example.redpackets.mapper.RedRobRecordMapper;
import com.example.redpackets.service.IRedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@EnableAsync
public class RedService implements IRedService {
    @Resource
    private RedRecordMapper redRecordMapper;
    @Resource
    private RedDetailMapper redDetailMapper;
    @Resource
    private RedRobRecordMapper redRobRecordMapper;

    @Override
    @Async
    @Transactional
    public void recordRedPacket(RedPacketDto dto, String redPacket, List<Integer> list) {
        RedRecord redRecord = new RedRecord();
        redRecord.setUserId(dto.getUserId());
        redRecord.setAmount(BigDecimal.valueOf(dto.getAmount()));
        redRecord.setTotal(dto.getTotal());
        redRecord.setRedPacket(redPacket);
        redRecord.setCreateTime(LocalDateTime.now());
        redRecord.setIsActive(1);
        redRecordMapper.insert(redRecord);

        for (Integer amount : list) {
            RedDetail detail = new RedDetail();
            detail.setRecordId(redRecord.getId());
            detail.setAmount(BigDecimal.valueOf(amount));
            detail.setCreateTime(LocalDateTime.now());
            detail.setIsActive(1);
            redDetailMapper.insert(detail);
        }

    }

    @Override
    @Async
    @Transactional
    public void recordRobRedPacket(Integer userId, String redPacket, BigDecimal amount) {
        RedRobRecord redRobRecord = new RedRobRecord();
        redRobRecord.setUserId(userId);
        redRobRecord.setRedPacket(redPacket);
        redRobRecord.setAmount(amount);
        redRobRecord.setRobTime(LocalDateTime.now());

        redRobRecordMapper.insert(redRobRecord);
    }
}
