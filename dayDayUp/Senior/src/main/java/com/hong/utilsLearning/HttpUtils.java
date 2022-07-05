package com.hong.utilsLearning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class HttpUtils {

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 20000;
    private static HttpRequestRetryHandler httpRequestRetryHandler;
    private static Registry<CookieSpecProvider> cookieSpecProviderRegistry;
    private static ObjectMapper mapper = SerializeUtils.getGlobalObjectMapper();

    static {
        PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", trustAllHttpsCertificates()).build();

        cookieSpecProviderRegistry = RegistryBuilder.<CookieSpecProvider> create()
                .register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider(publicSuffixMatcher))
                .register(CookieSpecs.STANDARD, new RFC6265CookieSpecProvider(publicSuffixMatcher))
                .register("easy", context -> new EasySpec()).build();
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 设置连接池大小
        connMgr.setMaxTotal(200);
        connMgr.setDefaultMaxPerRoute(100);

        RequestConfig.Builder configBuilder = RequestConfig.custom().setCookieSpec("easy");
        // 设置连接超时
        configBuilder.setConnectTimeout(2000);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(1000);

        requestConfig = configBuilder.build();

        //设置重试机制
        httpRequestRetryHandler = (exception, executionCount, context) -> {
            // 如果已经重试了1次，就放弃
            if (executionCount >= 2) {
                return false;
            }
            // 如果服务器丢掉了连接，那么就重试
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                return false;
            }
            if (exception instanceof InterruptedIOException) {// 超时不重试
                return false;
            }
            if (exception instanceof ConnectException) {// 找到host建立连接失败重试
                return true;
            }
            if (exception instanceof UnknownHostException) {// 目标服务器不可达
                return false;
            }
            if (exception instanceof SSLException) {// ssl握手异常
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                return true;
            }
            //flush数据时io异常，则重试
            return !clientContext.isRequestSent();
        };
    }

    /**
     * 信任所有证书
     */
    private static SSLConnectionSocketFactory trustAllHttpsCertificates() {
        SSLConnectionSocketFactory socketFactory = null;
        TrustManager[] trustAllCerts = { new Mitm() };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, null);
            socketFactory = new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE);
            //HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error(e.toString());
        }
        return socketFactory;
    }

    /**
     * Mitm
     */
    static class Mitm implements TrustManager, X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            //don't check
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            //don't check
        }
    }

    /**
     * 一些cookie里面的Expires格式不规范，在这里去掉，防止报错
     */
    static class EasySpec extends DefaultCookieSpec {
        @Override
        public List<Cookie> parse(Header header, CookieOrigin cookieOrigin)
                throws MalformedCookieException {
            String value = header.getValue();
            String prefix = "Expires=";
            if (value.contains(prefix)) {
                String expires = value.substring(value.indexOf(prefix) + prefix.length());
                expires = expires.substring(0, expires.indexOf(";"));
                LocalDateTime dateDate = null;
                try {
                    dateDate = LocalDateTimeUtils.parse2LocalDateTime(expires,
                            "EEE, dd-MMM-yy HH:mm:ss z");
                } catch (DateTimeParseException e) { //时间格式转换错误
                    log.error("ocalDateTimeUtils.parse2LocalDateTime Expires date exception ", e);
                }
                if (dateDate == null) {
                    value = value.replaceAll(prefix + expires + ";", "");
                }
            }
            header = new BasicHeader(header.getName(), value);
            return super.parse(header, cookieOrigin);
        }
    }

    /**
     * 获取httpclient实例
     *
     * @return 从连接池中返回实例
     */
    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(connMgr)
                .setDefaultCookieSpecRegistry(cookieSpecProviderRegistry)
                .setDefaultRequestConfig(requestConfig).setRetryHandler(httpRequestRetryHandler)
                .build();
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url    url
     * @param params get请求的参数
     * @return 返回的数据
     */
    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, params, null);
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url          url
     * @param params       get请求的参数
     * @param headerParams 请求头设置
     * @return 返回结果
     */
    public static String doGet(String url, Map<String, Object> params,
                               Map<String, String> headerParams) {
        List<NameValuePair> pairList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach((k, v) -> pairList
                    .add(new BasicNameValuePair(k, v == null ? "" : v.toString())));
        }
        String result;
        CloseableHttpClient httpClient = getHttpClient();
        HttpResponse response = null;
        try {
            URIBuilder uri = new URIBuilder(url);
            uri.addParameters(pairList);
            uri.setCharset(Charset.forName("UTF-8"));
            HttpGet httpGet = new HttpGet(uri.build());
            log.info("请求参数：" + httpGet.getURI().toString());
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach(httpGet::setHeader);
            }
            response = httpClient.execute(httpGet);
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            String httpStr = EntityUtils.toString(entity, "UTF-8");
            log.info("响应参数：" + httpStr);
            result = httpStr;
        } catch (Exception e) {
            log.error("Failed to get data ", e);
            result = e.getMessage();
        } finally {
            closeInputStream(response);
        }
        log.info("doGet request is {}, the response is {}", url, result);
        return result;
    }

    /**
     * 发送 GET 请求（HTTP），带SSL认证
     *
     * @param url          url
     * @param params       get请求的参数
     * @param headerParams 请求头设置
     * @return 返回结果
     */
    public static String doGet(String url, Map<String, Object> params,
                               Map<String, String> headerParams, SSLContext sslContext) {
        List<NameValuePair> pairList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach((k, v) -> pairList
                    .add(new BasicNameValuePair(k, v == null ? "" : v.toString())));
        }
        String result;
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("https", sslsf).build();
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        // 设置连接池大小
        //        connMgr.setMaxTotal(200);
        //        connMgr.setDefaultMaxPerRoute(100);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connMgr)
                .setDefaultCookieSpecRegistry(cookieSpecProviderRegistry)
                .setDefaultRequestConfig(requestConfig).setRetryHandler(httpRequestRetryHandler)
                .setSSLSocketFactory(sslsf).build();
        HttpResponse response = null;
        try {
            URIBuilder uri = new URIBuilder(url);
            uri.addParameters(pairList);
            uri.setCharset(Charset.forName("UTF-8"));
            HttpGet httpGet = new HttpGet(uri.build());
            log.info("请求参数：" + httpGet.getURI().toString());
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach(httpGet::setHeader);
            }
            response = httpClient.execute(httpGet);
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            String httpStr = EntityUtils.toString(entity, "UTF-8");
            log.info("响应参数：" + httpStr);
            result = httpStr;
        } catch (Exception e) {
            log.error("Failed to get data ", e);
            result = e.getMessage();
        } finally {
            closeInputStream(response);
        }
        log.info("doGet request is {}, the response is {}", url, result);
        return result;
    }

    /**
     * GET请求，并且返回结果带上http的错误码，方便业务测判断是否出现http异常
     * @param url          url
     * @param params       get请求的参数
     * @param headerParams 请求头设置
     * @return 返回结果
     */
    public static HttpUtilsResult doGetResult(String url, Map<String, Object> params,
                                              Map<String, String> headerParams) {
        List<NameValuePair> pairList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach((k, v) -> pairList.add(new BasicNameValuePair(k, v.toString())));
        }
        HttpUtilsResult result = new HttpUtilsResult();
        CloseableHttpClient httpClient = getHttpClient();
        HttpResponse response = null;
        try {
            URIBuilder uri = new URIBuilder(url);
            uri.addParameters(pairList);
            uri.setCharset(Charset.forName("UTF-8"));
            HttpGet httpGet = new HttpGet(uri.build());
            log.info("请求参数：" + httpGet.getURI().toString());
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach(httpGet::setHeader);
            }
            response = httpClient.execute(httpGet);
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            result.setCode(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            String httpStr = EntityUtils.toString(entity, "UTF-8");
            log.info("响应参数：" + httpStr);
            result.setResult(httpStr);
        } catch (Exception e) {
            log.error("Failed to get data ", e);
            result.setCode(99999);
            result.setResult(e.getMessage());
        } finally {
            closeInputStream(response);
        }
        log.info("doGet request is {}, the response is {}", url, result.getResult());
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式，form表单
     *
     * @param apiUrl       API接口URL
     * @param params       参数map
     * @param headerParams 请求头设置
     * @return
     */
    public static String doPostForm(String apiUrl, Map<String, Object> params,
                                    Map<String, String> headerParams) {
        CloseableHttpClient httpClient = getHttpClient();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        if (!CollectionUtils.isEmpty(headerParams)) {
            headerParams.forEach(httpPost::setHeader);
        }
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(),
                        entry.getValue() == null ? "" : entry.getValue().toString());
                pairList.add(pair);
            }
            log.info("发送的请求URL是：" + apiUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            log.info("发送的请求URL是：" + httpPost.getEntity().toString());
            response = httpClient.execute(httpPost);
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
            log.info("响应参数：" + httpStr);
        } catch (IOException e) {
            e.printStackTrace();
            httpStr = e.getMessage();
            log.error("Failed to post data ", e);
        } finally {
            closeInputStream(response);
        }
        log.info("doPostForm request is {}, the response is {}", apiUrl, httpStr);
        return httpStr;
    }

    /**
     *
     * @param apiUrl
     * @param params
     * @param headerParams
     * @return
     */
    public static String doPostWithoutUTF8(String apiUrl, String params,
                                           Map<String, String> headerParams) {
        CloseableHttpClient httpClient = getHttpClient();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        if (!CollectionUtils.isEmpty(headerParams)) {
            headerParams.forEach(httpPost::setHeader);
        }
        CloseableHttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(params, Charset.forName("UTF-8"));
            // 发送Json格式的数据请求
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            log.info("发送的请求URL是：" + apiUrl);
            httpPost.setEntity(entity);
            log.info("发送的请求参数是：" + params);
            response = httpClient.execute(httpPost);
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            HttpEntity responseEntity = response.getEntity();
            httpStr = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (Exception e) {
            log.error("Failed to post data ", e);
        } finally {
            closeInputStream(response);
        }
        log.info("doPost request is {}, the response is {}", apiUrl, httpStr);
        return httpStr;
    }

    /**
     * post请求，返回String数据
     * @param apiUrl url
     * @param params 请求数据
     * @param headerParams head参数
     * @return 返回结果
     */
    public static String doPost(String apiUrl, String params, Map<String, String> headerParams) {
        CloseableHttpClient httpClient = getHttpClient();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        if (!CollectionUtils.isEmpty(headerParams)) {
            headerParams.forEach(httpPost::setHeader);
        }
        CloseableHttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(params, Charset.forName("UTF-8"));
            // 发送Json格式的数据请求
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            entity.setContentEncoding("UTF-8");
            log.info("发送的请求URL是：" + apiUrl);
            httpPost.setEntity(entity);
            log.info("发送的请求参数是：" + params);
            response = httpClient.execute(httpPost);
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            HttpEntity responseEntity = response.getEntity();
            httpStr = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (Exception e) {
            log.error("Failed to post data ", e);
        } finally {
            closeInputStream(response);
        }
        log.info("doPost request is {}, the response is {}", apiUrl, httpStr);
        return httpStr;
    }

    /**
     * 发送 Post 请求（HTTPS），带SSL认证
     *
     * @param apiUrl          url
     * @param params       get请求的参数
     * @param headerParams 请求头设置
     * @return 返回结果
     */
    public static String doPost(String apiUrl, String params, Map<String, String> headerParams,
                                SSLContext sslContext) {
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("https", sslsf).build();
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        // 设置连接池大小
        //        connMgr.setMaxTotal(200);
        //        connMgr.setDefaultMaxPerRoute(100);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connMgr)
                .setDefaultCookieSpecRegistry(cookieSpecProviderRegistry)
                .setDefaultRequestConfig(requestConfig).setRetryHandler(httpRequestRetryHandler)
                .setSSLSocketFactory(sslsf).build();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        if (!CollectionUtils.isEmpty(headerParams)) {
            headerParams.forEach(httpPost::setHeader);
        }
        CloseableHttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(params, Charset.forName("UTF-8"));
            // 发送Json格式的数据请求
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            entity.setContentEncoding("UTF-8");
            log.info("发送的请求URL是：" + apiUrl);
            httpPost.setEntity(entity);
            log.info("发送的请求参数是：" + params);
            response = httpClient.execute(httpPost);
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            HttpEntity responseEntity = response.getEntity();
            httpStr = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (Exception e) {
            log.error("Failed to post data ", e);
        } finally {
            closeInputStream(response);
        }
        log.info("doPost request is {}, the response is {}", apiUrl, httpStr);
        return httpStr;
    }

    /**
     * 和魔镜的获取token，不能设置ContentType，否则就会 415 不支持的媒体类型
     * @param apiUrl
     * @param params
     * @param headerParams
     * @return HttpUtilsResult
     */
    public static HttpUtilsResult doPostNoContentEncoding(String apiUrl, Object params,
                                                          Map<String, String> headerParams) {
        String paramsJson = toJson(params);
        CloseableHttpClient httpClient = getHttpClient();
        HttpUtilsResult result = new HttpUtilsResult();
        HttpPost httpPost = new HttpPost(apiUrl);
        if (!CollectionUtils.isEmpty(headerParams)) {
            headerParams.forEach(httpPost::setHeader);
        }
        CloseableHttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(paramsJson, Charset.forName("UTF-8"));
            // 发送Json格式的数据请求
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            //            entity.setContentEncoding("UTF-8");
            log.info("发送的请求URL是：" + apiUrl);
            httpPost.setEntity(entity);
            log.info("发送的请求参数是：" + paramsJson);
            response = httpClient.execute(httpPost);
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            result.setCode(response.getStatusLine().getStatusCode());
            HttpEntity responseEntity = response.getEntity();
            result.setResult(EntityUtils.toString(responseEntity, "UTF-8"));
        } catch (Exception e) {
            log.error("Failed to post data ", e);
            result.setCode(99999);
            result.setResult(e.getMessage());
        } finally {
            closeInputStream(response);
        }
        log.info("doPost request is {}, the response is {}", apiUrl, result.getResult());
        return result;
    }

    /**
     * post请求，并且返回结果带上http的错误码，方便业务测判断是否出现http异常
     * @param apiUrl url
     * @param params 请求数据，实体或者map都可以
     * @param headerParams head参数
     * @return 返回结果
     */
    public static HttpUtilsResult doPostResult(String apiUrl, Object params,
                                               Map<String, String> headerParams) {
        return doPostResult(apiUrl, toJson(params), headerParams);
    }

    /**
     * post请求，并且返回结果带上http的错误码，方便业务测判断是否出现http异常
     * @param apiUrl url
     * @param params 请求数据，已经是json的字符串
     * @param headerParams head参数
     * @return 返回结果
     */
    public static HttpUtilsResult doPostResult(String apiUrl, String params,
                                               Map<String, String> headerParams) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpUtilsResult result = new HttpUtilsResult();
        HttpPost httpPost = new HttpPost(apiUrl);
        if (!CollectionUtils.isEmpty(headerParams)) {
            headerParams.forEach(httpPost::setHeader);
        }
        CloseableHttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(params, Charset.forName("UTF-8"));
            // 发送Json格式的数据请求
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            entity.setContentEncoding("UTF-8");
            log.info("发送的请求URL是：" + apiUrl);
            httpPost.setEntity(entity);
            log.info("发送的请求参数是：" + params);
            response = httpClient.execute(httpPost);
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            result.setCode(response.getStatusLine().getStatusCode());
            HttpEntity responseEntity = response.getEntity();
            result.setResult(EntityUtils.toString(responseEntity, "UTF-8"));
        } catch (Exception e) {
            log.error("Failed to post data ", e);
            result.setCode(99999);
            result.setResult(e.getMessage());
        } finally {
            closeInputStream(response);
        }
        log.info("doPost request is {}, the response is {}", apiUrl, result.getResult());
        return result;
    }

    /**
     * Post请求
     *
     * @param apiUrl
     * @param params
     * @param headerParams
     * @return
     */
    public static String doPost(String apiUrl, Object params, Map<String, String> headerParams) {
        return doPost(apiUrl, toJson(params), headerParams);
    }

    /**
     * Post请求Json格式
     *
     * @param apiUrl url
     * @param params 参数map
     * @return 返回结果
     */
    public static String doPost(String apiUrl, Object params) {
        return doPost(apiUrl, params, null);
    }

    /**
     * @param apiUrl
     * @param paramsMap
     * @param fileMap
     * @return
     */
    public static String doPostFile(String apiUrl, Map<String, Object> paramsMap,
                                    Map<String, File> fileMap, String mimeType) {
        CloseableHttpClient httpClient = getHttpClient();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        log.info("发送的请求URL是：" + apiUrl);
        String boundary = "-----------------" + System.currentTimeMillis();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName("UTF-8"));//设置请求的编码格式
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
        builder.setBoundary(boundary);
        if (!CollectionUtils.isEmpty(fileMap)) {
            fileMap.forEach(builder::addBinaryBody);
        }
        ContentType contentType = ContentType.create(mimeType, "UTF-8");
        paramsMap.forEach((k, v) -> builder.addTextBody(k, String.valueOf(v), contentType));

        try {
            HttpEntity entity = builder.build();// 生成 HTTP POST 实体
            httpPost.setEntity(entity);//设置请求参数

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            httpStr = EntityUtils.toString(responseEntity, "UTF-8");
            log.info("响应体 ：" + httpStr);
        } catch (IOException e) {
            log.error("请求异常", e);
            httpStr = e.getMessage();
        }
        log.info("doPost request is {}, the response is {}", apiUrl, httpStr);
        return httpStr;
    }

    /**
     * @param apiUrl
     * @param paramsMap
     * @param fileMap
     * @return
     */
    public static String doPostFile(String apiUrl, Map<String, Object> paramsMap,
                                    Map<String, File> fileMap) {
        return doPostFile(apiUrl, paramsMap, fileMap, "application/x-www-form-urlencoded");
    }

    /**
     * 将二进制文件提交至文件服务器
     * @param apiUrl 上传文件url
     * @param file 文件对象
     * @param headerParams 请求头
     * @return result 上传结果
     */
    public static String doPostBinaryFile(String apiUrl, File file,
                                          Map<String, String> headerParams) {
        CloseableHttpClient httpclient = getHttpClient();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(apiUrl);
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach(httpPost::setHeader);
            }
            log.info("发送的请求URL是：" + apiUrl);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create()
                    .setCharset(Charset.forName("UTF-8")).setMode(HttpMultipartMode.RFC6532);
            mEntityBuilder.addBinaryBody("file", file);
            httpPost.setEntity(mEntityBuilder.build());
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                result = EntityUtils.toString(responseEntity, "UTF-8");
                log.info("响应体 ：" + result);
                closeInputStream(response);
            }
        } catch (Exception e) {
            log.error("请求异常", e);
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 将文本文件提交至文件服务器
     * @param apiUrl 上传文件url
     * @param file 文件对象
     * @param headerParams 请求头
     * @return result 上传结果
     */
    public static String doPostTextFile(String apiUrl, File file,
                                        Map<String, String> headerParams) {
        CloseableHttpClient httpclient = getHttpClient();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(apiUrl);
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach(httpPost::setHeader);
            }
            log.info("发送的请求URL是：" + apiUrl);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create()
                    .setCharset(Charset.forName("UTF-8")).setMode(HttpMultipartMode.RFC6532);
            mEntityBuilder.addPart("file", new FileBody(file, ContentType.create("text/plain")));
            httpPost.setEntity(mEntityBuilder.build());
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("响应状态码：" + response.getStatusLine().getStatusCode());
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                result = EntityUtils.toString(responseEntity, "UTF-8");
                log.info("响应体 ：" + result);
                closeInputStream(response);
            }
        } catch (Exception e) {
            log.error("请求异常", e);
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 关闭response读取流
     *
     * @param response 回应
     */
    private static void closeInputStream(HttpResponse response) {
        if (response != null) {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
    }

    private static String toJson(Object params) {
        String json = "";
        try {
            json = mapper.writeValueAsString(params);
            log.info("发送的请求body是 ：" + json);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return json;
    }

    /**
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseParams2Map(Object params) {
        if (params instanceof Map) {
            return (Map<String, Object>) params;
        }
        try {
            return mapper.readValue(toJson(params), new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyMap();
    }

    /**
     * @param params
     * @return
     */
    public static String parse2UrlParam(Object params) {
        return parse2UrlParam(parseParams2Map(params));
    }

    /**
     * @param params
     * @return
     */
    public static String parse2UrlParam(Map<String, Object> params) {
        return parse2UrlParam(params, false);
    }

    /**
     * 把Map所有元素按key排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params
     * @param includeNull
     * @return
     */
    public static String parse2UrlParam(Map<String, Object> params, boolean includeNull) {
        String paramStr = params.entrySet().stream()
                .filter(e -> includeNull || e.getValue() != null).sorted(Map.Entry.comparingByKey())
                .map(e -> String.join("=", e.getKey(), String.valueOf(e.getValue())))
                .collect(Collectors.joining("&"));
        log.info("Parameters are joined: {}", paramStr);
        return paramStr;
    }
}
