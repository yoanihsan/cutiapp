package com.demo.cutiapp.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BaseResponseMessage<T> {

    private String statusCode;
    private String message;
    private List<BaseErrorMessage> errorMessages;
    private T data;
    private PageableResponse meta;

    public void setStatusCode(String statusCode) {
    	this.statusCode = statusCode;
    }
    
    public void setStatusCode(ResponseCodeEnum responseCodeEnum) {
        this.statusCode = responseCodeEnum.getCode();
        this.message = responseCodeEnum.getDescription();
    }
    
}
