package de.qudosoft.guicejunitrunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import de.qudosoft.guicejunitrunner.annotation.Guice;
import de.qudosoft.guicejunitrunner.testClasses.ITestClass;
import de.qudosoft.guicejunitrunner.testClasses.TestModule;



@RunWith(GuiceJunitRunner.class)
@Guice(modules = {TestModule.class})
public class GuiceJunitRunnerTest {

	
	@Inject
	private ITestClass testClass;
	
	@Test
	public void testInjection(){
		Assert.assertNotNull(testClass);
	}
	
	
}
