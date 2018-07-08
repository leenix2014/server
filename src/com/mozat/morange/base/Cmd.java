package com.mozat.morange.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Cmd {

	/**
	 * 命令
	 * 
	 * @return
	 */
	String cmd();

	/**
	 * @return 方法参数名列表,格式:变量名,变量名,没有参数为空字符
	 */
	String paramNames() default "";
}
