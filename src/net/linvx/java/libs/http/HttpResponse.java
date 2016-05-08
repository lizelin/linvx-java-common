package net.linvx.java.libs.http;


import net.linvx.java.libs.utils.MyStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lizelin on 16/3/11.
 */
public class HttpResponse {
    private String responseCharset;

    private byte[] responseBytes;

    private Map<String, List<String>> responseHeaders;

    private List<HttpCookie> responseCookies;

    private String tid;

    private int responseCode;

    public HttpResponse() {
        initResponse();
    }

    public HttpResponse initResponse() {
        setResponseCharset(HttpConstants.DEFAULT_CHARSET);
        responseHeaders = new HashMap<String, List<String>>();
        responseCookies = new ArrayList<HttpCookie>();
        this.setTid(HttpConstants.DEFAULT_HTTP_CONNECTION_TID);
        responseCode = 0;
        return this;
    }

    public void releaseMe() {
        this.setResponseBytes(null);
        if (null != getResponseCookies()) {
            getResponseCookies().clear();
            responseCookies = null;
        }
        if (null != getResponseHeaders()) {
            getResponseHeaders().clear();
            responseHeaders = null;
        }

        if (null!=this.extraDatas) {
            extraDatas.clear();
        }


    }

    public HttpResponse(String tid) {
        initResponse();
        this.setTid(tid);
    }


    /**
     * 响应内容的编码
     */
    public String getResponseCharset() {
        return responseCharset;
    }

    public HttpResponse setResponseCharset(String responseCharset) {
        this.responseCharset = responseCharset;
        return this;
    }

    /**
     * 响应数据
     */
    public byte[] getResponseBytes() {
        return responseBytes;
    }

    public HttpResponse setResponseBytes(byte[] responseBytes) {
        this.responseBytes = responseBytes;
        return this;
    }

    /**
     * 响应的headers
     */
    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public HttpResponse fetchResponseHeaders(Map<String, List<String>> map) {
        this.getResponseHeaders().clear();
        this.getResponseHeaders().putAll(map);
        if (this.getResponseHeaders().containsKey(HttpConstants.HTTP_HEADER_NAME_RES_COOKIE))
            this.getResponseHeaders().remove(HttpConstants.HTTP_HEADER_NAME_RES_COOKIE);
        return this;
    }

    /**
     * 获取header信息
     *
     * @param name
     * @return
     */
    public List<String> getResponseHeader(String name) {
        if (MyStringUtils.isEmpty(name) || !this.getResponseHeaders().containsKey(name))
            return null;
        return getResponseHeaders().get(name);
    }

    /**
     * 获取header的第一个值（一般来讲都是只有一个值）
     *
     * @param name
     * @return
     */
    public String getResponseHeaderFirstValue(String name) {
        List<String> list = this.getResponseHeader(name);
        return list!=null && list.size()>0 ? list.get(0) : "";
    }


    /**
     * 响应的cookies（通过header Set-Cookie头获得）
     */
    public List<HttpCookie> getResponseCookies() {
        return responseCookies;
    }

    /**
     * 根据响应设置cookies
     *
     * @param cookiestrings
     * @return
     */
    public HttpResponse fetchResponseCookies(List<String> cookiestrings) {
        this.getResponseCookies().clear();
        if (null != cookiestrings && cookiestrings.size() > 0) {
            for (String cookie_str : cookiestrings) {
                HttpCookie lc = HttpCookie.parseCookieString(cookie_str);
                if (null != lc)
                    this.getResponseCookies().add(lc);
            }
        }
        return this;
    }

    /**
     * 获取Cookie
     *
     * @param name
     * @return
     */
    public HttpCookie getResponseCookie(String name) {
        if (MyStringUtils.isEmpty(name))
            return null;
        for (HttpCookie c : responseCookies) {
            if (name.equals(c.getName()))
                return c;
        }
        return null;
    }

    /**
     * 事务ID，为异步回调服务
     */
    public String getTid() {
        return tid;
    }

    public HttpResponse setTid(String tid) {
        this.tid = tid;
        return this;
    }

    /**
     * 获取字符串
     *
     * @return
     */
    public String getResponseString() {
        return MyStringUtils.getString(this.getResponseBytes(), this.getResponseCharset());
    }

    /**
     * 根据编码获取字符串
     *
     * @param charset
     * @return
     */
    public String getResponseString(String charset) {
        return MyStringUtils.getString(this.getResponseBytes(), charset);
    }

    public int getResponseCode() {
        return responseCode;
    }

    public HttpResponse setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    private Map<String, Object> extraDatas = new HashMap<String, Object>();
    public Object getExtraData(String key) {
        if (extraDatas.containsKey(key))
            return extraDatas.get(key);
        else
            return null;
    }

    public HttpResponse addExtraData(String key, Object obj) {
        if (MyStringUtils.isNotEmpty(key) && obj!=null)
            extraDatas.put(key, obj);
        return this;
    }
    
    public boolean isSuccess() {
    	return this.getResponseCode() == 200;
    }
}
