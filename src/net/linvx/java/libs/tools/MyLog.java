/**
 * 
 */
package net.linvx.java.libs.tools;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import net.linvx.java.libs.conf.MyConfigReader;

/**
 * Log4J的简单wrapper
 * @author lizelin
 * 用到了jar包：log4j-1.2.14.jar
 * 主要是指定了log4j的配置文件名称和路径
 * （在根下[即和顶层包同一路径]，文件名log4j.properties）
 * 注意：log4j.properties文件中的log4f.logger.net.linvx的net.linvx实际上有过滤器的作用
 * log4j.properties文件示例：

log4j.rootLogger=INFO,C
log4j.logger.net.linvx=DEBUG,R  

log4j.appender.C=org.apache.log4j.ConsoleAppender
log4j.appender.C.layout=org.apache.log4j.PatternLayout
log4j.appender.C.layout.ConversionPattern=[%p] %-d{yy-MM-dd HH:mm:ss} [%c : %L] %m%n

log4j.appender.R=org.apache.log4j.DailyRollingFileAppender
log4j.appender.R.File=E:/techwks/eclipse/LinvxTools/logs/log4j-app.log
log4j.appender.R.layout=org.apache.log4j.PatternLayout
log4j.appender.R.layout.ConversionPattern=[%p] %-d{yy-MM-dd HH:mm:ss} [%c : %L] %m%n

log4j.appender.WEB=org.apache.log4j.DailyRollingFileAppender
log4j.appender.WEB.File=E:/techwks/eclipse/LinvxTools/logs/log4j-web.log
log4j.appender.WEB.layout=org.apache.log4j.PatternLayout
log4j.appender.WEB.layout.ConversionPattern=[%p] %-d{yy-MM-dd HH:mm:ss} [%c : %L] %m%n

 *
 */
public class MyLog {
	static {
		String logpath = CommonAssistant.guessLogPath();
		System.setProperty("MYLOGDIR", logpath);
		PropertyConfigurator.configureAndWatch(CommonAssistant.guessConfigPath() + "/log4j.properties");
		Logger.getLogger(MyLog.class).info("MyLog init end!");
	}
	
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class c) {
		return Logger.getLogger(c);
	}
	
	public static void main(String[] args) {
		
	}

}
