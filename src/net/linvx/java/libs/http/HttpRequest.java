package net.linvx.java.libs.http;


import net.linvx.java.libs.utils.MyStringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP Request类
 * <p/>
 * Created by lizelin on 16/3/10.
 */
public class HttpRequest {
    /**
     * 连接服务器的超时时间（毫秒）
     */
    private int connectTimeOut;
    /**
     * 从服务器获取数据的超时时间（毫秒）
     */
    private int readTimeOut;
    /**
     * 请求的url
     */
    private String url;
    /**
     * 字符集（post 数据的字符集）
     */
    private String charset;
    /**
     * HTTP Method
     */
    private String method;

    /**
     * Post请求的content type
     */
    private String postContentType;

    /**
     * post data
     */
    private byte[] postData;

    /**
     * 请求headers
     */
    private Map<String, String> requestHeaders;
    /**
     * 请求的Cookies
     */
    private List<HttpCookie> requestCookies;

    /**
     * 请求的事务id，为了异步使用
     */
    private String tid;
    private HttpResponseStreamProcessor responseStreamProcessor = null;

    private HttpRequest() {
    }

    public static HttpRequest createHttpRequest(String _url) {
        return new HttpRequest().setUrl(_url).initRequest()
                .setMethod(HttpConstants.HTTP_METHOD_GET);
    }

    public static HttpRequest createHttpPostRequest(String _url, byte[] _postData) {
        return new HttpRequest().setUrl(_url).initRequest()
                .setMethod(HttpConstants.HTTP_METHOD_POST)
                .setPostData(_postData);
    }

    public static void main(String[] args) {
        HttpRequest req = HttpRequest.createHttpRequest("https://www.aliyun.com/");
        HttpResponse res = req.getResponse();
        System.out.println(res.getResponseCode());
        System.out.println(res.getResponseString());
    }

    /**
     * 初始化请求，主要用default的一些字段填充。
     *
     * @return
     */
    private HttpRequest initRequest() {
        this.requestHeaders = new HashMap<String, String>();
        this.requestCookies = new ArrayList<HttpCookie>();
        return this.setConnectTimeOut(HttpConstants.DEFAULT_CONNECT_TIME_OUT_MS)
                .setReadTimeOut(HttpConstants.DEFAULT_READ_TIME_OUT_MS)
                .setCharset(HttpConstants.DEFAULT_CHARSET)
                .setPostContentType(HttpConstants.DEFAULT_POST_CONTENT_TYPE)
                .setTid(HttpConstants.DEFAULT_HTTP_CONNECTION_TID);
    }

    public HttpRequest setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    public HttpRequest setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public HttpRequest setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HttpRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public HttpRequest setPostContentType(String postContentType) {
        this.postContentType = postContentType;
        return this;
    }

