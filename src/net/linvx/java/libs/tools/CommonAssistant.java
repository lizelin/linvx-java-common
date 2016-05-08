/**
 * 
 */
package net.linvx.java.libs.tools;

import java.net.URL;

/**
 * 常用的一些类工具，比如路径转换等
 * @author lizelin
 *
 */
public class CommonAssistant {
	/**
	 * 获取资源的路径（文件系统路径）
	 * @param name
	 * @return
	 */
	public static String getResourcePath(String name) {
		URL u = CommonAssistant.class.getClassLoader().getResource(name);
		return u.getPath();
	}
	
	/**
	 * 获取资源的根路径：
	 * 一般情况下，对于java application，路径为/x/y/z/projectfolder/build/classes/
	 * 对于web app，路径为：/x/y/z/projectfolder/WEB-INF/classes/
	 * @return
	 */
	public static String getResourceRootPath() {
		return CommonAssistant.getResourcePath("");
	}
	
	/**
	 * 猜测conf目录
	 * 一般约定：java application程序的配置一般为和build（即classes的上一级）同级的conf
	 * web app，路径为/x/y/z/projectfolder/WEB-INF/conf
	 * @return
	 */
	public static String guessConfigPath(){
		String path = getResourceRootPath();
		if (path.endsWith("/build/classes/"))
			return path.substring(0, path.length()-"/build/classes/".length()) + "/conf";
		else if (path.endsWith("/WEB-INF/classes/"))
			return path.substring(0, path.length()-"/classes/".length()) + "/conf";
		else
			return path;
	}
	
	/**
	 * 猜测logs目录
	 * 一般约定：java application程序的配置一般为和build（即classes的上一级）同级的logs
	 * web app，路径为/x/y/z/projectfolder/WEB-INF/logs
	 * @return
	 */
	public static String guessLogPath(){
		String path = getResourceRootPath();
		if (path.endsWith("/build/classes/"))
			return path.substring(0, path.length()-"/build/classes/".length()) + "/logs";
		else if (path.endsWith("/WEB-INF/classes/"))
			return path.substring(0, path.length()-"/classes/".length()) + "/logs";
		else
			return path;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
