package com.mokey.scheduler;

import com.mokey.common.Request;

/**
 * @User: jufeng
 * @Date: 18-1-11
 * @Time: 下午2:12
 **/
public interface Scheduler {



    boolean put(Request request);

    Request get();
}
