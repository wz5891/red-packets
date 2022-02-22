package com.example.redpackets.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedPacketUtil {
    /**
     * 二倍均值算法发红包
     * @param totalAmount 红包总金额-单位为分
     * @param totalPeopleNum 总人数
     * @return
     */
    public static List<Integer> divideRedPackage(Integer totalAmount, Integer totalPeopleNum) {
        List<Integer> amountList = new ArrayList<>();
        if(totalAmount>0 && totalPeopleNum>0){
            Integer restAmount = totalAmount;
            Integer restPeopleNum = totalPeopleNum;

            Random random = new Random();

            for(int i=0;i<totalPeopleNum-1;i++){
                int amount = random.nextInt(restAmount / restPeopleNum * 2-1)+1;

                restAmount -= amount;
                restPeopleNum --;

                amountList.add(amount);
            }

            amountList.add(restAmount);
        }

        return amountList;
    }
}
