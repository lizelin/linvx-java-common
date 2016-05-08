package net.linvx.java.libs.conf;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

import net.linvx.java.libs.tools.CommonAssistant;
import net.linvx.java.libs.tools.MyLog;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 读取配置文件，使用到了commons-configuration-1.10.jar
 * 
 * @author lizelin
 *
 */
public class MyConfigReader {
	private MyConfigReader() {
	}
	private static Logger log = MyLog.getLogger(MyConfigReader.class);
	private static FileChangedReloadingStrategy strategy = null;
	private static Map<String, Configuration> configs = new HashMap<String, Configuration>();
	
	static {
		strategy = new FileChangedReloadingStrategy();
		strategy.setRefreshDelay(60 * 1000);
		String basePath = CommonAssistant.guessConfigPath();
		File file = new File(basePath);
		if (file.exists() && file.isDirectory()) {
			for (File child : file.listFiles()) {
				String pathName = child.getAbsolutePath();
				String fileName = child.getName();
				if (fileName.equalsIgnoreCase("log4j.properties"))
					continue;
				String key = "";
				if (fileName.endsWith(".xml")) {
					key = fileName.substring(0, fileName.length() - ".xml".length());
					try {
						initXmlConfig(key, pathName);
						log.info("config file [" + pathName + "] has been read!");
					} catch (ConfigurationException e) {
						e.printStackTrace();
						log.error("", e);
					}
				} else if (fileName.endsWith(".properties")) {
					key = fileName.substring(0, fileName.length() - ".properties".length());
					try {
						initPropertyConfig(key, pathName);
						log.info("config file [" + pathName + "] has been read!");
					} catch (ConfigurationException e) {
						e.printStackTrace();
						log.error("", e);
					}
				}
			}
		}
	}

	public static void initXmlConfig(String key, String xmlFile) throws ConfigurationException {
		File file = new File(xmlFile);
		if (!file.exists() || !file.isFile())
			return;
		XMLConfiguration xmlConfig = new XMLConfiguration(xmlFile);
		xmlConfig.setReloadingStrategy(strategy);
		configs.put(key, xmlConfig);
	}

	public static void initPropertyConfig(String key, String props) throws ConfigurationException {
		File file = new File(props);
		if (!file.exists() || !file.isFile())
			return;
		PropertiesConfiguration propConfig = new PropertiesConfiguration(props);
		propConfig.setReloadingStrategy(strategy);
		configs.put(key, propConfig);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Configuration> T getConfig(String key) {
		if (configs.containsKey(key))
			return (T) configs.get(key);
		else
			return null;
	}
}
