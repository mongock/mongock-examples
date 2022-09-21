package io.mongock.examples.mongodb.springboot.advance.config;


import io.mongock.examples.mongodb.springboot.advance.events.MongockFailEventListener;
import io.mongock.examples.mongodb.springboot.advance.events.MongockStartedEventListener;
import io.mongock.examples.mongodb.springboot.advance.events.MongockSuccessEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public MongockStartedEventListener mongockStartedEventListener() {
		return new MongockStartedEventListener();
	}

	@Bean
	public MongockSuccessEventListener mongockSuccessEventListener() {
		return new MongockSuccessEventListener();
	}

	@Bean
	public MongockFailEventListener mongockFailEventListener() {
		return new MongockFailEventListener();
	}

}
