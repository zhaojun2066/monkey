package com.mokey;

import com.mokey.analyzer.Analyzer;
import com.mokey.analyzer.DemoAnalyzer;
import com.mokey.common.*;
import com.mokey.common.utils.UrlUtils;
import com.mokey.downloader.Downloader;
import com.mokey.downloader.HttpClientDownloader;
import com.mokey.exception.MokeyException;
import com.mokey.transfers.ConsoleTransfers;
import com.mokey.transfers.Transfers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @User: jufeng
 * @Date: 18-1-4
 * @Time: 下午11:36
 **/
public class MonkeySpider {

    public static void  main(String [] args){
        MonkeySpider.me().setDownloader(new HttpClientDownloader(10,2))
                .setAnalyzer(new
                         DemoAnalyzer()).setTransfers(new ConsoleTransfers()).spider(
                Site.me().setDomain("blog.csdn.net")
                        .addUrl(Url.me("http://blog.csdn.net/column/details/jsoup.html")
                        .setCharset("utf-8"))
        );
    }

    private Downloader downloader;
    private Analyzer analyzer;
    private Transfers transfers;
    private Map<String,ThreadPoolExecutor> tasks = new HashMap<String, ThreadPoolExecutor>();

    public static MonkeySpider me(){
        return new MonkeySpider();
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





    private void checkWorkPool(Site site){
        if (!tasks.containsKey(site.getDomain())){
            ThreadPoolExecutor pool = site.getSpiderPool();
            tasks.put(site.getDomain(),pool);
        }
    }

    private void check(){
        if (downloader==null){
            downloader = new HttpClientDownloader();
        }
        if (analyzer==null){
            throw  new MokeyException("Analyzer can not be null.");
        }

        if (transfers==null){
            transfers = new ConsoleTransfers();
        }
    }

    public void spider(Site site){
        //todo: scheduler
        check();
        checkWorkPool(site);

        List<Url> urls = site.getUrls();
        if (!CommonUtil.isEmpty(urls)){
            for (Url url : urls){
                final Request request = Request.me()
                        .setCharset(url.getCharset())
                        .setUrl(url.getUrl())
                        .setHeaders(url.getHeaders())
                        .setCookies(url.getCookies());
                ThreadPoolExecutor service = tasks.get(site.getDomain());
                if (service==null){
                    tasks.put(site.getDomain(),site.getSpiderPool());
                }
                service.submit(new SpiderService(request,site));
            }
        }
    }

    public void spider(List<Site> siteList ){
        if (!CommonUtil.isEmpty(siteList)){
            for (Site site: siteList){
                spider(site);
            }
        }else {
            throw new MokeyException("siteList can not be empty");
        }
    }



    public void spider(Url url){
        String domain = UrlUtils.getDomain(url.getUrl());
        Site site = Site.me().addUrl(url).setDomain(domain);
        spider(site);
    }



    class SpiderService implements Runnable{
        private Request request;
        private Site site;

        public SpiderService(Request request, Site site) {
            this.request = request;
            this.site = site;
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
