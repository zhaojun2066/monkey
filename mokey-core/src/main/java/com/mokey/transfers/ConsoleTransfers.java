package com.mokey.transfers;

import com.mokey.common.TransferData;

/**
 * @User: jufeng
 * @Date: 18-1-10
 * @Time: 下午10:37
 **/
public class ConsoleTransfers implements Transfers {
    @Override
    public void transfer(TransferData transferData) {
        System.out.println("url-> " +transferData.getUrl()+"ResultItems,->"+transferData.getResultItems());
    }
}
