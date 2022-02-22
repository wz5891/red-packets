package com.example.redpackets.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 红包明细
 */
@Data
@TableName(value = "red_detail")
public class RedDetail {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 红包记录id
     */
    private Integer recordId;
    /**
     * 红包金额
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
