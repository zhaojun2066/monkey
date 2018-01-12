package com.mokey.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 上午12:49
 **/
public class Request {

    private String url;
    private String method;


   private List<Request> nextRequestList = new ArrayList<Request>();


    private Map<String, String> cookies = new HashMap<String, String>();

    private Map<String, String> headers = new HashMap<String, String>();


    private String charset;

    public static Request me(){
        return new Request();
    }


    public String getUrl() {
        return url;
    }

    public Request setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Request setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Request setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Request setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public Request addCookie(String name, String value) {
        cookies.put(name, value);
        return this;
    }

    public Request addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public List<Request> getNextRequestList() {
        return nextRequestList;
    }

    public Request addNextRequestList(List<Request> nextRequestList) {
        this.nextRequestList = nextRequestList;
        return this;
    }

    public Request addNextRequest(Request nextRequest) {
        this.nextRequestList.add(nextRequest);
        return this;
    }
}
