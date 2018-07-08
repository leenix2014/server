package com.mozat.morange.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

public class CmdHandler {

	@Autowired
	private CmdDispatch cmdDispatch;

	/**
	 * 处理请求
	 * 
	 * @param uri
	 * @param paramValues
	 *            参数值
	 */
	public Object doRequest(Map<String, String[]> paramValues, int port) {
		// 验证参数合法性
		return new Object();
	}

}
