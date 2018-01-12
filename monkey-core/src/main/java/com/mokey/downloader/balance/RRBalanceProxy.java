package com.mokey.downloader.balance;

import com.mokey.common.ProxyInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 下午2:37
 **/
public class RRBalanceProxy implements BalanceProxy {

    private AtomicInteger number = new AtomicInteger(0);


    private List<ProxyInfo> list;

    private int size;

    public RRBalanceProxy(List<ProxyInfo> list) {
        this.list = list;
        size = list.size();
    }

    public ProxyInfo next(){
        int index =  Math.abs(number.getAndIncrement()) % size;
        return list.get(index);
    }
}
