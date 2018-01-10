package com.mokey.common;

/**
 * @User: jufeng
 * @Date: 18-1-10
 * @Time: 下午4:23
 **/
public class TransferData {

    private ResultItems resultItems = new ResultItems();

    private String url;


    public ResultItems getResultItems() {
        return resultItems;
    }

    public TransferData setResultItems(ResultItems resultItems) {
        this.resultItems = resultItems;
        return  this;
    }

    public String getUrl() {
        return url;
    }

    public TransferData setUrl(String url) {
        this.url = url;
        return this;
    }


}
