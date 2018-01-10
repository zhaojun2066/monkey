package com.mokey.common;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * @User: jufeng
 * @Date: 18-1-4
 * @Time: 下午3:58
 **/
public class Page {
    private Request request;

    private ResultItems resultItems = new ResultItems();

    private String rawText;// source page text

    private Document document;// jsoup ducment

    private Map<String,String> cookies = new HashMap<String, String>();

    private int statusCode = 200;

    private boolean downloadSuccess = true;

    public boolean isDownloadSuccess() {
        return downloadSuccess;
    }

    public Page setDownloadSuccess(boolean downloadSuccess) {
        this.downloadSuccess = downloadSuccess;
        return this;
    }

    public static Page me(){
        return new Page();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Page setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public Page setRequest(Request request) {
        this.request = request;
        return this;
    }

    public String getRawText() {
        return rawText;
    }

    public Page setRawText(String rawText) {
        this.rawText = rawText;
        return this;
    }

    public Document getDocument() {
        return document;
    }

    public Page setDocument(Document document) {
        this.document = document;
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Page setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public ResultItems getResultItems() {
        return resultItems;
    }

    public Page setResultItems(ResultItems resultItems) {
        this.resultItems = resultItems;
        return this;
    }
}
