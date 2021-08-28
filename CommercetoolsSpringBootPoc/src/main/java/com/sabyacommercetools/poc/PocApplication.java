package com.sabyacommercetools.poc;


import com.sabyacommercetools.marut.controllers.merchant.ProductImporter;
import com.sabyacommercetools.marut.controllers.merchant.ProductVariantImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.FileNotFoundException;
import java.util.concurrent.*;

@SpringBootApplication(scanBasePackages = "com.sabyacommercetools")
public class PocApplication {

	@Autowired
	private Environment env;


	public static void main(String[] args) throws FileNotFoundException, ExecutionException, InterruptedException {

		ConfigurableApplicationContext context = SpringApplication.run(PocApplication.class, args);

		context.getBean(ProductImporter.class).importProducts();
//		context.getBean(ProductVariantImporter.class).importProductVariants();
	}
}
