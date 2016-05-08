package net.linvx.java.libs.http;


import net.linvx.java.libs.utils.MyStringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by lizelin on 16/3/23.
 */
public class HttpHelper {
    private HttpHelper() {
    }

    /**
     * http get helper
     *
     * @param uri
     * @return
     */
    public static String httpGet(String uri) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_GET, null, null, null, null).getResponseString();
    }

    /**
     * http get helper
     *
     * @param uri
     * @param tid
     * @return
     */
    public static String httpGet(String uri, String tid) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_GET, null, null, null, tid).getResponseString();
    }

    /**
     * http get helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @return
     */
    public static String httpGet(String uri, Map<String, String> headers, List<HttpCookie> cookies) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_GET, headers, cookies, null, null).getResponseString();
    }

    /**
     * http get helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @param tid
     * @return
     */
    public static String httpGet(String uri, Map<String, String> headers, List<HttpCookie> cookies, String tid) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_GET, headers, cookies, null, tid).getResponseString();
    }

    /**
     * http post helper
     *
     * @param uri
     * @param postData
     * @return
     */
    public static String httpPost(String uri, byte[] postData) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_POST, null, null, postData, null).getResponseString();
    }

    /**
     * http post helper
     *
     * @param uri
     * @param postData
     * @param tid
     * @return
     */
    public static String httpPost(String uri, byte[] postData, String tid) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_POST, null, null, postData, tid).getResponseString();
    }

    /**
     * http post helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @param postData
     * @return
     */
    public static String httpPost(String uri, Map<String, String> headers, List<HttpCookie> cookies, byte[] postData) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_POST, headers, cookies, postData, null).getResponseString();
    }

    /**
     * @param uri
     * @param headers
     * @param cookies
     * @param postData
     * @param tid
     * @return
     */
    public static String httpPost(String uri, Map<String, String> headers, List<HttpCookie> cookies, byte[] postData, String tid) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_POST, headers, cookies, postData, tid).getResponseString();
    }

    /**
     * 执行http request
     *
     * @param uri
     * @param method
     * @param headers
     * @param cookies
     * @param postData
     * @param tid
     * @return HttpResponse
     */
    public static HttpResponse execute(String uri,
                                          String method,
                                          Map<String, String> headers,
                                          List<HttpCookie> cookies,
                                           byte[] postData,
                                          String tid
    ) {
        if (MyStringUtils.isEmpty(uri))
            return null;
        HttpRequest req = null;
        if (MyStringUtils.isEmpty(method))
            method = HttpConstants.HTTP_METHOD_GET;

        if (method.equalsIgnoreCase(HttpConstants.HTTP_METHOD_GET))
            req = HttpRequest.createHttpRequest(uri);
        else
            req = HttpRequest.createHttpPostRequest(uri, postData);
        req.setTid(tid);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                req.addHttpHeader(entry.getKey(), entry.getValue());
            }
        }
        if (cookies != null) {
            for (HttpCookie cookie : cookies) {
                req.addHttpCookie(cookie);
            }
        }
        if (postData!=null && postData.length>0)
            req.setPostData(postData);

        return req.getResponse();
    }

    /**
     * http get helper
     *
     * @param uri
     * @return
     */
    public static HttpResponse httpGetResponse(String uri) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_GET, null, null, null, null);
    }

    /**
     * http get helper
     *
     * @param uri
     * @param tid
     * @return
     */
    public static HttpResponse httpGetResponse(String uri, String tid) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_GET, null, null, null, tid);
    }

    /**
     * http get helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @return
     */
    public static HttpResponse httpGetResponse(String uri, Map<String, String> headers, List<HttpCookie> cookies) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_GET, headers, cookies, null, null);
    }

    /**
     * http get helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @param tid
     * @return
     */
    public static HttpResponse httpGetResponse(String uri, Map<String, String> headers, List<HttpCookie> cookies, String tid) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_GET, headers, cookies, null, tid);
    }

    /**
     * http post helper
     *
     * @param uri
     * @param postData
     * @return
     */
    public static HttpResponse httpPostResponse(String uri, byte[] postData) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_POST, null, null, postData, null);
    }

    /**
     * http post helper
     *
     * @param uri
     * @param postData
     * @param tid
     * @return
     */
    public static HttpResponse httpPostResponse(String uri, byte[] postData, String tid) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_POST, null, null, postData, tid);
    }

    /**
     * http post helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @param postData
     * @return
     */
    public static HttpResponse httpPostResponse(String uri, Map<String, String> headers, List<HttpCookie> cookies, byte[] postData) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_POST, headers, cookies, postData, null);
    }

    /**
     * @param uri
     * @param headers
     * @param cookies
     * @param postData
     * @param tid
     * @return
     */
    public static HttpResponse httpPostResponse(String uri,
                                                   Map<String, String> headers,
                                                   List<HttpCookie> cookies,
                                                   byte[] postData,
                                                   String tid) {
        return HttpHelper.execute(uri, HttpConstants.HTTP_METHOD_POST, headers,
                cookies, postData, tid);
    }
    
    public static void main(String[] args) {
    	String s = HttpHelper.httpGet("http://www.baidu.com/");
    	System.out.println(s);
    }
}
