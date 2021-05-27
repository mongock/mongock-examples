package com.github.cloudyrock.mongock.examples.events;

import com.github.cloudyrock.mongock.runner.core.event.MigrationFailureEvent;
import com.github.cloudyrock.mongock.runner.core.event.MigrationStartedEvent;
import com.github.cloudyrock.mongock.runner.core.event.MigrationSuccessEvent;

public class MongockEventListener {

  public static void onStart(MigrationStartedEvent event) {
    System.out.println("[EVENT LISTENER] - Mongock STARTED successfully");
  }

  public static void onSuccess(MigrationSuccessEvent event) {
    System.out.println("[EVENT LISTENER] - Mongock finished successfully");
  }

  public static void onFail(MigrationFailureEvent event) {
    System.out.println("[EVENT LISTENER] - Mongock finished with failures");
  }
}
