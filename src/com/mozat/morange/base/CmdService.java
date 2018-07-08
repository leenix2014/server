package com.mozat.morange.base;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 所有cmd Service必须实现此接口
 * 
 */
public abstract class CmdService {

	@Autowired
	private CmdDispatch cmdDispatch;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initialize() {
		Class<?> clz = this.getClass();
		for (Method method : clz.getMethods()) {
			Cmd cmd = method.getAnnotation(Cmd.class);
			if (cmd != null) {
				cmdDispatch.addCmdMethod(cmd.cmd(), this, method,
						method.getParameterTypes(),
						cmd.paramNames().split("\\,"));
			}
		}
	}
}
