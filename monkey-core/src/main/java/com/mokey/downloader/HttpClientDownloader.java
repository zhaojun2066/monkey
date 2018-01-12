package com.mokey.downloader;

import com.mokey.common.*;
import com.mokey.downloader.balance.BalanceProxy;
import com.mokey.exception.MokeyException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.commons.io.IOUtils;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @User: jufeng
 * @Date: 18-1-4
 * @Time: 下午11:45
 **/
public class HttpClientDownloader implements Downloader {

    private HttpConnectionPoolManager connectionPoolManager;

    //key : domain ,value: CloseableHttpClient
    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

    private BalanceProxy balanceProxy;

    public HttpClientDownloader(int maxConnections,int maxDefaultPerRoute) {
        connectionPoolManager = new HttpConnectionPoolManager(maxConnections,maxDefaultPerRoute);
    }

    public HttpClientDownloader() {
        this(200,5);
    }

    @Override
    public void balanceProxy(BalanceProxy balanceProxy){
        this.balanceProxy = balanceProxy;
    }

    @Override
    public void close(String domain) {
        CloseableHttpClient closeableHttpClient = httpClients.get(domain);
        if (closeableHttpClient!=null){
            try {
                closeableHttpClient.close();
                closeableHttpClient = null;
                httpClients.remove(domain);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    @Override
    public Page download(Request request,Site site) {
        if (request==null){
            throw new MokeyException("Request can not be null ");
        }
        CloseableHttpClient  client = getClient(site);
        CloseableHttpResponse httpResponse = null;
        Page page = Page.me().setDownloadSuccess(false);
        try {
            httpResponse = client.execute(getHttpUriRequestForGet(request, site),getHttpClientContext(request,site));
            //processor response
            String charset = CommonUtil.isNullString(request.getCharset()) ? site.getCharset() : request.getCharset();
            page = handlerResponse(request,httpResponse,charset,site.getDomain());
            return page;

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpResponse!=null){
                EntityUtils.consumeQuietly(httpResponse.getEntity());
               // client.close();
                //remove from client map
            }
        }
        return page;
    }

    private CloseableHttpClient getClient(Site site){
        if (site == null){
            throw new MokeyException("Site can not be null ");
        }
        String domain = site.getDomain();
        CloseableHttpClient closeableHttpClient = httpClients.get(domain);
        if (closeableHttpClient!=null){
            return closeableHttpClient;
        }
        closeableHttpClient = connectionPoolManager.getHttpClientForSite(site);
        httpClients.put(domain,closeableHttpClient);
        return closeableHttpClient;
    }

    private HttpUriRequest getHttpUriRequestForGet(Request request,Site site){
        RequestBuilder requestBuilder = RequestBuilder.get().setUri(request.getUrl());
        Map<String,String> headers = request.getHeaders();
        if (!CommonUtil.isEmpty(headers)) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        if (site != null) {
            requestConfigBuilder.setConnectionRequestTimeout(site.getTimeout())
                    .setSocketTimeout(site.getTimeout())
                    .setConnectTimeout(site.getTimeout())
                    .setCookieSpec(CookieSpecs.STANDARD);
        }

        if (balanceProxy != null) {
            ProxyInfo proxy = balanceProxy.next();
            requestConfigBuilder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        HttpUriRequest httpUriRequest = requestBuilder.build();
        if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
                httpUriRequest.addHeader(header.getKey(), header.getValue());
            }
        }
        return httpUriRequest;
    }


    private HttpClientContext getHttpClientContext(Request request, Site site) {
        HttpClientContext httpContext = new HttpClientContext();
        if (balanceProxy != null) {
            ProxyInfo proxy = balanceProxy.next();
            if (proxy != null && proxy.getUsername() != null) {
                AuthState authState = new AuthState();
                authState.update(new BasicScheme(ChallengeState.PROXY), new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword()));
                httpContext.setAttribute(HttpClientContext.PROXY_AUTH_STATE, authState);
            }
        }

        if (request.getCookies() != null && !request.getCookies().isEmpty()) {
            CookieStore cookieStore = new BasicCookieStore();
            for (Map.Entry<String, String> cookieEntry : request.getCookies().entrySet()) {
                BasicClientCookie cookie1 = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie1.setDomain(site.getDomain());
                cookieStore.addCookie(cookie1);
            }
            httpContext.setCookieStore(cookieStore);
        }
        return httpContext;
    }

    private Page handlerResponse(Request request,HttpResponse response,String charset,String domain) throws IOException {
        HttpEntity entity =  response.getEntity();
        String content = IOUtils.toString(entity.getContent(),charset);
        Page page = Page.me()
                .setDownloadSuccess(true)
                .setRawText(content)
                .setRequest(request)
                .setStatusCode(response.getStatusLine().getStatusCode());
        if (!CommonUtil.isNullString(content)){
            page.setDocument(Jsoup.parse(content,domain));
        }
        return page;
    }


}
