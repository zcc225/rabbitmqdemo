package com.daixinlian.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.daixinlian.dao.FileMapMapper;
import com.daixinlian.entity.FileMap;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Validator;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;

@Service
@Transactional
public class ExportService {
	
	@Autowired
	FileMapMapper fileMapMapper;
	

	/**
	 * 检查后缀
	 * @return
	 */
	public void checkSuffix(String originalFilename){
		String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		if(!"xls,xlsx".contains(suffix)){
			throw new RuntimeException("老铁走错片场了!我们在导入数据,只支持 【xls,xlsx】格式文件,您传入的是格式为"+suffix);
		}
	}

	/**
	 * 导入
	 * @param path
	 * @param file
	 */
	public void export(String path,MultipartFile file) {
		//检查数据正确性
		List<Map<String, Object>> checkData = checkData(file);
		//构建实体类
		HashMap<String, ArrayList> allDataObjectListMap =  buildObject(checkData);
		//批量插入数据库
		batchSave(allDataObjectListMap);
		//关联旧案及相关案件
		linkOldCaseAndData();
		
	}
	/**
	 * 关联旧案数据
	 */
	private void linkOldCaseAndData() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 批量保存数据
	 * @param allDataObjectListMap
	 */
	private void batchSave(HashMap<String, ArrayList> allDataObjectListMap) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 根据数据构建实体类
	 * @param allDataObjectListMap
	 */
	private HashMap<String, ArrayList> buildObject(List<Map<String, Object>> checkData) {
		// TODO Auto-generated method stub
		ArrayList<Object> caseHeadList = new ArrayList<>();//索引表集合
		ArrayList<Object> detailList = new ArrayList<>();//详情
		ArrayList<Object> linkmanList = new ArrayList<>();//联系人
		ArrayList<FileMap> ceshiList = new ArrayList<>();//测试 集合
		for (int i = 0; i < checkData.size(); i++) {
			Map<String, Object> map = checkData.get(i);
			//检查数据 组装所需保存数据
			buildObjectOne( i, map);
		
		}
		return null;
	}

	/**
	 * 对一行数据做实体类构建
	 * @param i
	 * @param map
	 */
	private void buildObjectOne(int i, Map<String, Object> map) {
		// TODO Auto-generated method stub
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			String value = map.get(key)==null?"":map.get(key).toString().trim();
			switch (key) {
			case "借款人身份证号":
				if(Validator.isNotEmpty(value)){
					//设置身份证号
				
				}
				break;
			case "逾期-逾期总金额":
				if(Validator.isNotEmpty(value)){
					//设置逾期总金额
				}
				break;
				
			
			default:
				Console.log("{}数据没有对应赋值",key);
				break;
			}
		}
	}


	//总案件key集合
	HashMap<String, String> allCase = new HashMap<String,String>();
	
//	ArrayList<FileMap> allcaseList = new ArrayList<FileMap>(); 批量插入集合 用于 caseHead
	
	int allCaseMoney=0; //导入俺家你的总金额 再检查案件中赋值
	public List<Map<String, Object>> checkData(MultipartFile file) {
		
		List<Map<String,Object>> readAll = null;
		//总金额
		String originalFilename = null;
		//获取文件名称
		originalFilename = file.getOriginalFilename();
		//检查文件后缀
		checkSuffix(originalFilename);

		
		try {
			//读取数据
			ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
			readAll = reader.readAll();
			
			for (int i = 0; i < readAll.size(); i++) {
				Map<String, Object> map = readAll.get(i);
				//检查数据 组装所需保存数据
			checkRow( i, map);
			
//				计算总金额
//			allCaseMoney = calTotalMoney(allCaseMoney, map);
			}
//			FileMap fileMap = new FileMap();
//			fileMap.setCreateTime("2018-15-12");
//			fileMap.setFileId("2");
//			fileMap.setFileMd5("zcctest1");
//			fileMap.setTripType(1);
//			fileMapMapper.test_insert(fileMap);
			//插入数据
//			fileMapMapper.test_batch_insert(allcaseList);
//			Console.log("开始报错");
//			int i=1/0;
//			Console.log("报错了");
			Console.log("总金额===="+allCaseMoney);
			//本次执行清空历史
			
		}catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//对变量及集合清空
			allCaseMoney = 0;
			allCase.clear();
//			allcaseList.clear();
		}
		
		return readAll;
	}
	
	


