package com.monkey.jsoup;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @User: jufeng
 * @Date: 18-1-4
 * @Time: 下午4:45
 **/
public class HttpClientTest {


    public static void main(String [] args) throws Exception {
        get();
        try {
            createURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        processorResponse();
        entity();
        getTest();
        responseTest01();
    }

    private void proxyTest(){
        // 依次是目标请求地址，端口号,协议类型
        HttpHost target = new HttpHost("10.10.100.102:8080/mytest", 8080,
                "http");
        // 依次是代理地址，代理端口号，协议类型
        HttpHost proxy = new HttpHost("yourproxy", 8080, "http");
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

        // 请求地址
        HttpPost httpPost = new HttpPost("http://10.10.100.102:8080/mytest");
        httpPost.setConfig(config);

    }



    // 自定义route 链接数
    private void  test22(){
        PoolingHttpClientConnectionManager cm  = new PoolingHttpClientConnectionManager();
        // 将最大连接数增加到200
        cm.setMaxTotal(200);
        // 将每个路由基础的连接增加到20
        cm.setDefaultMaxPerRoute(20);
        //将目标主机的最大连接数增加到50
        HttpHost localhost = new HttpHost("www.yeetrack.com", 80);
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }
    public static void post(){
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post  = new HttpPost("http://www.baidu.com");
        List formparams = new ArrayList();
        formparams.add(new BasicNameValuePair("name","admin"));
        formparams.add(new BasicNameValuePair("pwd","132456"));

        UrlEncodedFormEntity urlEncodedFormEntity;
        HttpEntity responEntity=null;
        CloseableHttpResponse response = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(formparams,"utf-8");
            post.setEntity(urlEncodedFormEntity);
            System.out.println("executing request " + post.getURI());
             response = httpClient.execute(post);
             responEntity = response.getEntity();
            if (responEntity!=null){
                System.out.println("response->" + EntityUtils.toString(responEntity));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response!=null){
                try {
                    response.close();
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }
    }


    public static void get(){
        /*HttpClient httpClient= new DefaultHttpClient();
        HttpGet get = new HttpGet("http://www.baidu.com");
        System.out.println("excute request "+get.getURI());
        HttpResponse response =  null;
        try {
             response = httpClient.execute(get);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            System.out.println("--------------------------------------");
            // 打印响应状态
            System.out.println(response.getStatusLine());
            if (entity != null) {
                // 打印响应内容长度
                System.out.println("Response content length: " + entity.getContentLength());
                // 打印响应内容
                System.out.println("Response content: " + EntityUtils.toString(entity));
            }
            System.out.println("------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }*/

        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(1000)
                .setConnectTimeout(1000)
                .build();
        HttpGet httpget1 = new HttpGet("http://www.baidu.com");
        httpget1.setConfig(requestConfig);
        CloseableHttpResponse response1 = null;
        try {
            response1 = httpclient.execute(httpget1);
            HttpEntity entity1 = response1.getEntity();
            System.out.println(EntityUtils.toString(entity1));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //response1.close();
        }
    }


    /**
     * creat URI
     * @return
     * @throws URISyntaxException
     */
    private static void createURI() throws URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.google.com")
                .setPath("/search")
                .setParameter("q", "httpclient")
                .setParameter("btnG", "Google Search")
                .setParameter("aq", "f")
                .setParameter("oq", "")
                .build();

        HttpGet httpGet  = new HttpGet(uri);
        System.out.println("URI-> " + httpGet.getURI());
    }

    /**
     * uri @see
     * @param uri getURI()
     */
    private void createHttpGet(URI uri){
        HttpGet httpGet  = new HttpGet(uri);
    }

    private static void processorResponse(){
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        System.out.println("-------processorResponse---------");
        System.out.println(response.getProtocolVersion());//HTTP/1.1
        System.out.println(response.getStatusLine().getStatusCode());//200
        System.out.println(response.getStatusLine().getReasonPhrase());//OK
        System.out.println(response.getStatusLine().toString());//HTTP/1.1 200 OK


        System.out.println("-------");
        response.addHeader("Set-Cookie",
                "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie",
                "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
        Header h1 = response.getFirstHeader("Set-Cookie");
        System.out.println(h1);//Set-Cookie: c1=a; path=/; domain=localhost
        Header h2 = response.getLastHeader("Set-Cookie");
        System.out.println(h2);//Set-Cookie: c2=b; path="/", c3=c; domain="localhost"
        Header[] hs = response.getHeaders("Set-Cookie");
        System.out.println(hs.length);//2


        /*response.addHeader("Set-Cookie",
                "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie",
                "c2=b; path=\"/\", c3=c; domain=\"localhost\"");*/

        HeaderIterator it = response.headerIterator("Set-Cookie");

        System.out.println("--------------");
        while (it.hasNext()) {
            System.out.println("--->" + it.next());
        }

        System.out.println("--------------");

        HeaderElementIterator it1 = new BasicHeaderElementIterator(
                response.headerIterator("Set-Cookie"));

        while (it1.hasNext()) {
            HeaderElement elem = it1.nextElement();
            System.out.println(elem.getName() + " = " + elem.getValue());
            NameValuePair[] params = elem.getParameters();
            for (int i = 0; i < params.length; i++) {
                System.out.println(" " + params[i]);
            }
        }
    }


    /**
     * 为了读取内容，任何人都可以使用HttpEntity#getContent()返回java.io.InputStream，或者用HttpEntity#writeTo(OutputStream)提供给输出流。
     当实体通过一个收到的报文获取时，HttpEntity#getContentType()方法和HttpEntity#getContentLength()方法可以用来读取通用的元数据，如Content-Type和Content-Length头部信息（如果它们是可用的）。因为头部信息Content-Type可以包含对文本MIME类型的字符编码，比如text/plain或text/html，HttpEntity#getContentEncoding()方法用来读取这个信息。如果头部信息Content-Length不可用，那么就返回长度-1，而对于内容类型返回NULL。如果头部信息Content-Type是可用的，那么就会返回一个Header对象。
     */
    private static void entity(){

        System.out.println("--------entity------");
        StringEntity myEntity = new StringEntity("important message",
                ContentType.create("text/plain", "UTF-8"));
        System.out.println(myEntity.getContentType());
        System.out.println(myEntity.getContentLength());
        try {
            System.out.println(EntityUtils.toString(myEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(EntityUtils.toByteArray(myEntity).length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static  void getTest() throws IOException {
        System.out.println("--------getTest------");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    // do something useful
                    System.out.println(entity.getContentEncoding());
                    System.out.println(entity.getContentLength());
                    System.out.println(entity.getContentType());
                } finally {
                    instream.close();
                }
            }
        } finally {
            response.close();
        }
    }

    /**
     * EntityUtils is not recommend
     * not at you know response length
     * --------------------
     * HttpEntity#getContent()和HttpEntity#writeTo(OutputStream) is recommend
     */
    private void getTest1() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost/");
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                } else {
                    // Stream content out
                }
            }
        } finally {
            response.close();
        }
    }

