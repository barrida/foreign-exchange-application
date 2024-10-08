package com.openpayd.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ForeignExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForeignExchangeApplication.class, args);
	}

}
