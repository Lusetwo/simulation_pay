package com.example.simulation_pay.teacherDuen.qrcodeMoudle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.simulation_pay.teacherDuen.qrcodeMoudle.entity.Qrcode;
import com.example.simulation_pay.teacherDuen.qrcodeMoudle.mapper.QrcodeMapper;
import com.example.simulation_pay.teacherDuen.qrcodeMoudle.service.IQrcodeService;
import org.springframework.stereotype.Service;

@Service
public class QrcodeServiceImpl extends ServiceImpl<QrcodeMapper, Qrcode> implements IQrcodeService {
}
