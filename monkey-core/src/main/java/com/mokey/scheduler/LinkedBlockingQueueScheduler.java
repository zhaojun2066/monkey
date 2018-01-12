package com.mokey.scheduler;

import com.mokey.common.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @User: jufeng
 * @Date: 18-1-11
 * @Time: 下午2:16
 **/
public class LinkedBlockingQueueScheduler implements Scheduler {

    private BlockingQueue<Request> requests = new LinkedBlockingDeque<Request>();


    @Override
    public boolean put(Request request) {
        //todo: bloom filer url | hashset
        return requests.add(request);
    }

    @Override
    public Request get() {
        try {
            return requests.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
