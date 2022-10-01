package io.mongock.examples.remote.standalone;

import io.mongock.driver.remote.driver.RemoteDriver;
import io.mongock.runner.standalone.MongockStandalone;

public class RemoteStandaloneApp {

	public static void main(String[] args) {

		MongockStandalone.builder()
				.setDriver(RemoteDriver.withDefaultLock("organization", "service", "http://localhost:8080"))
				.addMigrationScanPackage("io.mongock.examples.remote.standalone.migration")
				.addDependency(args.length > 0)//throws exception if any param is passed
				.buildRunner()
				.execute();

	}
}
