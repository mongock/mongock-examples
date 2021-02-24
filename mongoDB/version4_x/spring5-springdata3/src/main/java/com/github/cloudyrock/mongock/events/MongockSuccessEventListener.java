package com.github.cloudyrock.mongock.events;

import com.github.cloudyrock.spring.util.events.SpringMigrationSuccessEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MongockSuccessEventListener implements ApplicationListener<SpringMigrationSuccessEvent> {

    @Override
    public void onApplicationEvent(SpringMigrationSuccessEvent event) {
        System.out.println("[EVENT LISTENER] - Mongock finished successfully");
    }

}
