package com.whatu1.es.controller;

import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("high")
public class RestHighController {

	private static final Logger logger = LoggerFactory.getLogger(RestLowController.class);

	@Autowired
	private RestHighLevelClient client;

}
