package com.mozat.morange.base;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CmdDispatch {

	/**
	 * cmd->实例名,方法名,参数类型,参数名
	 */
	private Map<String, Object[]> cmdMethodMap = new HashMap<String, Object[]>();

	/**
	 * 根据cmd获取方法
	 * 
	 * @param cmd
	 * @return
	 */
	public Object[] getCmdMethod(String cmd) {
		return cmdMethodMap.get(cmd);
	}

	/**
	 * 添加cmd相应方法
	 * 
	 * @param cmd
	 * @param bean
	 * @param method
	 * @param paramsType
	 * @param paramsName
	 */
	public void addCmdMethod(String cmd, Object bean, Method method,
			Class<?>[] paramsType, String[] paramsName) {
		cmdMethodMap.put(cmd, new Object[] { bean, method, paramsType,
				paramsName });
	}
}
