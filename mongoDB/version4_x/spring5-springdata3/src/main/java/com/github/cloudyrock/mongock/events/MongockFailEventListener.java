package com.github.cloudyrock.mongock.events;

import com.github.cloudyrock.spring.util.events.SpringMigrationFailureEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MongockFailEventListener implements ApplicationListener<SpringMigrationFailureEvent> {

    @Override
    public void onApplicationEvent(SpringMigrationFailureEvent event) {
        System.out.println("[EVENT LISTENER] - Mongock finished with failures");
    }

}
