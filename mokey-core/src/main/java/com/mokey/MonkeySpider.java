package com.mokey;

import com.mokey.analyzer.Analyzer;
import com.mokey.analyzer.DemoAnalyzer;
import com.mokey.common.*;
import com.mokey.downloader.Downloader;
import com.mokey.downloader.HttpClientDownloader;
import com.mokey.exception.MokeyException;
import com.mokey.scheduler.LinkedBlockingQueueScheduler;
import com.mokey.scheduler.Scheduler;
import com.mokey.transfers.ConsoleTransfers;
import com.mokey.transfers.Transfers;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @User: jufeng
 * @Date: 18-1-4
 * @Time: 下午11:36
 **/
public class MonkeySpider {

    public static void  main(String [] args){
        Site site = Site.me().setDomain("blog.csdn.net")
                                .setCharset("utf-8");
        MonkeySpider.me().setDownloader(new HttpClientDownloader(10,2))
                .setAnalyzer(new
                         DemoAnalyzer()).setTransfers(new ConsoleTransfers()
        ).setSite(site).addRequest(Request.me().setUrl("http://blog.csdn.net/column/details/jsoup.html"))
                .spider();
    }

    private Downloader downloader;
    private Analyzer analyzer;
    private Transfers transfers;
    private Scheduler scheduler;
    private Site site;
    private ThreadPoolExecutor spiderPool;

    public static MonkeySpider me(){
        return new MonkeySpider();
    }


    public MonkeySpider setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public MonkeySpider setSite(Site site) {
        this.site = site;
        return this;
    }

    public MonkeySpider setSpiderPool(ThreadPoolExecutor spiderPool) {
        this.spiderPool = spiderPool;
        return this;
    }

    public MonkeySpider setDownloader(Downloader downloader){
        this.downloader = downloader;
        return this;
    }

    public MonkeySpider setAnalyzer(Analyzer analyzer){
        this.analyzer = analyzer;
        return this;
    }

    public MonkeySpider setTransfers(Transfers transfers){
        this.transfers = transfers;
        return this;
    }

    private void check(){
        if (site==null){
            throw  new MokeyException("Site can not be null.");
        }
        if (downloader==null){
            downloader = new HttpClientDownloader();
        }
        if (analyzer==null){
            throw  new MokeyException("Analyzer can not be null.");
        }

        if (transfers==null){
            transfers = new ConsoleTransfers();
        }

        if (spiderPool==null){
            spiderPool = new ThreadPoolExecutor(2,2,60, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(2));
        }
        if (scheduler==null){
            scheduler = new LinkedBlockingQueueScheduler();
        }
    }



    public void spider(){
        check();
        while (true){
            Request request = scheduler.get();
            if (request!=null){
                spiderPool.submit(new SpiderService(request));
            }
        }
    }


    public MonkeySpider addRequest(Request request){
       scheduler.put(request);
       return this;
    }

    public MonkeySpider addRequest(List<Request> requestList){
        if (!CommonUtil.isEmpty(requestList)){
            for (Request request: requestList){
                scheduler.put(request);
            }
        }

        return this;
    }



    class SpiderService implements Runnable{
        private Request request;

        public SpiderService(Request request) {
            this.request = request;
        }

        @Override
        public void run() {
            Page page = downloader.download(request,site);
            if (page.isDownloadSuccess()){
                analyzer.analyzer(page);
                TransferData transferData = new TransferData();
                transferData.setResultItems(page.getResultItems());
                transferData.setUrl(page.getRequest().getUrl());
                transfers.transfer(transferData);
            }else {
                //if fail then re submit to spider
                System.out.println("download failed...........");
            }
        }
    }
}
