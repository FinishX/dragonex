/**   
* @Title: WebAppConfig.java 
* @Package com.example.demo.interceptors 
* @Description:
* @author hjq  
* @date 2018年4月27日 上午9:37:45 
* @version V1.0   
*/
package com.example.demo.interceptors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** 
* @ClassName: WebAppConfig 
* @Description:
* @author hjq
* @date 2018年4月27日 上午9:37:45 
*  
*/
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter{

	public void addInterceptors(InterceptorRegistry registry){
		System.out.println(1);
	}
}
