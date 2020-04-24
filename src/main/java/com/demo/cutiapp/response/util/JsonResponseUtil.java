package com.demo.cutiapp.response.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import com.demo.cutiapp.response.BaseErrorMessage;
import com.demo.cutiapp.response.BaseResponseMessage;
import com.demo.cutiapp.response.PageableResponse;
import com.demo.cutiapp.response.ResponseCodeEnum;

import java.util.ArrayList;
import java.util.List;

public class JsonResponseUtil<T> {
	
	public static <T> ResponseEntity<BaseResponseMessage<T>> formatSuccessResponse(T object, PageableResponse meta) {
		BaseResponseMessage<T> baseResponseMessage = new BaseResponseMessage<>();
		baseResponseMessage.setStatusCode(ResponseCodeEnum.SUCCESS);
		baseResponseMessage.setErrorMessages(null);
		baseResponseMessage.setData(object);
		baseResponseMessage.setMeta(meta);
		return ResponseEntity.status(HttpStatus.OK).body(baseResponseMessage);
	}
		
	public static <T> ResponseEntity<BaseResponseMessage<T>> formatSuccessResponse(T object) {
		BaseResponseMessage<T> baseResponseMessage = new BaseResponseMessage<>();
		baseResponseMessage.setStatusCode(ResponseCodeEnum.SUCCESS);
		baseResponseMessage.setErrorMessages(null);
		baseResponseMessage.setData(object);
		return ResponseEntity.status(HttpStatus.OK).body(baseResponseMessage);
	}
	
	public static <T> ResponseEntity<BaseResponseMessage<T>> formatErrors(List<FieldError> errors) {
        List<BaseErrorMessage> listErrorMessages = new ArrayList<>();
        for (FieldError error : errors) {
        	listErrorMessages.add(new BaseErrorMessage(error.getField(), error.getDefaultMessage()));
        }  
        BaseResponseMessage<T> baseResponseMessage = new BaseResponseMessage<>();
		baseResponseMessage.setStatusCode(ResponseCodeEnum.VALIDATE_ERROR);
		baseResponseMessage.setErrorMessages(listErrorMessages);
		baseResponseMessage.setData(null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseMessage);
    }
	
	public static <T> ResponseEntity<BaseResponseMessage<T>> formatErrors(List<FieldError> errors, String message) {
        List<BaseErrorMessage> listErrorMessages = new ArrayList<>();
        for (FieldError error : errors) {
        	listErrorMessages.add(new BaseErrorMessage(error.getField(), error.getDefaultMessage()));
        }
        
        BaseResponseMessage<T> baseResponseMessage = new BaseResponseMessage<>();
		baseResponseMessage.setStatusCode(ResponseCodeEnum.VALIDATE_ERROR);
		baseResponseMessage.setMessage(message);
		baseResponseMessage.setErrorMessages(listErrorMessages);
		baseResponseMessage.setData(null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseMessage);
	}
	
	public static <T> ResponseEntity<BaseResponseMessage<T>> formatException(HttpStatus httpStatus, ResponseCodeEnum statusCode) {
		BaseResponseMessage<T> baseResponseMessage = new BaseResponseMessage<>();
		baseResponseMessage.setStatusCode(statusCode);
		baseResponseMessage.setErrorMessages(null);
		baseResponseMessage.setData(null);
		return ResponseEntity.status(httpStatus).body(baseResponseMessage);
	}
	
	public static <T> ResponseEntity<BaseResponseMessage<T>> formatException(HttpStatus httpStatus, ResponseCodeEnum statusCode,String message) {
		BaseResponseMessage<T> baseResponseMessage = new BaseResponseMessage<>();
		baseResponseMessage.setStatusCode(statusCode);
		baseResponseMessage.setMessage(message);
		baseResponseMessage.setErrorMessages(null);
		baseResponseMessage.setData(null);
		return ResponseEntity.status(httpStatus).body(baseResponseMessage);
	}
	
}