    private void test(){
        /*CloseableHttpResponse response ;//
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            entity = new BufferedHttpEntity(entity);
        }*/
    }

    private void chunkSend(){
        StringEntity entity = new StringEntity("important message",
                ContentType.create("plain/text", Consts.UTF_8));
        entity.setChunked(true);
        HttpPost httppost = new HttpPost("http://localhost/acrtion.do");
        httppost.setEntity(entity);
    }

    private static void responseTest01(){

        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        System.out.println("-----------responseTest01------------");
        System.out.println(response.getProtocolVersion());//HTTP/1.1
        System.out.println(response.getStatusLine().getStatusCode());//200
        System.out.println(response.getStatusLine().getReasonPhrase());//OK
        System.out.println(response.getStatusLine().toString());//HTTP/1.1 200 OK
    }


    /**
     * processor response Hanlder
     */
    private static void responseTest02(){
       /* CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost/json");

        ResponseHandler<MyJsonObject> rh = new ResponseHandler<MyJsonObject>() {

            @Override
            public JsonObject handleResponse(
                    final HttpResponse response) throws IOException {
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(
                            statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }
                if (entity == null) {
                    throw new ClientProtocolException("Response contains no content");
                }
                Gson gson = new GsonBuilder().create();
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                Reader reader = new InputStreamReader(entity.getContent(), charset);
                return gson.fromJson(reader, MyJsonObject.class);
            }
        };
        MyJsonObject myjson = client.execute(httpget, rh);*/
    }

    private static void httpclienttest01(){
        //可以这样 custom and set customer parameter
        /*CloseableHttpClient httpclient = HttpClients.custom()
                .setKeepAliveStrategy(keepAliveStrat)
                .build();*/
        CloseableHttpClient httpclient = HttpClients.custom().build();

        //HttpClient 已经是线程安全了,所以保证一个程序内一个实例就可以了,不使用了 记得关闭

       /* CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
    <...>
        } finally {
            httpclient.close();
        }*/
    }


    /**
     *  Interceptor
     * @throws IOException
     */
    private void InterceptorTest() throws IOException {

        CloseableHttpClient httpclient = HttpClients.custom()
                .addInterceptorLast(new HttpRequestInterceptor() {

                    public void process(
                            final HttpRequest request,
                            final HttpContext context) throws HttpException, IOException {
                        AtomicInteger count = (AtomicInteger) context.getAttribute("count");
                        request.addHeader("Count", Integer.toString(count.getAndIncrement()));
                    }

                })
                .build();

        AtomicInteger count = new AtomicInteger(1);
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAttribute("count", count);

        HttpGet httpget = new HttpGet("http://localhost/");
        for (int i = 0; i < 10; i++) {
            CloseableHttpResponse response = httpclient.execute(httpget, localContext);
            try {
                HttpEntity entity = response.getEntity();
            } finally {
                response.close();
            }
        }
    }
}
