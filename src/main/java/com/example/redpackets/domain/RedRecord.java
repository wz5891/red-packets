package com.example.redpackets.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 发红包记录
 */
@Data
@TableName(value = "red_record")
public class RedRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 红包标识
     */
    private String redPacket;
    /**
     * 人数
     */
    private Integer total;
    /**
     * 总金额（单位为分）
     */
    private BigDecimal amount;
    /**
     * 是否有效（1-是，0-否）
     */
    private Integer isActive;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
