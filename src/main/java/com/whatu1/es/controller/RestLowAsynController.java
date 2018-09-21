package com.whatu1.es.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("low/asyn")
public class RestLowAsynController {

	@Autowired
	private RestClient client;

	/**
	 * 发送同步请求
	 */
	@RequestMapping("performRequestAsynA")
	public void performRequestAsynA() throws IOException {

		// 方式1： 提供谓词，终节点和响应监听器来发送异步请求，一旦请求完成，就会通知响应监听器，这三个参数是必需要的参数
		client.performRequestAsync("GET", "/", new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				// 定义请求成功执行时需要做的事情
			}

			@Override
			public void onFailure(Exception exception) {
				// 定义请求失败时需要做的事情，即每当发生连接错误或返回错误状态码时做的操作。
			}
		});
	}

	/**
	 * 发送同步请求
	 */
	@RequestMapping("performRequestAsynB")
	public void performRequestAsynB() throws IOException {

		// 方式2： 提供谓词，终节点，一些查询字符串参数和响应监听器来发送异步请求
		Map<String, String> params = Collections.singletonMap("pretty", "true");
		client.performRequestAsync("GET", "/", params, new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				// 定义请求成功执行时需要做的事情
			}

			@Override
			public void onFailure(Exception exception) {
				// 定义请求失败时需要做的事情，即每当发生连接错误或返回错误状态码时做的操作。
			}
		});
	}

	/**
	 * 发送同步请求
	 */
	@RequestMapping("performRequestAsynC")
	public void performRequestAsynC() throws IOException {

		// 方式3：提供谓词，终节点，可选查询字符串参数，
		// org.apache.http.HttpEntity对象中包含的请求主体以及在请求完成后通知响应侦听器 来发送异步请求
		Map<String, String> params = Collections.emptyMap();
		String jsonString = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\"," + "\"message\":\"trying out Elasticsearch\"" + "}";
		NStringEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
		client.performRequestAsync("PUT", "/posts/doc/1", params, entity, new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				// 定义请求成功执行时需要做的事情
			}

			@Override
			public void onFailure(Exception exception) {
				// 定义请求失败时需要做的事情，即每当发生连接错误或返回错误状态码时做的操作。
			}
		});
	}

	/**
	 * 发送同步请求
	 */
	@RequestMapping("performRequestAsynD")
	public void performRequestAsynD() throws IOException {

		// 方式4：提供谓词，终节点，可选查询字符串参数，可选请求主体
		// 以及用于为每个请求尝试创建org.apache.http.nio.protocol.HttpAsyncResponseConsumer回调实例的可选工厂
		// 来发送异步请求。
		// 控制响应正文如何从客户端的非阻塞HTTP连接进行流式传输。
		// 如果未提供，则使用默认实现，将整个响应主体缓存在堆内存中，最大为100 MB。
		Map<String, String> params = Collections.emptyMap();
		HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory consumerFactory = new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(
				30 * 1024 * 1024);
		client.performRequestAsync("GET", "/posts/_search", params, null, consumerFactory, new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				// 定义请求成功执行时需要做的事情
			}

			@Override
			public void onFailure(Exception exception) {
				// 定义请求失败时需要做的事情，即每当发生连接错误或返回错误状态码时做的操作。
			}
		});
	}

}
