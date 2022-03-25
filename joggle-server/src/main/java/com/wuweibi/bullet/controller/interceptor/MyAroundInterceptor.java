package com.wuweibi.bullet.controller.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyAroundInterceptor implements MethodInterceptor
{
	 //实现MethodInterceptor接口必须实现invoke方法
	 public Object invoke(MethodInvocation invocation) throws Throwable
	 {
	   //调用目标方法之前执行的动作
	   System.out.println("调用方法之前: invocation对象：[" + invocation + "]");
	   //调用目标方法
	   Object rval = invocation.proceed();
	   //调用目标方法之后执行的动作
	   System.out.println("调用结束...");
	   return rval;
	 }

	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2,
			MethodProxy arg3) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
}