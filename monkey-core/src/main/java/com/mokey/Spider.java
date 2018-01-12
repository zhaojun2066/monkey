package com.mokey;

import com.mokey.common.Page;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 下午9:40
 **/
public interface Spider {

    void spider();
    void onSuccess(Page page);

    void onError(Page page);

    void shutdownNow();

    void stop();
}
