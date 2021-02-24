package com.github.cloudyrock.mongock.events;

import com.github.cloudyrock.spring.util.events.SpringMigrationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MongockStartedEventListener implements ApplicationListener<SpringMigrationStartedEvent> {

    @Override
    public void onApplicationEvent(SpringMigrationStartedEvent event) {
        System.out.println("[EVENT LISTENER] - Mongock STARTED successfully");
    }

}
