package com.example.simulation_pay.common.test;

import cn.hutool.core.util.IdUtil;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

public class TestA {
    public static void main(String[] args) {
        MyUUIDCreate();
    }
    public static String MyUUIDCreate(){
        Date date = new Date();
        int c=(int)('A'+Math.random()*('Z'-'A'+1));
        String time=date.getTime()+"";
        System.out.println(c+time);
        System.out.println((int)('A'+Math.random()*('Z'-'A'+1))+(new Date().getTime()+""));
        return null;
    }
}
