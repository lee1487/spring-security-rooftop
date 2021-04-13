package com.hys.blog.core.security;

public interface AuthToken<T> {
	boolean validate();
	T getData();
}
