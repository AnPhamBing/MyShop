package com.bing.icommerce.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class EcommerceCoreApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(EcommerceCoreApplication.class);

	public static void main(String[] args) throws UnknownHostException {
		final ConfigurableApplicationContext applicationContext = SpringApplication.run(EcommerceCoreApplication.class, args);
		final ConfigurableEnvironment env = applicationContext.getEnvironment();
		LOGGER.info(
				"\n----------------------------------------------------------\n"
					+ "\tApplication '{}' is running!\n"
					+ "\tHost: {}; Port: {}; Management port {}.\n"
					+ "\tWith Timezone: {} ({}).\n"
					+ "----------------------------------------------------------\n",
				env.getProperty("spring.application.name"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"),
				env.getProperty("management.port"),
				TimeZone.getDefault().getDisplayName(),
				TimeZone.getDefault().getID()
		);
	}

}