//	private int calTotalMoney(int allCaseMoney, Map<String, Object> map) {
//		int caseMoney = Integer.valueOf(map.get("逾期-逾期总金额").toString());
//		allCaseMoney=allCaseMoney+caseMoney;
//		return allCaseMoney;
//	}


	private void checkRow( int i, Map<String, Object> map) {
		
//		FileMap fileMap = new FileMap();
		//判重
		StringBuffer allCaseKey = new StringBuffer();
		
		
		//检查cell
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			String value = map.get(key)==null?"":map.get(key).toString().trim();
			switch (key) {
			case "借款人身份证号":
				//添加唯一属性
				allCaseKey.append(value);
				//检查身份证信息
				checkIdcard(i, value);
				
				break;
			case "逾期-逾期总金额":
				//检查逾期金额正确性
				check(i, key, value);
				//TODO  金额 5000$ 
				//金额累加
				allCaseMoney=allCaseMoney+Integer.valueOf(value);
				
				break;
				
//				
			case "借款人客户号":
				allCaseKey.append(value);
				check(i, key, value);
//				if(Validator.isEmpty(value)){
//					throw new RuntimeException("第"+(i+1)+"条案件没有填写"+key+"，请填写"+key+"之后重新导入");
//				}
//				fileMap.setFileId(value);
//				fileMap.setFileMd5(value);
				break;
			case "合同号":
			case "卡号":
				allCaseKey.append(value);
				if(Validator.isEmpty(value)){
					throw new RuntimeException("第"+(i+1)+"条案件没有填写卡号或合同号，请填写卡号或合同号之后重新导入（如没有卡号合同号可以填入证明案件唯一性的凭证编码）");
				}
				break;
			case "客户号":
				allCaseKey.append(value);
				if(Validator.isEmpty(value)){
					throw new RuntimeException("第"+(i+1)+"条案件没有填写卡号或合同号，请填写卡号或合同号之后重新导入（如没有卡号合同号可以填入证明案件唯一性的凭证编码）");
				}
				break;
			case "放款日期":
				if(Validator.isBirthday(value)){
					throw new RuntimeException("第"+(i+1)+"条案件"+key+"格式不正确,请修改{"+value+"}");
				}
//				fileMap.setCreateTime(value);
//				fileMap.setTripType(1);
				break;
			
			default:
				break;
			}
		}
		String allCaseKeyStr =allCaseKey.toString()==null?null: allCaseKey.toString().trim();//得到最后的唯一case标记
		String existskey = allCase.get(allCaseKeyStr);
//		System.out.println(allCaseKeyStr);		
		if(Validator.isNotEmpty(existskey)){
			throw new RuntimeException("第"+(Integer.valueOf(existskey)+1)+"条数据与第"+(i+1)+"条案件重复,请检查");
		}
		allCase.put(allCaseKeyStr, String.valueOf(i));//存入集合 方便后续 判重实用
//		allcaseList.add(fileMap);//转换成对象后存入指定集合
//		fileMap= null;
	}


	private void check(int i, String key, String value) {
		if(Validator.isEmpty(value)){
			throw new RuntimeException("第"+(i+1)+"条案件没有填写"+key+"，请填写"+key+"之后重新导入");
		}
		
		if(!Pattern.matches("^[$0-9]+$",value)){
			throw new RuntimeException("第"+(i+1)+"条案件的案件逾期金额格式不正确，请检查");
		}
	}


	private void checkIdcard(int i, String value) {
		if(Validator.isEmpty(value)){
			throw new RuntimeException("第"+(i+1)+"条案件没有填写借款人身份证号，请填写借款人身份证号之后重新导入");
		}
		
		if(!Pattern.matches("(^\\d{18}$)|(^\\d{15}$)",value)){
			throw new RuntimeException("第"+(i+1)+"条案件的案件借款人身份证号格式不正确，请检查");
		}
	}
	

//	private boolean checkCellValue(Object obj ){
//		if(obj==null){
//			return true;
//		}
//		return StringUtils.isEmpty(obj.toString());
//	}

	public static void main(String[] args) {
		HashMap<String, String> hashMap = new HashMap<String ,String>();
		hashMap.put("10225502550112270933199102255500", "0");
		String string = hashMap.get("10225502550112270933199102255500");
		System.out.println(string);
	}
}
