package net.linvx.java.libs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class MyWebUtils {
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * 获取request数据
	 * @param request
	 * @return
	 */
	public static String getRequestData(HttpServletRequest request) {
		return MyWebUtils.getRequestData(request, DEFAULT_ENCODING);
	}
	
	/**
	 * 获取request数据
	 * @param request
	 * @param encoding
	 * @return
	 */
	public static String getRequestData(HttpServletRequest request, String encoding) {
		if (request.getMethod().equalsIgnoreCase("get"))
			return "";
		InputStream is = null;
		String requestData = null;
		try {
			is = request.getInputStream();
			// 取HTTP请求流长度
			int size = request.getContentLength();
			// 用于缓存每次读取的数据
			byte[] buffer = new byte[size];
			// 用于存放结果的数组
			byte[] dataByte = new byte[size];
			int count = 0;
			int rbyte = 0;
			// 循环读取
			while (count < size) {
				// 每次实际读取长度存于rbyte中
				rbyte = is.read(buffer);
				for (int i = 0; i < rbyte; i++) {
					dataByte[count + i] = buffer[i];
				}
				count += rbyte;
			}
			requestData = new String(dataByte, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return requestData;
	}

	public static void writeResp(HttpServletResponse response, String data) {
		MyWebUtils.writeResp(response, data, DEFAULT_ENCODING);
	}

	/**
	 * 向http response中写回数据
	 * 
	 * @param response
	 * @param data
	 * @param encoding
	 */
	public static void writeResp(HttpServletResponse response, String data, String encoding) {
		Writer writer = null;
		try {
			response.setCharacterEncoding(encoding);
			writer = response.getWriter();
			writer.write(data);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
