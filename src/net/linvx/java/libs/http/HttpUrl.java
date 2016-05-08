package net.linvx.java.libs.http;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import net.linvx.java.libs.tools.MyLog;
import net.linvx.java.libs.utils.MyStringUtils;

public class HttpUrl {
	private String path = "";
	private String query = "";
	private URL url = null;
	public HttpUrl(HttpServletRequest req){
		path = req.getRequestURL().toString();
		query = req.getQueryString();
		try {
			url = new URL(path + (MyStringUtils.isEmpty(query)?"":("?"+query)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public String getSchemaHostPortPath() {
		String path = url.getPath();
		return this.getSchemaHostPort() + path;
	}
	
	public String getSchemaHostPort() {
		String schema = url.getProtocol();
		String host = url.getHost();
		int port = url.getPort();
		return schema + "://" + host + (port==80||port==-1?"":":"+port);
	}
	public HttpUrl(String _url) {
		try {
			url = new URL(_url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public String getUrlString(){
		return url.toString();
	}
	
	public URL getUrl(){
		return url;
	}
	
	public static String addParam(String url, String key, String value) {
		String ret = "";
		String[] s1 = url.split("#");
		String[] s2 = s1[0].split("\\?");
		try {
			if (s2.length == 1) {
				ret = s1[0] + "?" + key + "=" + URLEncoder.encode(value, HttpConstants.DEFAULT_CHARSET);
			} else {
				ret = s1[0] + "&" + key + "=" + URLEncoder.encode(value, HttpConstants.DEFAULT_CHARSET);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (s1.length==1) {
			return ret;
		} else {
			return ret + "#" + s1[1];
		}
	}
	public static void main(String[] args){
		HttpUrl web = new HttpUrl("http://www.haishang360.com/m/a.jsp?a=0#mmm");
		String s = web.getUrlString();
		System.out.println(web.getSchemaHostPortPath()+"[]");
		System.out.println(HttpUrl.addParam("http://www.haishang360.com/api/s.do?aaaa=111#123", "key", "222222"));
		
	}
	
	
}
