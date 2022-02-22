package com.example.redpackets.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 抢红包记录
 */
@Data
@TableName(value = "red_rob_record")
public class RedRobRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 红包标识串
     */
    private String redPacket;
    /**
     * 红包金额（单位为分）
     */
    private BigDecimal amount;
    /**
     * 获得时间
     */
    private LocalDateTime robTime;
    /**
     * 是否有效（1-是，0-否）
     */
    private Integer isActive;
}
