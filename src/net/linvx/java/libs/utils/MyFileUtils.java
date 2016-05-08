package net.linvx.java.libs.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MyFileUtils {

	private MyFileUtils() {
	}
	
	/**
	 * 读取文件内容，返回字符串
	 * @param filePath
	 * @param encoding
	 * @return
	 */
	public static String readFile(String filePath, String encoding) {
		byte[] bs = MyFileUtils.readFile(filePath);
		if (bs == null) return "";
		String ret = "";
		try {
			ret = new String(bs, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 读取文件内容
	 * @param filePath
	 * @return
	 */
	public static byte[] readFile(String filePath) {
		byte[] ret = null;
		InputStreamReader read = null;
		ByteArrayOutputStream baos = null;
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				read = new InputStreamReader(new FileInputStream(file));
				int ch;
				baos = new ByteArrayOutputStream();
				while ((ch = read.read()) != -1) {
					baos.write(ch);
				}
				if (baos.size() >= 0)
					ret = baos.toByteArray();
			} else {

			}
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定的文件");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("读取文件出错");
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				baos = null;
			}
			if (read != null) {
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				read = null;
			}
		}
		return ret;
	}

	/**
	 * 读取key=value结构的文件
	 * 
	 * @param name
	 *            类似于："/date.properties"
	 * @return
	 * @throws IOException
	 */
	public static Properties readPropertyResource(String name)
			throws IOException {
		InputStream is = MyFileUtils.class.getResourceAsStream(name);
		Properties thisProps = new Properties();
		thisProps.load(is);
		return thisProps;
	}

	/**
	 * 读取properties文件
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Properties readPropertyFile(String fileName) throws FileNotFoundException, IOException {
		Properties thisProps = new Properties();
		thisProps.load(new FileInputStream(fileName));
		return thisProps;
	}
	/**
	 * 获取文件最后修改时间
	 * @param fileName
	 * @return Long （毫秒数，自1970 gmt）
	 */
	public static Long getFileLastModified(String fileName) {
		File file = new File(fileName);
		return file==null || !file.exists() ? 0l: file.lastModified();
	}
	
	/**
	 * 判断文件自lastModified后，是否改变
	 * @param fileName
	 * @param lastModified （毫秒数，自1970 gmt）
	 * @return
	 */
	public static boolean isFileModifiedSince(String fileName, Long lastModified) {
		return MyFileUtils.getFileLastModified(fileName) > lastModified;
	}
	
	/**
	 * 文件是否存在
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExists(String fileName){
		File file = new File(fileName);
		return file != null && file.exists() && file.isFile();			
	}
	
	/**
	 * 路径是否存在
	 * @param dirName
	 * @return
	 */
	public static boolean isDirExists(String dirName) {
		File file = new File(dirName);
		return file != null && file.exists() && file.isDirectory();
	}
	
	public static void main(String[] args) {
//		// ***************BEGIN MyFileUtils.readFile example************************
//		System.out.println(MyFileUtils.readFile("e:/a.txt", "GBK"));
//		// ***************END    MyFileUtils.readFile example************************

//		// ***************BEGIN MyFileUtils.readPropertyFile example************************
//		 try {
//		 Properties p = MyFileUtils.readPropertyFile("/date.properties");
//		 String v1 = p.getProperty("key1");
//		 String v2 = p.getProperty("key2");
//		 System.out.println(v1);
//		 System.out.println(v2);
//		 } catch (IOException e) {
//		 e.printStackTrace();
//		 }
//		// ***************END    MyFileUtils.readPropertyFile example************************
	}

}
