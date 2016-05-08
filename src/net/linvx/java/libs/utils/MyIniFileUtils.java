package net.linvx.java.libs.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.ini4j.Ini;
import org.ini4j.Ini.Section;
import org.ini4j.InvalidIniFormatException;


/**
 * 读取ini文件 用到了org.ini4j包: ini4j-0.4.1.jar 关于路径问题：
 * 第一种情况：resource
 * /data.property路径，文件实际存在于class的首层。本例中就是与net目录同级
 * 第二种情况：文件路径
 * @author lizelin ini文件示例： [session1] key1 = 1 key2 = aaaaaaaaaa aaaaaa
 * 
 *         [session2] key1 = 2 key2 = bbbbbbbb bbbb
 * 
 */
public class MyIniFileUtils {

	private MyIniFileUtils() {
	}

	/**
	 * 读取ini资源
	 * 
	 * @param name
	 *            类似于："/data.ini"
	 * @return
	 * @throws InvalidIniFormatException
	 * @throws IOException
	 */
	public static Ini readIniResource(String name) throws InvalidIniFormatException, IOException {
		Ini ini = new Ini();
		ini.load(MyIniFileUtils.class.getResourceAsStream(name));
		return ini;
	}

	/**
	 * 读取ini文件
	 * @param fileName
	 * 			类似于：/var/aaa.ini
	 * @return
	 * @throws InvalidIniFormatException
	 * @throws IOException
	 */
	public static Ini readIniFile(String fileName) throws InvalidIniFormatException, IOException {
		Ini ini = new Ini();
		ini.load(new FileInputStream(fileName));
		return ini;
	}
	
	/**
	 * 从url获取ini文件
	 * @param url
	 * @return
	 * @throws InvalidIniFormatException
	 * @throws IOException
	 */
	public static Ini readIniUrl(URL url) throws InvalidIniFormatException, IOException {
		Ini ini = new Ini();
		ini.load(url);
		return ini;
	}
	
	/**
	 * 读取ini文件中指定section的指定key的值
	 * 
	 * @param ini
	 * @param section
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getIniProperty(Ini ini, String section, String key, String defaultValue) {
		Ini.Section iniSection = ini.get(section);
		if (iniSection == null)
			return defaultValue;
		String v = iniSection.get(key);
		return net.linvx.java.libs.utils.MyStringUtils.isEmpty(v) ? defaultValue : v;
	}

	/**
	 * 获取所有Section
	 * 
	 * @param ini
	 * @return
	 */
	public static List<String> getIniSections(Ini ini) {
		List<String> list = new ArrayList<String>();
		for (Entry<String, Section> entry : ini.entrySet()) {
			list.add(entry.getKey());
		}
		return list;
	}

	public static void main(String[] args) {
		Ini p;
		try {
			p = MyIniFileUtils.readIniResource("/data.properties");
			System.out.println(MyIniFileUtils.getIniSections(p));
			String v1 = MyIniFileUtils.getIniProperty(p, "aaa", "key1", "non");
			String v2 = MyIniFileUtils.getIniProperty(p, "section2", "key2", "non");
			System.out.println(v1);
			System.out.println(v2);
		} catch (InvalidIniFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
