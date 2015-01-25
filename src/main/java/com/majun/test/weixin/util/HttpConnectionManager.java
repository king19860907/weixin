package com.majun.test.weixin.util;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Service;

public class HttpConnectionManager {
	private static final Log logger = LogFactory
			.getLog(HttpConnectionManager.class);

	/**
	 * 连接池里的最大连接数
	 */
	public static final int MAX_TOTAL_CONNECTIONS = 200;

	/**
	 * 每个路由的默认最大连接数
	 */
	public static final int MAX_ROUTE_CONNECTIONS = 100;

	/**
	 * 连接超时时间
	 */
	public static final int CONNECT_TIMEOUT = 100000;

	/**
	 * 套接字超时时间
	 */
	public static final int SOCKET_TIMEOUT = 100000;

	/**
	 * 连接池中 连接请求执行被阻塞的超时时间
	 */
	public static final long CONN_MANAGER_TIMEOUT = 300000;

	/**
	 * http连接相关参数
	 */
	private HttpParams parentParams;

	/**
	 * 默认目标主机
	 */
	private static HttpHost DEFAULT_TARGETHOST;

	// private static String DEFAULT_IP = "http://180.153.103.86";

	// private static int DEFAULT_PORT = 10050;

	/**
	 * http线程池管理器
	 */
	private static PoolingClientConnectionManager cm;
	static {
		// SchemeRegistry schemeRegistry = new SchemeRegistry();
		/*
		 * schemeRegistry.register(new Scheme("http", DEFAULT_PORT,
		 * PlainSocketFactory .getSocketFactory()));
		 * 
		 * DEFAULT_TARGETHOST = new HttpHost( DEFAULT_IP, DEFAULT_PORT);
		 */
		// cm = new
		// PoolingClientConnectionManager(schemeRegistry,1,TimeUnit.NANOSECONDS);

		cm = new PoolingClientConnectionManager();

		cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);

		cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

