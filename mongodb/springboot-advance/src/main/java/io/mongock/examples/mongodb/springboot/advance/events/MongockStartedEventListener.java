package io.mongock.examples.mongodb.springboot.advance.events;

import io.mongock.runner.spring.base.events.SpringMigrationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

public class MongockStartedEventListener implements ApplicationListener<SpringMigrationStartedEvent> {

    @Override
    public void onApplicationEvent(SpringMigrationStartedEvent event) {
        System.out.println("[EVENT LISTENER] - Mongock STARTED successfully");
    }

}
