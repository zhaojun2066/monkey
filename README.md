# monkey


    spider


## monkey-core
    爬虫核心:httpclient\jsoup\proxy\process\store


## monkey-core-demo
        Site site = Site.me().setDomain("blog.csdn.net")
        .setCharset("utf-8");
        MonkeySpider.me().setDownloader(new HttpClientDownloader(10,2))
        .setAnalyzer(new
        DemoAnalyzer()).setTransfers(new ConsoleTransfers()
        ).setSite(site).addRequest(Request.me().setUrl("http://blog.csdn.net/column/details/jsoup.html"))
        .spider();