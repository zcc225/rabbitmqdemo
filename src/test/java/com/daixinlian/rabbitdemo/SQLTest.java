package com.daixinlian.rabbitdemo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.daixinlian.dao.FileMapMapper;
import com.daixinlian.entity.FileMap;


@Component
public class SQLTest {

	@Autowired
	FileMapMapper fileMapMapper;
	
	//自动载入，程序启动时候就运行啦。。。。
	@Autowired
	public void run() {
		
		
//test_insert_filemap();
		
//		test_query_filemap();
		test_insert_filemap();
//		test_query();

//		test_insert();
//		test_query();
//
//		test_update();
//		test_query();
//
//		test_multi_query();
//
//		test_multi_insert();
//		test_multi_query();		
//
//		test_delete();
//		test_query();
//
//		test_multi_delete();
//		test_multi_query();
//
//		test_exe_procedure(true);
//		test_exe_procedure(false);
		
////		test_insert_filemap();
//		
//		test_query_filemap();
	}

//	private void test_query_filemap() {
//		// TODO Auto-generated method stub
//		System.out.println("test_query...");	
//		List<FileMap> test_query = fileMapMapper.test_query("1");
//		System.out.println(test_query.get(0));
//		return;
//	}

	public void test_insert_filemap() {
		System.out.println("test_insert_filemap...");	
		FileMap fileMap = new FileMap();
		fileMap.setCreateTime("2018-15-12");
		fileMap.setFileId("21313213");
		fileMap.setFileMd5("fasdfdsf");
//		fileMap.setId(123234);
		fileMap.setTripType(1);
		fileMapMapper.test_insert(fileMap);
	}


}
