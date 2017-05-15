package com.asiainfo.simulation.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.asiainfo.simulation.aop.advice.IAdvice;

public class ProxyFactoryBean {
	
	// 增强对象 
    private IAdvice advice;
    // 目标对象 
    private Object target;

    // 获取目标对象的代理对象  
    public Object getProxy() {
        Object proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), 
        		new InvocationHandler() {
            		// InvocationHandler接口的匿名内部类
            		// 执行代理对象的任何方法时都将被替换为执行如下invoke方法
            		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            			advice.beforeMethod(method);// 执行【前置】增强方法 
            			Object retVal = null;
            			try {
            				retVal = method.invoke(target, args);// 执行目标方法
            			} catch (InvocationTargetException ex) {
            				advice.onException(method);
            				throw new RuntimeException(ex.getTargetException());
            			}
            			advice.afterMethod(method);// 执行【后置】增强方法
            			return retVal;// 返回目标方法执行结果，代理对象的方法返回值必须与目标对象的方法返回值相同 
            		}
        });
        return proxy;
    }
  
    public void setAdvice(IAdvice advice) {
        this.advice = advice;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
