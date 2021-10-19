package io.mongock.examples.config;


import io.mongock.examples.events.MongockFailEventListener;
import io.mongock.examples.events.MongockStartedEventListener;
import io.mongock.examples.events.MongockSuccessEventListener;
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
