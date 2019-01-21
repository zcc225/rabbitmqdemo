package com.daixinlian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * 
 * @author zhangchangchao
 *
 */
@EnableTransactionManagement   
@ComponentScan(basePackages = {"com.daixinlian"})
@SpringBootApplication
public class RabbitdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitdemoApplication.class, args);
	}
}

