package com.yexf.imcommon;

import com.yexf.imcommon.enums.ResultCodeEnum;
import lombok.Getter;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    private final int code;

    public BusinessException(String msg) {

        super(msg);
        this.code = 400;
    }


    public BusinessException(ResultCodeEnum resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
}
