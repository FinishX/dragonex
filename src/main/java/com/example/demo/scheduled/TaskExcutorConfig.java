/**   
* @Title: TaskExcutorConfig.java 
* @Package com.example.demo.scheduled 
* @Description:
* @author hjq  
* @date 2018年5月8日 下午3:50:15 
* @version V1.0   
*/
package com.example.demo.scheduled;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** 
* @ClassName: TaskExcutorConfig 
* @Description:
* @author hjq
* @date 2018年5月8日 下午3:50:15 
*  
*/
@Configuration
public class TaskExcutorConfig implements AsyncConfigurer{
	@Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(100);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }

    class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            //do something
        	System.out.println("不明含义啊");
        }

    }

}
