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
        // ???????????????
        connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // ?????????????????????
        connMgr.setMaxTotal(200);
        connMgr.setDefaultMaxPerRoute(100);

        RequestConfig.Builder configBuilder = RequestConfig.custom().setCookieSpec("easy");
        // ??????????????????
        configBuilder.setConnectTimeout(2000);
        // ??????????????????
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // ?????????????????????????????????????????????
        configBuilder.setConnectionRequestTimeout(1000);

        requestConfig = configBuilder.build();

        //??????????????????
        httpRequestRetryHandler = (exception, executionCount, context) -> {
            // ?????????????????????1???????????????
            if (executionCount >= 2) {
                return false;
            }
            // ????????????????????????????????????????????????
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// ????????????SSL????????????
                return false;
            }
            if (exception instanceof InterruptedIOException) {// ???????????????
                return false;
            }
            if (exception instanceof ConnectException) {// ??????host????????????????????????
                return true;
            }
            if (exception instanceof UnknownHostException) {// ????????????????????????
                return false;
            }
            if (exception instanceof SSLException) {// ssl????????????
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // ??????????????????????????????????????????
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                return true;
            }
            //flush?????????io??????????????????
            return !clientContext.isRequestSent();
        };
    }

    /**
     * ??????????????????
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
     * ??????cookie?????????Expires????????????????????????????????????????????????
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
                } catch (DateTimeParseException e) { //????????????????????????
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
     * ??????httpclient??????
     *
     * @return ???????????????????????????
     */
    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(connMgr)
                .setDefaultCookieSpecRegistry(cookieSpecProviderRegistry)
                .setDefaultRequestConfig(requestConfig).setRetryHandler(httpRequestRetryHandler)
                .build();
    }

    /**
     * ?????? GET ?????????HTTP??????K-V??????
     *
     * @param url    url
     * @param params get???????????????
     * @return ???????????????
     */
    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, params, null);
    }

    /**
     * ?????? GET ?????????HTTP??????K-V??????
     *
     * @param url          url
     * @param params       get???????????????
     * @param headerParams ???????????????
     * @return ????????????
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
            log.info("???????????????" + httpGet.getURI().toString());
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach(httpGet::setHeader);
            }
            response = httpClient.execute(httpGet);
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            String httpStr = EntityUtils.toString(entity, "UTF-8");
            log.info("???????????????" + httpStr);
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
     * ?????? GET ?????????HTTP?????????SSL??????
     *
     * @param url          url
     * @param params       get???????????????
     * @param headerParams ???????????????
     * @return ????????????
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
        // ?????????????????????
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
            log.info("???????????????" + httpGet.getURI().toString());
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach(httpGet::setHeader);
            }
            response = httpClient.execute(httpGet);
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            String httpStr = EntityUtils.toString(entity, "UTF-8");
            log.info("???????????????" + httpStr);
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
     * GET?????????????????????????????????http????????????????????????????????????????????????http??????
     * @param url          url
     * @param params       get???????????????
     * @param headerParams ???????????????
     * @return ????????????
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
            log.info("???????????????" + httpGet.getURI().toString());
            if (!CollectionUtils.isEmpty(headerParams)) {
                headerParams.forEach(httpGet::setHeader);
            }
            response = httpClient.execute(httpGet);
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
            result.setCode(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            String httpStr = EntityUtils.toString(entity, "UTF-8");
            log.info("???????????????" + httpStr);
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
     * ?????? POST ?????????HTTP??????K-V?????????form??????
     *
     * @param apiUrl       API??????URL
     * @param params       ??????map
     * @param headerParams ???????????????
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
            log.info("???????????????URL??????" + apiUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            log.info("???????????????URL??????" + httpPost.getEntity().toString());
            response = httpClient.execute(httpPost);
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
            log.info("???????????????" + httpStr);
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
            // ??????Json?????????????????????
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            log.info("???????????????URL??????" + apiUrl);
            httpPost.setEntity(entity);
            log.info("???????????????????????????" + params);
            response = httpClient.execute(httpPost);
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
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
     * post???????????????String??????
     * @param apiUrl url
     * @param params ????????????
     * @param headerParams head??????
     * @return ????????????
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
            // ??????Json?????????????????????
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            entity.setContentEncoding("UTF-8");
            log.info("???????????????URL??????" + apiUrl);
            httpPost.setEntity(entity);
            log.info("???????????????????????????" + params);
            response = httpClient.execute(httpPost);
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
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
     * ?????? Post ?????????HTTPS?????????SSL??????
     *
     * @param apiUrl          url
     * @param params       get???????????????
     * @param headerParams ???????????????
     * @return ????????????
     */
    public static String doPost(String apiUrl, String params, Map<String, String> headerParams,
                                SSLContext sslContext) {
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("https", sslsf).build();
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        // ?????????????????????
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
            // ??????Json?????????????????????
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            entity.setContentEncoding("UTF-8");
            log.info("???????????????URL??????" + apiUrl);
            httpPost.setEntity(entity);
            log.info("???????????????????????????" + params);
            response = httpClient.execute(httpPost);
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
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
     * ??????????????????token???????????????ContentType??????????????? 415 ????????????????????????
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
            // ??????Json?????????????????????
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            //            entity.setContentEncoding("UTF-8");
            log.info("???????????????URL??????" + apiUrl);
            httpPost.setEntity(entity);
            log.info("???????????????????????????" + paramsJson);
            response = httpClient.execute(httpPost);
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
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
     * post?????????????????????????????????http????????????????????????????????????????????????http??????
     * @param apiUrl url
     * @param params ???????????????????????????map?????????
     * @param headerParams head??????
     * @return ????????????
     */
    public static HttpUtilsResult doPostResult(String apiUrl, Object params,
                                               Map<String, String> headerParams) {
        return doPostResult(apiUrl, toJson(params), headerParams);
    }

    /**
     * post?????????????????????????????????http????????????????????????????????????????????????http??????
     * @param apiUrl url
     * @param params ????????????????????????json????????????
     * @param headerParams head??????
     * @return ????????????
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
            // ??????Json?????????????????????
            entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            entity.setContentEncoding("UTF-8");
            log.info("???????????????URL??????" + apiUrl);
            httpPost.setEntity(entity);
            log.info("???????????????????????????" + params);
            response = httpClient.execute(httpPost);
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
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
     * Post??????
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
     * Post??????Json??????
     *
     * @param apiUrl url
     * @param params ??????map
     * @return ????????????
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
        log.info("???????????????URL??????" + apiUrl);
        String boundary = "-----------------" + System.currentTimeMillis();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName("UTF-8"));//???????????????????????????
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//???????????????????????????
        builder.setBoundary(boundary);
        if (!CollectionUtils.isEmpty(fileMap)) {
            fileMap.forEach(builder::addBinaryBody);
        }
        ContentType contentType = ContentType.create(mimeType, "UTF-8");
        paramsMap.forEach((k, v) -> builder.addTextBody(k, String.valueOf(v), contentType));

        try {
            HttpEntity entity = builder.build();// ?????? HTTP POST ??????
            httpPost.setEntity(entity);//??????????????????

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
            httpStr = EntityUtils.toString(responseEntity, "UTF-8");
            log.info("????????? ???" + httpStr);
        } catch (IOException e) {
            log.error("????????????", e);
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
     * ??????????????????????????????????????????
     * @param apiUrl ????????????url
     * @param file ????????????
     * @param headerParams ?????????
     * @return result ????????????
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
            log.info("???????????????URL??????" + apiUrl);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create()
                    .setCharset(Charset.forName("UTF-8")).setMode(HttpMultipartMode.RFC6532);
            mEntityBuilder.addBinaryBody("file", file);
            httpPost.setEntity(mEntityBuilder.build());
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                result = EntityUtils.toString(responseEntity, "UTF-8");
                log.info("????????? ???" + result);
                closeInputStream(response);
            }
        } catch (Exception e) {
            log.error("????????????", e);
            result = e.getMessage();
        }
        return result;
    }

    /**
     * ???????????????????????????????????????
     * @param apiUrl ????????????url
     * @param file ????????????
     * @param headerParams ?????????
     * @return result ????????????
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
            log.info("???????????????URL??????" + apiUrl);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create()
                    .setCharset(Charset.forName("UTF-8")).setMode(HttpMultipartMode.RFC6532);
            mEntityBuilder.addPart("file", new FileBody(file, ContentType.create("text/plain")));
            httpPost.setEntity(mEntityBuilder.build());
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("??????????????????" + response.getStatusLine().getStatusCode());
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                result = EntityUtils.toString(responseEntity, "UTF-8");
                log.info("????????? ???" + result);
                closeInputStream(response);
            }
        } catch (Exception e) {
            log.error("????????????", e);
            result = e.getMessage();
        }
        return result;
    }

    /**
     * ??????response?????????
     *
     * @param response ??????
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
            log.info("???????????????body??? ???" + json);
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
     * ???Map???????????????key???????????????????????????=???????????????????????????&???????????????????????????
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
