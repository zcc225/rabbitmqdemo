package com.daixinlian.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.daixinlian.entity.FileMap;

public interface FileMapMapper {

	//插入
	void test_insert(FileMap fileMap);
	
	//插入
	void test_batch_insert(List<FileMap> fileMapList);
	
	//查询。@Param对应参数属性注解，There is no getter for property named 'xx' in 'class java.lang.Integer
	List<FileMap> test_query(@Param("id")String string);
}
