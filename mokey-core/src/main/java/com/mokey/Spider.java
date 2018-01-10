package com.mokey;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 下午9:40
 **/
public interface Spider {

    void spider();
    void onSuccess();

    void onError();
    void onComplete();
}
