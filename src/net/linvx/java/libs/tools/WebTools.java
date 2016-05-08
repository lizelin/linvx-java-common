package net.linvx.java.libs.tools;

import net.linvx.java.libs.http.HttpHelper;
import net.linvx.java.libs.utils.MyStringUtils;
import net.sf.json.JSONObject;
/**
 * 常用的Web类，包括：
 * 一，根据ip获取城市信息
 * public static JSONObject ip2CityJson(String ip)
 * public static JSONObject ip2CityName(String ip)
 * 使用到了json包：
 * commons-beanutils-1.8.0.jar
 * commons-beanutils-core-1.8.0.jar
 * commons-collections-3.1.jar
 * commons-lang-2.4.jar
 * commons-logging.jar
 * ezmorph-1.0.6.jar
 * json-lib-2.3-jdk15.jar
 * 
 * 
 * @author lizelin
 *
 */
public class WebTools {
	private static final String TAOBAO_IP_SERVICE = "http://ip.taobao.com/service/getIpInfo.php?ip=";
	private WebTools() {
	}

	
	/**
	 * 根据ip获取城市信息：
	 * {
	 *     "code": 0, 
	 *     "data": {
     * 		"country": "中国", 
     *     "country_id": "CN", 
     *        "area": "华北", 
     *        "area_id": "100000", 
     *        "region": "北京市", 
     *        "region_id": "110000", 
     *        "city": "北京市", 
     *        "city_id": "110100", 
     *        "county": "", 
     *        "county_id": "-1", 
     *        "isp": "联通", 
     *        "isp_id": "100026", 
     *        "ip": "202.106.0.20"
     *     }
	 *  }
	 * @param ip
	 * @return
	 */
	public static JSONObject ip2CityJson(String ip) {
		String jsonRet = HttpHelper.httpGet(TAOBAO_IP_SERVICE + ip);
		if (MyStringUtils.isEmpty(jsonRet))
			return null;
		JSONObject json = JSONObject.fromObject(jsonRet);
		if (json==null || json.optInt("code", -1)!=0 )
			return null;
		return json.optJSONObject("data");
	}
	
	/*
	 * 根据城市获取城市名称
	 */
	public static String ip2CityName(String ip) {
		JSONObject json = WebTools.ip2CityJson(ip);
		if (json == null)
			return "";
		else
			return json.optString("city", "");
	}
	
	public static void main(String[] args) {
		System.out.println(WebTools.ip2CityJson("113.108.161.218"));
		System.out.println(WebTools.ip2CityName("123.120.199.216"));
	}

}
