package com.hys.blog.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResponse {

	private String message;
	private int status;
	private String code;
}
