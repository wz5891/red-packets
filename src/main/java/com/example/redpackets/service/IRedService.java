package com.example.redpackets.service;

import com.example.redpackets.dto.RedPacketDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数据记录接口-异步实现
 */
public interface IRedService {
    /**
     * 发红包信息存入数据库
     * @param dto 发红包数据
     * @param redPacket 红包标识
     * @param list 随机金额列表
     */
    void recordRedPacket(RedPacketDto dto, String redPacket, List<Integer> list);

    void recordRobRedPacket(Integer userId, String redPacket, BigDecimal amount);
}
