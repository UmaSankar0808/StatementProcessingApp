package com.xyzbank.customerstatementrecords.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.xyzbank.customerstatementrecords.configuration.ApplicationConfig;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AuditFolderCleanUp {

	private ApplicationConfig config;

	@Scheduled(cron = "${config.purgeCronExpression}")
	public void purgeFiles() {
		purgeFiles(new File(config.getAuditFolderPath()),
		        config.getPrugeFilesPeriodInMillis());
	}

	public void purgeFiles(final File file, final long purgeTime) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File aFile : files) {
				purgeFiles(aFile, purgeTime);
			}
		} else {
			log.info(file.toString());
			log.info(new Date(file.lastModified()).toString());
			if (file.lastModified() < System.currentTimeMillis() - purgeTime) {
			    log.info("Deleting file: {} ", file);
			    try {
                    Files.deleteIfExists(file.toPath());
                } catch (IOException exp) {
                    log.error(exp.getMessage(), exp);
                }
			}
		}
	}

}
