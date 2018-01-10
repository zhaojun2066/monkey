package com.mokey.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 下午10:22
 **/
public class Url {
    private String url;

    private Map<String, String> headers = new HashMap<String, String>();

    private Map<String ,String> cookies = new HashMap();

    private String charset;


    public  static  Url me(String url){
        return new Url(url);
    }

    public  Url(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Url setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Url setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Url setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Url setCharset(String charset) {
        this.charset = charset;
        return this;
    }
}
