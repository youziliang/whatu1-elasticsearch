package com.whatu1.es.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * String indexName(); 索引库的名称，个人建议以项目的名称命名
 * String type() default ""; 类型，个人建议以实体的名称命名
 * short shards() default 5; 默认分区数
 * short replicas() default 1; 每个分区默认的备份数
 * String refreshInterval() default "1s"; 刷新间隔
 * String indexStoreType() default "fs"; 索引文件存储类型
 */
@Document(indexName = "domodb", type = "demo", shards = 5, replicas = 1, refreshInterval = "-1", indexStoreType = "fs")
public class Demo implements Serializable {

	private static final long serialVersionUID = 6079581006537788324L;

	@Id
	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Demo [id=" + id + ", name=" + name + "]";
	}
}
