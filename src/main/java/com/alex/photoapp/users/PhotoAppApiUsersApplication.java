package com.alex.photoapp.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * this will make this application a client of a Eureka discover service.
 * Also add some stuff in app.properties (see the properties file)
 *
 * This application PhotoAppApiUsersApplication and
 *  * PhotoAppApiAccountManagementApplication are both clients for EnableDiscoveryClient (Eureka server => PhotoAppDiscoveryServiceApplication )
 *
 * 1. First start Eureka Discovery server ( PhotoAppDiscoveryServiceApplication.java app)
 * 		Open a browser and type => http://localhost:8010
 *
 * 	To run SpringBoot application from a command line using maven
 * 	-------------------------------------
 * 	mvn spring-boot:run -Dspring-boot.run.arguments=--spring.application.instance-id=Alex
 */
@SpringBootApplication
@EnableDiscoveryClient
//@EnableConfigurationProperties
public class PhotoAppApiUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiUsersApplication.class, args);
	}


}
