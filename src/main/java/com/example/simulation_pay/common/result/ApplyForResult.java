package com.example.simulation_pay.common.result;

import lombok.Data;

@Data
public class ApplyForResult {


    /**
     * 申请记录唯一标识
     * */
    private String uuid;

    /**
     * 修改状态
     * */
    private String status;

}
