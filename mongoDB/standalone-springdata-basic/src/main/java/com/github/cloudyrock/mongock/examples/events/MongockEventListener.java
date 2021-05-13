package com.github.cloudyrock.mongock.examples.events;

import com.github.cloudyrock.standalone.event.StandaloneMigrationFailureEvent;
import com.github.cloudyrock.standalone.event.StandaloneMigrationSuccessEvent;

public class MongockEventListener {

  public static void onStart() {
    System.out.println("[EVENT LISTENER] - Mongock STARTED successfully");
  }

  public static void onSuccess(StandaloneMigrationSuccessEvent event) {
    System.out.println("[EVENT LISTENER] - Mongock finished successfully");
  }

  public static void onFail(StandaloneMigrationFailureEvent event) {
    System.out.println("[EVENT LISTENER] - Mongock finished with failures");
  }
}
