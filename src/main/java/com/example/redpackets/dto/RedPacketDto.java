package com.example.redpackets.dto;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 发红包请求时接收参数的对象
 */
@Data
@ToString
@NoArgsConstructor
public class RedPacketDto {
    private Integer userId;
    @NotNull
    private Integer total;
    @NotNull
    private Integer amount;
}
