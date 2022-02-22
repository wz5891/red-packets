package com.example.redpackets.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
@Slf4j
class RedPacketUtilTest {

    @Test
    void divideRedPackage() {
        Integer totalAmount = 10 * 100;
        Integer totalPeopleNum = 10;
        List<Integer> list = RedPacketUtil.divideRedPackage(totalAmount, totalPeopleNum);


        Integer total = 0;
        for (Integer amount : list) {
            total += amount;
            log.info("{}",amount);
        }
        log.info("总红包金额：{}",total);
    }
}