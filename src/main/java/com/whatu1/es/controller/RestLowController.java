package com.whatu1.es.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.RequestLine;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * 疑難雜症： 1、查詢、新增索引，type級別的操作會報錯，index和id級別操作正常
 * 
 * @author Desig
 *
 */
@RestController
@RequestMapping("low")
public class RestLowController {

	private static final Logger logger = LoggerFactory.getLogger(RestLowController.class);

	@Autowired
	private RestClient client;

	/**
	 * 查詢索引是否存在
	 */
	@RequestMapping("isExist")
	public Boolean isExist(String index, String type, String id) throws IOException {
		String endpoint = "/";
		if (!StringUtils.isEmpty(index)) {
			endpoint = endpoint + index;
		}
		if (!StringUtils.isEmpty(type)) {
			endpoint = endpoint + "/" + type;
		}
		if (!StringUtils.isEmpty(id)) {
			endpoint = endpoint + "/" + id;
		}
		logger.info("endpoint: {}", endpoint);
		Response response = client.performRequest("HEAD", endpoint, Collections.<String, String>emptyMap());
		int statusCode = response.getStatusLine().getStatusCode();
		logger.info("索引查詢結果: {}", statusCode);
		if (404 == statusCode) {
			return false;
		}
		return true;
	}

	/**
	 * 創建索引
	 */
	@RequestMapping("createIndex")
	public String createIndex(String index) throws IOException {
		Response response = client.performRequest("POST", "/" + index, Collections.<String, String>emptyMap());
		String responseEntity = EntityUtils.toString(response.getEntity());
		logger.info("創建索引結果: {}", responseEntity);
		return responseEntity;
	}

	/**
	 * 删除索引
	 */
	@RequestMapping("deleteIndex")
	public String deleteIndex(String index) throws IOException {
		Response response = client.performRequest("DELETE", "/" + index, Collections.<String, String>emptyMap());
		String responseEntity = EntityUtils.toString(response.getEntity());
		logger.info("删除索引結果: {}", responseEntity);
		return responseEntity;
	}

	/**
	 * 根据属性删除所有索引
	 */
	@RequestMapping("deleteIndexByKeyword")
	public String deleteIndexByKeyword(String index, String type, String keyword) throws IOException {
		keyword = "{\"query\": {\"match_all\":{}}}";
		HttpEntity entity = new NStringEntity(keyword, ContentType.APPLICATION_JSON);
		Response response = client.performRequest("POST", "/" + index + "/" + type, Collections.<String, String>emptyMap(), entity);
		String responseEntity = EntityUtils.toString(response.getEntity());
		logger.info("根据属性删除所有索引結果: {}", responseEntity);
		return responseEntity;
	}

	/**
	 * 新增/修改索引數據
	 */
	@RequestMapping("createOrUpdateData")
	public String createOrUpdateData(String index, String type, String id, String jsonData) throws IOException {
		jsonData = "{\"name\":\"友子梁\",\"date\":\"2013-01-30\",\"msg\":\"Trying Out Elasticsearch\"}";
		HttpEntity entity = new NStringEntity(jsonData, ContentType.APPLICATION_JSON);
		Response response = client.performRequest("PUT", "/" + index + "/" + type + "/" + id, Collections.<String, String>emptyMap(), entity);
		String responseEntity = EntityUtils.toString(response.getEntity());
		logger.info("新增/修改索引數據結果: {}", responseEntity);
		return responseEntity;
	}

	/**
	 * 根据属性查询索引數據
	 */
	@RequestMapping("queryDataByKeyword")
	public String queryDataByKeyword(String index, String type, String keyword) throws IOException {
		keyword = "{\"query\" : {\"match_phrase\": {\"name\":{\"query\":\"友\",\"boost\":\"5\"}}}}}";
		HttpEntity entity = new NStringEntity(keyword, ContentType.APPLICATION_JSON);
		Response response = client.performRequest("GET", "/" + index + "/" + type + "/_search", Collections.singletonMap("pretty", "true"), entity);
		String responseEntity = EntityUtils.toString(response.getEntity());
		logger.info("根据属性查询索引數據結果: {}", responseEntity);
		return responseEntity;
	}

	/**
	 * 批量新增索引數據
	 */
	@RequestMapping("createBulkData")
	public String createBulkData(String index, String type, String id, String jsonData) throws IOException {
		jsonData = "{\"name\":\"友子梁\",\"date\":\"2013-01-30\",\"msg\":\"Trying Out Elasticsearch\"}\n";
		// 多條數據之間一定要換行"\n"
		jsonData += "{\"name\":\"王玨\",\"date\":\"2013-01-30\",\"msg\":\"Trying Out Elasticsearch\"}";
		HttpEntity entity = new NStringEntity(jsonData, ContentType.APPLICATION_JSON);
		Response response = client.performRequest("POST", "/" + index + "/" + type + "/" + id, Collections.singletonMap("pretty", "true"), entity);
		String responseEntity = EntityUtils.toString(response.getEntity());
		logger.info("批量新增索引數據結果: {}", responseEntity);
		return responseEntity;
	}

	/**
	 * 删除索引數據
	 */
	@RequestMapping("deleteData")
	public String deleteData(String index, String type, String id) throws IOException {
		Response response = client.performRequest("DELETE", "/" + index + "/" + type + "/" + id, Collections.<String, String>emptyMap());
		String responseEntity = EntityUtils.toString(response.getEntity());
		logger.info("删除索引數據結果: {}", responseEntity);
		return responseEntity;
	}

	/**
	 * 根据属性删除索引數據
	 */
	@RequestMapping("deleteDataByKeyword")
	public String deleteDataByKeyword(String index, String type, String keyword) throws IOException {
		keyword = "{\"query\": {\"match_phrase\": {\"name\": \"友子梁\"}}}";
		HttpEntity entity = new NStringEntity(keyword, ContentType.APPLICATION_JSON);
		Response response = client.performRequest("POST", "/" + index + "/" + type + "/_delete_by_query", Collections.<String, String>emptyMap(), entity);
		String responseEntity = EntityUtils.toString(response.getEntity());
		logger.info("根据属性删除索引數據結果: {}", responseEntity);
		return responseEntity;
	}

	/**
	 * 发送同步请求
	 */
	@RequestMapping("testMethod")
	public String testMethod() throws IOException {
		// 方式4：提供谓词，终节点，可选查询字符串参数，可选请求主体
		// 以及用于为每个请求尝试创建org.apache.http.nio.protocol.HttpAsyncResponseConsumer回调实例的可选工厂来发送请求。
		// 控制响应正文如何从客户端的非阻塞HTTP连接进行流式传输。
		// 如果未提供，则使用默认实现，将整个响应主体缓存在堆内存中，最大为100 MB。
		Map<String, String> params = Collections.emptyMap();
		HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory consumerFactory = new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(
				30 * 1024 * 1024);
		Response response = client.performRequest("GET", "/posts/_search", params, null, consumerFactory);
		return parseResponse(response);
	}

	@SuppressWarnings("unused")
	private String parseResponse(Response response) throws ParseException, IOException {
		// 关于已执行请求的信息
		RequestLine requestLine = response.getRequestLine();
		// 返回响应的主机
		HttpHost host = response.getHost();
		// 响应状态行，可以从中获取状态码
		int statusCode = response.getStatusLine().getStatusCode();
		// 获取响应头
		Header[] headers = response.getHeaders();
		// 获取指定名称的响应头
		String header = response.getHeader("content-type");
		// 响应体包含在org.apache.http.HttpEntity对象中
		String responseBody = EntityUtils.toString(response.getEntity());
		return responseBody;
	}
}
