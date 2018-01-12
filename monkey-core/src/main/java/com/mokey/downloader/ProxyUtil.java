package com.mokey.downloader;

import com.mokey.common.CommonUtil;
import com.mokey.common.ProxyInfo;
import org.apache.http.HttpHost;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 上午10:26
 **/
public class ProxyUtil {
    private static BasicAuthCache authCache = new BasicAuthCache();
    private static List<HttpHost> proxys = new ArrayList<HttpHost>();


    private static void initProxy(List<ProxyInfo>  proxyInfoList) throws MalformedChallengeException {
       if (!CommonUtil.isEmpty(proxyInfoList)){
           for (ProxyInfo proxyInfo : proxyInfoList){
               HttpHost proxy = new HttpHost(proxyInfo.getHost(), proxyInfo.getPort(),proxyInfo.getScheme());
               if (authCache.get(proxy)==null){
                   BasicScheme proxyAuth = new BasicScheme();
                   // Make client believe the challenge came form a proxy
                   proxyAuth.processChallenge(new BasicHeader(AUTH.PROXY_AUTH, "BASIC realm=default"));
                   authCache.put(proxy, proxyAuth);
               }

           }
       }
    }



    public static void setAuthProxy(HttpClientContext clientContext,ProxyInfo proxyInfo) throws MalformedChallengeException {
        HttpHost proxy = new HttpHost(proxyInfo.getHost(), proxyInfo.getPort(),proxyInfo.getScheme());

        BasicScheme proxyAuth = new BasicScheme();
        // Make client believe the challenge came form a proxy
        proxyAuth.processChallenge(new BasicHeader(AUTH.PROXY_AUTH, "BASIC realm=default"));
        authCache.put(proxy, proxyAuth);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(proxy),
                new UsernamePasswordCredentials(proxyInfo.getUsername(), proxyInfo.getPassword()));

        clientContext.setAuthCache(authCache);
        clientContext.setCredentialsProvider(credsProvider);
    }

    public static void setProxy( RequestConfig.Builder configBuilder ,ProxyInfo proxyInfo){
        HttpHost proxy = new HttpHost(proxyInfo.getHost(), proxyInfo.getPort(),proxyInfo.getScheme());
        configBuilder.setProxy(proxy);
    }


    public void createBasicAuth(HttpClientBuilder httpClientBuilder ,ProxyInfo proxyInfo){
        // 创建HttpClientBuilder
        // 设置BasicAuth
        HttpHost proxy = new HttpHost(proxyInfo.getHost(), proxyInfo.getPort(),proxyInfo.getScheme());
        CredentialsProvider provider = new BasicCredentialsProvider();
        // Create the authentication scope
        AuthScope scope = new AuthScope(proxy);
        // Create credential pair，在此处填写用户名和密码
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(proxyInfo.getUsername(), proxyInfo.getPassword());
        // Inject the credentials
        provider.setCredentials(scope, credentials);
        // Set the default credentials provider
        httpClientBuilder.setDefaultCredentialsProvider(provider);
    }

}
