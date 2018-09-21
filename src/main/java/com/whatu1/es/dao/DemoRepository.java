package com.whatu1.es.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.whatu1.es.entity.Demo;

// 泛型的参数分别是实体类型和主键类型
public interface DemoRepository extends ElasticsearchRepository<Demo, Long> {

}
