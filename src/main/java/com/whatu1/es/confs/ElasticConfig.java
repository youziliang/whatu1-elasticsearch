package com.whatu1.es.confs;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

	Header[] headers = { new BasicHeader("domain", "www.whatu1.com"), new BasicHeader("port", "9200") };

	private String host = "www.whatu1.com";

	private int port = 9200;

	private String schema = "http";

	private int connectTimeOut = 1000;

	private int socketTimeOut = 30000;

	private int connectionRequestTimeOut = 500;

	private int maxConnectNum = 100;

	private int maxConnectPerRoute = 100;

	/**
	 * RestLowClient客戶端
	 */
	@Bean
	public RestClient initLowClient() {
		return getBuilder().build();
	}

	/**
	 * RestHighClient客戶端
	 */
	@Bean
	public RestHighLevelClient initHighClient() {
		return new RestHighLevelClient(getBuilder());
	}

	/**
	 * 關閉RestLowClient客戶端
	 */
	public void closeLowClient(RestClient lowClient) throws IOException {
		lowClient.close();
	}

	/**
	 * 關閉RestHighClient客戶端
	 */
	public void closeHighClient(RestHighLevelClient client) throws IOException {
		client.close();
	}

	/**
	 * RestClientBuilder配置
	 */
	private RestClientBuilder getBuilder() {
		RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, schema));

		// 设置每个请求需要发送的默认headers，这样就不用在每个请求中指定它们。
		builder.setDefaultHeaders(headers);

		// 设置应该授予的超时时间，以防对相同的请求进行多次尝试。默认值是30秒，与默认socket超时时间相同。
		// 如果自定义socket超时时间，则应相应地调整最大重试超时时间。
		builder.setMaxRetryTimeoutMillis(10000);
		builder.setFailureListener(new RestClient.FailureListener() {
			@Override
			public void onFailure(HttpHost host) {
				// 设置一个监听程序，每次节点发生故障时都会收到通知，这样就可以采取相应的措施。
				// Used internally when sniffing on failure is enabled
			}
		});

		builder.setRequestConfigCallback(new RequestConfigCallback() {
			@Override
			public Builder customizeRequestConfig(Builder builder) {
				// 设置允许修改默认请求配置的回调
				// （例如，请求超时，身份验证或org.apache.http.client.config.RequestConfig.Builder允许设置的任何内容）
				builder.setConnectTimeout(connectTimeOut);
				builder.setSocketTimeout(socketTimeOut);
				builder.setConnectionRequestTimeout(connectionRequestTimeOut);
				return builder;
			}
		});

		builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
			@Override
			public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder builder) {
				// 设置允许修改http客户端配置的回调
				// （例如，通过SSL的加密通信，或者org.apache.http.impl.nio.client.HttpAsyncClientBuilder允许设置的任何内容）
				// builder.setProxy(new HttpHost("proxy", 9000, "http"));
				builder.setMaxConnTotal(maxConnectNum);
				builder.setMaxConnPerRoute(maxConnectPerRoute);
				return builder;
			}
		});
		return builder;
	}

}
