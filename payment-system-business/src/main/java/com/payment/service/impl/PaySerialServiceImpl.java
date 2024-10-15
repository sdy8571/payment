package com.payment.service.impl;

import com.framework.sequence.sequence.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.payment.contants.enums.PaySerialSequenceEnum;
import com.payment.service.PaySerialService;

import javax.annotation.Resource;

/**
 * @author sdy
 * @description
 * @date 2024/6/15
 */
@Slf4j
@Service
public class PaySerialServiceImpl implements PaySerialService {

    @Resource
    private Sequence sequence;

    @Override
    public String getSerialNo() {
        return sequence.getSequence(PaySerialSequenceEnum.SerialNo.name());
    }

}