		// cm.setMaxPerRoute(new HttpRoute(DEFAULT_TARGETHOST),
		// MAX_ROUTE_CONNECTIONS); // 设置对目标主机的最大连接数
	}

	/**
	 * http客户端
	 */
	private DefaultHttpClient httpClient;

	/**
	 * 初始化http连接池，设置参数、http头等等信息
	 * 
	 */

	private static X509TrustManager tm = new X509TrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	};

	@SuppressWarnings("deprecation")
	public DefaultHttpClient getClient() {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = client.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			client = new DefaultHttpClient(ccm, client.getParams());
			return client;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private DefaultHttpClient getClientOld() {
		parentParams = new BasicHttpParams();
		parentParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);

		parentParams
				.setParameter(ClientPNames.DEFAULT_HOST, DEFAULT_TARGETHOST); // 设置默认targetHost

		parentParams.setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		parentParams.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT,
				CONN_MANAGER_TIMEOUT);
		parentParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				CONNECT_TIMEOUT);
		parentParams.setParameter(CoreConnectionPNames.SO_TIMEOUT,
				SOCKET_TIMEOUT);

		parentParams.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		parentParams.setParameter(ClientPNames.HANDLE_REDIRECTS, true);

		// 设置头信息,模拟浏览器

		Collection<BasicHeader> collection = new ArrayList<BasicHeader>();
		collection
				.add(new BasicHeader("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"));
		collection
				.add(new BasicHeader("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*;q=0.8"));
		collection.add(new BasicHeader("Accept-Language",
				"zh-cn,zh,en-US,en;q=0.5"));
		collection.add(new BasicHeader("Accept-Charset",
				"ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7"));
		collection.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));

		parentParams.setParameter(ClientPNames.DEFAULT_HEADERS, collection);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				if (executionCount >= 5) {
					// 如果超过最大重试次数，那么就不要继续了
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					// 不要重试SSL握手异常
					return false;
				}
				HttpRequest request = (HttpRequest) context
						.getAttribute(ExecutionContext.HTTP_REQUEST);
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// 如果请求被认为是幂等的，那么就重试
					return true;
				}
				return false;
			}
		};

		httpClient = new DefaultHttpClient(cm, parentParams);

		httpClient.setHttpRequestRetryHandler(httpRequestRetryHandler);

		httpClient.getParams().setBooleanParameter(
				"http.protocol.expect-continue", false);

		return httpClient;
	}
	
	public List<String> doHttpGet(String url) {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = null;
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Connection", "close");
		DefaultHttpClient httpClient = getClient();
		InputStream inputStream = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
			// 获取返回的数据信息
			reader = new BufferedReader(new InputStreamReader(inputStream,
					"UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.add(line);
				logger.debug(line);
			}
			inputStream.close();
			inputStream = null;
			reader.close();
			reader = null;
			logger.info("response.getStatusLine().getStatusCode():"
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != 200) {
				result.clear();
				result.add("error");
				logger.info("response.getStatusLine().getStatusCode():"
						+ response.getStatusLine().getStatusCode());
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("doHttpPost().UnsupportedEncodingException=", e);
		} catch (ClientProtocolException e) {
			logger.error("doHttpPost().ClientProtocolException=", e);
		} catch (IOException e) {
			if (e.getMessage().equalsIgnoreCase("Read timed out")) {
				result.add("success");
				return result;
			}
			logger.error("doHttpPost().IOException=", e);
		} catch (Exception e) {
			logger.error("doHttpPost().Exception=", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					logger.error("inputStream.close()", e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					logger.error("inputStream.close()", e);
				}
			}
			httpGet.releaseConnection();
			httpClient.clearRequestInterceptors();
			httpClient.clearResponseInterceptors();
			httpClient.getConnectionManager().closeExpiredConnections();
			httpClient.getConnectionManager().closeIdleConnections(1,
					TimeUnit.NANOSECONDS);
		}
		return result;

	}
	
	public List<String> doHttpDownload(String url,String descFilePath) {
		List<String> result = new ArrayList<String>();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Connection", "close");
		DefaultHttpClient httpClient = getClient();
		InputStream inputStream = null;
		DataInputStream dataInputStream = null;
		FileOutputStream fos = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
			
			dataInputStream = new DataInputStream(inputStream);
			byte[] buffer = new byte[1024];
			int length;
			
			fos = new FileOutputStream(new File(descFilePath));
			//开始填充数据
			while((length=dataInputStream.read(buffer))>0){
				fos.write(buffer, 0, length);
			}
 			
			inputStream.close();
			inputStream = null;
			logger.info("response.getStatusLine().getStatusCode():"
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != 200) {
				result.clear();
				result.add("error");
				logger.info("response.getStatusLine().getStatusCode():"
						+ response.getStatusLine().getStatusCode());
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("doHttpPost().UnsupportedEncodingException=", e);
		} catch (ClientProtocolException e) {
			logger.error("doHttpPost().ClientProtocolException=", e);
		} catch (IOException e) {
			if (e.getMessage().equalsIgnoreCase("Read timed out")) {
				result.add("success");
				return result;
			}
			logger.error("doHttpPost().IOException=", e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("doHttpPost().Exception=", e);
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					logger.error("inputStream.close()", e);
				}
			}
			if (dataInputStream != null) {
				try {
					dataInputStream.close();
					dataInputStream = null;
				} catch (IOException e) {
					logger.error("dataInputStream.close()", e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
					fos = null;
				} catch (IOException e) {
					logger.error("fileOutPutStream.close()", e);
				}
			}
			httpGet.releaseConnection();
			httpClient.clearRequestInterceptors();
			httpClient.clearResponseInterceptors();
			httpClient.getConnectionManager().closeExpiredConnections();
			httpClient.getConnectionManager().closeIdleConnections(1,
					TimeUnit.NANOSECONDS);
		}
		return result;

	}

	public List<String> doHttpPost(String url, Map<String, String> paraMap) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> ita = paraMap.entrySet().iterator();
		while (ita.hasNext()) {
			Entry<String, String> ent = ita.next();
			params.add(new BasicNameValuePair(ent.getKey(), ent.getValue()));
		}
		return this.doHttpPost(url, params);
	}

	private List<String> doHttpPost(String url, List<NameValuePair> params) {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = null;
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Connection", "close");
		DefaultHttpClient httpClient = getClient();
		InputStream inputStream = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
			// 获取返回的数据信息
			reader = new BufferedReader(new InputStreamReader(inputStream,
					"UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.add(line);
				logger.debug(line);
			}
			inputStream.close();
			inputStream = null;
			reader.close();
			reader = null;
			logger.info("response.getStatusLine().getStatusCode():"
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != 200) {
				result.clear();
				result.add("error");
				logger.info("response.getStatusLine().getStatusCode():"
						+ response.getStatusLine().getStatusCode());
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("doHttpPost().UnsupportedEncodingException=", e);
		} catch (ClientProtocolException e) {
			logger.error("doHttpPost().ClientProtocolException=", e);
		} catch (IOException e) {
			if (e.getMessage().equalsIgnoreCase("Read timed out")) {
				result.add("success");
				return result;
			}
			logger.error("doHttpPost().IOException=", e);
		} catch (Exception e) {
			logger.error("doHttpPost().Exception=", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					logger.error("inputStream.close()", e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					logger.error("inputStream.close()", e);
				}
			}
			httpPost.releaseConnection();
			httpClient.clearRequestInterceptors();
			httpClient.clearResponseInterceptors();
			httpClient.getConnectionManager().closeExpiredConnections();
			httpClient.getConnectionManager().closeIdleConnections(1,
					TimeUnit.NANOSECONDS);
		}
		return result;
	}
	
	public List<String> doPostStringData(String sendUrl, String data){
		List<String> result = new ArrayList<String>();
		BufferedReader reader = null;
		HttpPost httpPost = new HttpPost(sendUrl);
		httpPost.addHeader("Connection", "close");
		DefaultHttpClient httpClient = getClient();
		InputStream inputStream = null;
		try {
			httpPost.setHeader("Accept-Encoding", "gzip,deflate");
			httpPost.setEntity(new StringEntity(data, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
			// 获取返回的数据信息
			reader = new BufferedReader(new InputStreamReader(inputStream,
					"UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.add(line);
				logger.debug(line);
			}
			inputStream.close();
			inputStream = null;
			reader.close();
			reader = null;
			logger.info("response.getStatusLine().getStatusCode():"
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != 200) {
				result.clear();
				result.add("error");
				logger.info("response.getStatusLine().getStatusCode():"
						+ response.getStatusLine().getStatusCode());
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("doHttpPost().UnsupportedEncodingException=", e);
		} catch (ClientProtocolException e) {
			logger.error("doHttpPost().ClientProtocolException=", e);
		} catch (IOException e) {
			if (e.getMessage().equalsIgnoreCase("Read timed out")) {
				result.add("success");
				return result;
			}
			logger.error("doHttpPost().IOException=", e);
		} catch (Exception e) {
			logger.error("doHttpPost().Exception=", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					logger.error("inputStream.close()", e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					logger.error("inputStream.close()", e);
				}
			}
			httpPost.releaseConnection();
			httpClient.clearRequestInterceptors();
			httpClient.clearResponseInterceptors();
			httpClient.getConnectionManager().closeExpiredConnections();
			httpClient.getConnectionManager().closeIdleConnections(1,
					TimeUnit.NANOSECONDS);
		}
		return result;
	}
}
