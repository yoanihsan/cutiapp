package com.demo.cutiapp.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum ResponseCodeEnum {

	CONFLICT("409","Object already exist"),
	SUCCESS("200", "Success"), 
	VALIDATE_ERROR("400", "Validation error"),
	INVALID_ACCESS_TOKEN("401", "Invalid access token"),
	OBJ_NOT_FOUND("404", "Object not found"),
	UNPROCESSABLE_ENTITY("422","Your user ID did not registered on our Database"),
	NULL("200","Null Object"),
	TIMEOUT("504","Timed Out By API Gateway"),
	BAD_GATEWAY("502","Server do not give any Response");

	private String code;
	private String description;

}
