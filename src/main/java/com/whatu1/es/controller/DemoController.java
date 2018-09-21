package com.whatu1.es.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatu1.es.dao.DemoRepository;
import com.whatu1.es.entity.Demo;

/**
 * @Description ElasticSearch测试Controller
 * @author Desig
 */
@RestController
public class DemoController {

	@Autowired
	private DemoRepository demoRepository;

	/**
	 * @Description 新增数据
	 * @param name
	 */
	@PostMapping("addDemo")
	public void addDemo(String name) {
		Demo demo = new Demo();
		demo.setId(new Double(Math.round(Math.random() * 10000)).longValue());
		demo.setName(name);
		demoRepository.save(demo);
	}

	/**
	 * @Description 查询数据
	 * @param name
	 */
	@GetMapping("getDemos")
	public List<Demo> getByIndex(String indexKey) {
		List<Demo> list = new ArrayList<>();
		QueryStringQueryBuilder builder = new QueryStringQueryBuilder(indexKey);
		Iterable<Demo> searchResult = demoRepository.search(builder);
		Iterator<Demo> iterator = searchResult.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	/**
	 * @Description 删除数据
	 * @param name
	 */
	@PostMapping("deleteAll")
	public void deleteAll(String name) {
		demoRepository.deleteAll();
	}
}
