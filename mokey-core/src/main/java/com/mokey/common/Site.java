package com.mokey.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @User: jufeng
 * @Date: 18-1-4
 * @Time: 下午3:24
 * spider site config
 **/
public class Site {

    private String domain;

    private String userAgent;
    private Map<String ,String> defaultCookies = new HashMap();

    private String charset = "utf-8";

    private int retryTimes = 0; // retry downloader times
    private int retrySleepTime = 2000;// retry download time interval
    private int timeout = 8000;// 链接socket timeout
    private Map<String, String> defaultHeaders = new HashMap<String, String>();
    private Integer acceptStatCode = 200;

    private ThreadPoolExecutor spiderPool = new ThreadPoolExecutor(2,2,60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(2));

    private List<Url> urls = new ArrayList<Url>();


    private boolean disableCookieManagement = false;

    private boolean useGzip = true;

    public static Site me() {
        return new Site();
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    public ThreadPoolExecutor getSpiderPool() {
        return spiderPool;
    }

    public Site setSpiderPool(ThreadPoolExecutor spiderPool) {
        this.spiderPool = spiderPool;
        return this;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public Site addUrls(List<Url> urls) {
        this.urls.addAll(urls);
        return this;
    }

    public Site addUrl(Url url){
        this.urls.add(url);
        return this;
    }

    public Site setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
        return this;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public Map<String, String> getDefaultCookies() {
        return defaultCookies;
    }

    public Integer getAcceptStatCode() {
        return acceptStatCode;
    }

    public boolean isDisableCookieManagement() {
        return disableCookieManagement;
    }

    public Site setDisableCookieManagement(boolean disableCookieManagement) {
        this.disableCookieManagement = disableCookieManagement;
        return this;
    }

    public Site setAcceptStatCode(Integer acceptStatCode) {
        this.acceptStatCode = acceptStatCode;
        return this;
    }

    public Site addDefaultCookie(String name, String value){
        this.defaultCookies.put(name, value);
        return this;
    }

    public Site addDefaultHeaders(String name ,String value){
        this.defaultHeaders.put(name, value);
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public Site setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Site setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public int getRetrySleepTime() {
        return retrySleepTime;
    }

    public Site setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public Site setTimeout(int timeout) {
        this.timeout = timeout;
        return  this;
    }
}
