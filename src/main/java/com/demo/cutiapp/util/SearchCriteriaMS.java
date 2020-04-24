package com.demo.cutiapp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class SearchCriteriaMS {

	private String[] key;
    private String operation; 
    private Object[] value;
}