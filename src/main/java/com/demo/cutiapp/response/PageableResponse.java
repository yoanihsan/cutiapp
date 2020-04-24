package com.demo.cutiapp.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PageableResponse {
	private Long totalRecord;
	private Object pageable;
}
