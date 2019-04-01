package com.xyzbank.customerstatementrecords.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "config")
public class ApplicationConfig {

	private Integer pollingInterval;
	private Integer inputCoolingOffPeriod;
	private String inputFolderPath;
	private String errorFolderPath;
	private String processedFolderPath;
	private String auditFolderPath;

}
