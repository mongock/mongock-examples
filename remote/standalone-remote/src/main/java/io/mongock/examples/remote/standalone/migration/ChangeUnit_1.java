package io.mongock.examples.remote.standalone.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id = "change-unit-1", order = "1")
public class ChangeUnit_1 {

	@Execution
	public void execution(Boolean throwException) {
		if(throwException) {
			System.out.println("EXECUTION : throwing exception");
			throw new RuntimeException("planned exception");
		} else {
			System.out.println("EXECUTION : normal execution");
		}
	}

	@RollbackExecution
	public void beforeExecution() {
		System.out.println("ROLLBACK EXECUTION");
	}
}
