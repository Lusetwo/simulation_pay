package com.example.simulation_pay.common.util;

import cn.hutool.core.util.IdUtil;

public class MyUUid {

    /**
     * 为了防止某些对uuid长度有想法的哈批特设此类
     * */
    public static String MyUUIDCreate(){
        String uuid= IdUtil.simpleUUID().substring(0,10);
        System.out.println("生成一个九位数的uuid:"+uuid);
        return uuid;
    }
}
