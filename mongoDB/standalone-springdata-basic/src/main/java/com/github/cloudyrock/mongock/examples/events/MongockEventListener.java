package com.github.cloudyrock.mongock.examples.events;

import com.github.cloudyrock.mongock.runner.core.event.result.MigrationResult;

public class MongockEventListener {

  public static void onStart() {
    System.out.println("[EVENT LISTENER] - Mongock STARTED successfully");
  }

  public static void onSuccess(MigrationResult result) {
    System.out.println("[EVENT LISTENER] - Mongock finished successfully");
  }

  public static void onFail(Exception exception) {
    System.out.println("[EVENT LISTENER] - Mongock finished with failures");
  }
}
