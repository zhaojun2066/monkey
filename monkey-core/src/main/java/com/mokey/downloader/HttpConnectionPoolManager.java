package com.mokey.downloader;


import com.mokey.common.CommonUtil;
import com.mokey.common.ProxyInfo;
import com.mokey.common.Site;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @User: jufeng
 * @Date: 18-1-4
 * @Time: 下午11:39
 **/
public class HttpConnectionPoolManager {

    private PoolingHttpClientConnectionManager connectionManager;

    public  HttpConnectionPoolManager(int maxConnections,int maxDefaultPerRoute){
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(maxDefaultPerRoute);
        connectionManager.setMaxTotal(maxConnections);
      //  connectionManager.setConnectionConfig();
      //  connectionManager.setDefaultSocketConfig();
    }

    /**
     * commons CloseableHttpClient for all site
     * @return
     */
    public CloseableHttpClient getCommonHttpClient(){
        HttpClientBuilder builder =  HttpClients.custom();
        builder.setConnectionManager(connectionManager);
        builder.setRedirectStrategy(new CustomRedirectStrategy());
        return builder.build();
    }

    /**
     * get 啊a CloseableHttpClient for one site
     * @param site
     * @return
     */
    public CloseableHttpClient getHttpClientForSite(Site site){
        HttpClientBuilder builder =  HttpClients.custom();
        builder.setConnectionManager(connectionManager);
        String userAgent = site.getUserAgent();
        if (CommonUtil.isNullString(userAgent)) {
            builder.setUserAgent("");
        } else {
            builder.setUserAgent(userAgent);
        }
        builder.setConnectionTimeToLive(20, TimeUnit.MINUTES);
        builder.setRedirectStrategy(new CustomRedirectStrategy());
        SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
        socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
        socketConfigBuilder.setSoTimeout(site.getTimeout());
        SocketConfig socketConfig = socketConfigBuilder.build();
        builder.setDefaultSocketConfig(socketConfig);
        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(),true));
        generateCookie(builder,site);
        isUserGzip(builder,site);
        return builder.build();
    }

    private void generateProxy(HttpClientBuilder builder,Site site){
        /*ProxyInfo proxy = site.getProxy();
        if (proxy!=null){
            String host = proxy.getHost();
            int port = proxy.getPort();
            String scheme = proxy.getScheme();
            if (!CommonUtil.isNullString(host) && port>0){
                HttpHost httpHost = new HttpHost(host, port, scheme);
                builder.setProxy(httpHost);
            }
        }*/


    }

    private void isUserGzip(HttpClientBuilder builder,Site site){
        if (site.isUseGzip()) {
            builder.addInterceptorFirst(new HttpRequestInterceptor() {

                public void process(
                        final HttpRequest request,
                        final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }
            });
        }
    }


    private void generateCookie(HttpClientBuilder httpClientBuilder, Site site) {
        if (site.isDisableCookieManagement()) {
            httpClientBuilder.disableCookieManagement();
            return;
        }
        CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> cookieEntry : site.getDefaultCookies().entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setDomain(site.getDomain());
            cookieStore.addCookie(cookie);
        }
        httpClientBuilder.setDefaultCookieStore(cookieStore);
    }

}
