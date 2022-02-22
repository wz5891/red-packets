package com.example.redpackets.controller;

import com.example.redpackets.core.BaseResponse;
import com.example.redpackets.dto.RedPacketDto;
import com.example.redpackets.enums.StatusCode;
import com.example.redpackets.service.IRedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 红包处理逻辑Controller
 */
@RestController
@Slf4j
public class RedPacketController {
    @Resource
    private IRedPacketService redPacketService;

    @PostMapping(value = "/red/packet/hand/out", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse handOut(@Validated @RequestBody RedPacketDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return new BaseResponse(StatusCode.INVALID_PARAMS);
        }

        BaseResponse response = new BaseResponse(StatusCode.SUCCESS);

        String redPacket = redPacketService.handOut(dto);

        response.setData(redPacket);

        return response;
    }

    @GetMapping("/red/packet/rob")
    public BaseResponse rob(@RequestParam("userId") Integer userId,@RequestParam("redPacket") String redPacket) {
        BaseResponse response = new BaseResponse(StatusCode.SUCCESS);

        BigDecimal result = redPacketService.rob(userId, redPacket);

        if (result == null) {
            response = new BaseResponse(StatusCode.FAIL.getCode(), "红包已经被抢完了");
        } else {
            response.setData(result);
        }

        return response;
    }
}
