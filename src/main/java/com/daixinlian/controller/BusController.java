package com.daixinlian.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.daixinlian.service.ExportService;

import cn.hutool.extra.mail.MailUtil;

@Controller
public class BusController {

	@Autowired
	ExportService exportService;
	
	@RequestMapping("/upload")
	@ResponseBody
	public String uplpad(MultipartFile file ){
		try {
		
		//检查案件
		exportService.export(null,file);
		
		} catch (Exception e) {
			return e.getMessage();
		}
		return "上传成功";
	}
	
	
	
	
	@RequestMapping("/index")
	public String index(){
		
		return "index";
	}
	
	@PostMapping("/senderror")
	@ResponseBody
	public String  sendmeil(String flag,String errorMsg,String emaiaccount){
		
		MailUtil.send(emaiaccount, flag+"错误日志"+new Date().toString(), errorMsg, false);
		return "0";
	}
}
