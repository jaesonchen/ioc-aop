package com.asiainfo.simulation.aop.advice;

import java.lang.reflect.Method;

public interface IAdvice {
	
	// 前置增强方法 
    void beforeMethod(Method method);
  
    // 后置增强方法 
    void afterMethod(Method method);
}
