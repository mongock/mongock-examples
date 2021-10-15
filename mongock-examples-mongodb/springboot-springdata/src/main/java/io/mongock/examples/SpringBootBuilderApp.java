package io.mongock.examples;

import io.mongock.examples.config.MongockAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
//This is only added to deactivate the Mongock autoconfiguration class. It shouldn't be needed in a real scenario
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {MongockAutoConfiguration.class, SpringBootAutoconfigurationApp.class}))
public class SpringBootBuilderApp {


    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SpringBootBuilderApp.class).run(args);
    }

}
