package com.mozat.morange.base;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Spring管理类
 * 
 */
public class BaseAction {
	private static ApplicationContext ctx = null;

	private static String[] getPath() {
		return new String[] { "applicationContext.xml" };
	//	return new String[] { System.getProperty("user.dir") + "/config_deploy/applicationContext.xml" };
	}

	/**
	 * 获取某个bean
	 * 
	 * @param name
	 *            bean的名字
	 * @return bean的实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		if (ctx == null) {
			initSpring();
		}
		return (T) ctx.getBean(name);
	}

	/**
	 * @name initSpring
	 * @description 初始化spirng配置文件
	 * @condition 用于项目启动时候调用 void
	 * @throws
	 */
	public static void initSpring() {
		ctx = new ClassPathXmlApplicationContext(getPath());
	}

}
