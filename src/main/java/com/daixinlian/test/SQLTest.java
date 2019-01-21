package com.daixinlian.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daixinlian.dao.FileMapMapper;
import com.daixinlian.entity.FileMap;


@Service
@Transactional
public class SQLTest {

	@Autowired
	FileMapMapper fileMapMapper;
	
	//自动载入，程序启动时候就运行啦。。。。
//	@Autowired
	public void run() {
		
		
//test_insert_filemap();
		
//		test_query_filemap();
		test_batch_insert_filemap();
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

	private void test_query_filemap() {
		// TODO Auto-generated method stub
		System.out.println("test_query...");	
		List<FileMap> test_query = fileMapMapper.test_query("123126");
		System.out.println(test_query.get(0));
		return;
	}

	
	public void test_insert_filemap()throws RuntimeException {
		System.out.println("test_insert_filemap...");	
		FileMap fileMap = new FileMap();
		fileMap.setCreateTime("2018-15-12");
		fileMap.setFileId("2");
		fileMap.setFileMd5("zcctest1");
		fileMap.setTripType(1);
		fileMapMapper.test_insert(fileMap);
		int i=1/0;
		fileMap.setFileMd5("zcctest2");
		fileMapMapper.test_insert(fileMap);
	}
	
	
	public void test_batch_insert_filemap() {
		System.out.println("test_batch_insert_filemap...");	
		ArrayList<FileMap> arrayList = new ArrayList<FileMap>();
		for (int i = 0; i < 200000; i++) {
			FileMap fileMap=new FileMap();
			fileMap.setCreateTime("2019-01-16"+i);
			fileMap.setFileId("21313213"+i);
			fileMap.setFileMd5("fasdfdsf"+i);
//		fileMap.setId(123234);
			fileMap.setTripType(1);
			arrayList.add(fileMap);
			fileMap=null;
		}
		
		
		fileMapMapper.test_batch_insert(arrayList);
		int i=1/0;
		FileMap fileMap2 = new FileMap();
		fileMap2.setCreateTime("2018-15-12");
		fileMap2.setFileId("2");
		fileMap2.setFileMd5("cschenggong");
		fileMap2.setTripType(1);
		fileMapMapper.test_insert(fileMap2);
		fileMap2 = null;
		arrayList = null;
	}


}
