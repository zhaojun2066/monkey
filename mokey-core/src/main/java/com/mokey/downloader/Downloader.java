package com.mokey.downloader;

import com.mokey.common.Page;
import com.mokey.common.Request;
import com.mokey.common.Site;
import com.mokey.downloader.balance.BalanceProxy;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 上午12:48
 **/
public interface Downloader {

     Page download(Request request,Site site);

     void balanceProxy(BalanceProxy balanceProxy);


     void close(String domain);
}
