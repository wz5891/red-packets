package com.example.redpackets.service;

import com.example.redpackets.dto.RedPacketDto;

import java.math.BigDecimal;

/**
 * 红包业务逻辑处理接口
 */
public interface IRedPacketService {
    /**
     * 发红包
     *
     * @param dto
     * @return
     */
    String handOut(RedPacketDto dto);

    /**
     * 抢红包
     *
     * @param userId    用户id
     * @param redPacket 红包标识
     * @return
     */
    BigDecimal rob(Integer userId, String redPacket);
}
