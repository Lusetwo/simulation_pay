package com.example.simulation_pay.common.util;

import cn.hutool.core.util.IdUtil;

public class QRcodeIdUtil {
    public static Integer QrcodeRandom(){
        int qrId = (int)((Math.random()*9+1)*100000);
        return qrId;
    }

}