    public HttpRequest setPostData(byte[] postData) {
        this.postData = postData;
        return this;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public List<HttpCookie> getRequestCookies() {
        return requestCookies;
    }

    public String getTid() {
        return tid;
    }

    public HttpRequest setTid(String tid) {
        if (MyStringUtils.isNotEmpty(tid))
            this.tid = tid;
        return this;
    }

    /**
     * 添加header
     *
     * @param key
     * @param value
     * @return
     */
    public HttpRequest addHttpHeader(String key, String value) {
        if (!HttpConstants.HTTP_HEADER_NAME_REQ_COOKIE.equalsIgnoreCase(key))
            this.getRequestHeaders().put(key, value);
        return this;
    }

    /**
     * 添加cookie
     *
     * @param cookie
     * @return
     */
    public HttpRequest addHttpCookie(HttpCookie cookie) {
        boolean found = false;
        for (int i = 0; i < this.getRequestCookies().size(); i++) {
            if (this.getRequestCookies().get(i).equals(cookie)) {
                this.getRequestCookies().remove(i);
                this.getRequestCookies().add(i, cookie);
                found = true;
                break;
            }
        }
        if (!found)
            this.getRequestCookies().add(cookie);
        return this;
    }

    /**
     * 添加cookie
     *
     * @param name
     * @param value
     * @return
     */
    public HttpRequest addHttpCookie(String name, String value) {
        HttpCookie cookie = new HttpCookie();
        cookie.setName(name).setValue(value);
        return addHttpCookie(cookie);
    }

    /**
     * 根据http response header content-type获取charset
     *
     * @param ct
     * @return
     */
    private String getCharsetByResponseContentType(String ct) {
        String ret = null;
        if (!MyStringUtils.isEmpty(ct) && MyStringUtils.containsIgnoreCase(ct, "charset")) {
            String[] tmp = ct.split(";");
            for (int i = 0; i < tmp.length; i++) {
                if (MyStringUtils.containsIgnoreCase(tmp[i], "charset")) {
                    String[] tmp2 = tmp[i].split("=");
                    if (tmp2.length == 2 && !MyStringUtils.isEmpty(tmp2[1]) && tmp2[1].trim().length() > 0) {
                        ret = tmp2[1].trim();
                    }
                }
            }
        }
        return ret;
    }

    public HttpResponse getResponse() {
        HttpResponse res = new HttpResponse(tid);
        HttpURLConnection urlCon = null;
        URL urlInstance = null;
        try {
            urlInstance = new URL(url);
            urlCon = (HttpURLConnection) (urlInstance).openConnection();
            urlCon.setUseCaches(false);
            urlCon.setInstanceFollowRedirects(false);
            // 处理Method
            if (this.getMethod().equalsIgnoreCase(HttpConstants.HTTP_METHOD_POST)
                    && this.postData != null && this.postData.length > 0)
                urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod(this.getMethod());

            // 处理requestHeaders
            if (!this.requestHeaders.containsKey("User-Agent")) {
                this.addHttpHeader("User-Agent", HttpConstants.DEFAULT_USER_AGENT);
            }
            if (!this.requestHeaders.containsKey("Content-Type")
                    && this.getMethod().equalsIgnoreCase(HttpConstants.HTTP_METHOD_POST)) {
                this.addHttpHeader("Content-Type", this.postContentType);
            }

            for (Entry<String, String> entry : requestHeaders.entrySet()) {
                if (!MyStringUtils.isEmpty(entry.getValue()))
                    urlCon.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // 处理cookie
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.requestCookies.size(); i++) {
                sb.append(this.requestCookies.get(i).toCookieHeaderFormat());
            }
            if (this.requestCookies.size() > 0)
                urlCon.setRequestProperty("Cookie", sb.toString());

            // 设置超时时间
            urlCon.setReadTimeout(readTimeOut);
            urlCon.setConnectTimeout(connectTimeOut);

            // 设置几个关键属性（如果requestHeaders中没有，才增加）
            if (this.getMethod().equalsIgnoreCase(HttpConstants.HTTP_METHOD_POST))
                urlCon.addRequestProperty("Content-Type", HttpConstants.DEFAULT_POST_CONTENT_TYPE);
            urlCon.addRequestProperty("Connection", "Keep-Alive");
            urlCon.addRequestProperty("Accept-Encoding", "gzip, deflate");
            urlCon.connect();

            if (method.equalsIgnoreCase(HttpConstants.HTTP_METHOD_POST) && this.postData != null && this.postData.length > 0) {
                urlCon.getOutputStream().write(this.postData, 0, this.postData.length);
                urlCon.getOutputStream().flush();
                urlCon.getOutputStream().close();
            }

            int code = urlCon.getResponseCode();
            res.setResponseCode(code);

            // 处理responseHeaders
            res.fetchResponseHeaders(urlCon.getHeaderFields());

            // 处理responseCookie
            res.fetchResponseCookies(urlCon.getHeaderFields().get(
                    HttpConstants.HTTP_HEADER_NAME_RES_COOKIE));

            // 处理 charset
            String cs = getCharsetByResponseContentType(res.getResponseHeaderFirstValue("Content-Type"));
            if (MyStringUtils.isNotEmpty(cs))
                res.setResponseCharset(cs);

            InputStream is = null;

            try {
                if (code == 200) {
                    is = urlCon.getInputStream();
                } else {
                    is = urlCon.getErrorStream();
                }
                if (null == is) {
                    res.setResponseBytes(null);
                } else {
                    boolean hasConsume = false;
                    if (this.getResponseStreamProcessor()!=null)
                        hasConsume = this.getResponseStreamProcessor().consumeInputStream(res, is);
                    if (hasConsume==false)
                        new DefaultHttpResponseStreamProcessor().consumeInputStream(res, is);

                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                if (is != null)
                    is.close();
            }
        } catch (MalformedURLException e) {
            res.setResponseCode(-1001);
            e.printStackTrace();
        } catch (ProtocolException e) {
            res.setResponseCode(-1002);
            e.printStackTrace();
        } catch (IOException e) {
            res.setResponseCode(-1003);
            e.printStackTrace();
        } catch (Exception e) {
            res.setResponseCode(-1004);
            e.printStackTrace();
        } finally {
            if (urlCon != null) {
                urlCon.disconnect();
            }
            if (urlInstance != null) {
                urlInstance = null;
            }
        }
        return res;
    }

    public void releaseMe() {
        if (null != requestCookies) {
            requestCookies.clear();
            requestCookies = null;
        }
        if (null != requestHeaders) {
            requestHeaders.clear();
            requestHeaders = null;
        }
        if (null != postData)
            postData = null;

    }

    public HttpResponseStreamProcessor getResponseStreamProcessor() {
        return responseStreamProcessor;
    }

    public HttpRequest setResponseStreamProcessor(HttpResponseStreamProcessor responseStreamProcessor) {
        this.responseStreamProcessor = responseStreamProcessor;
        return this;
    }
}
