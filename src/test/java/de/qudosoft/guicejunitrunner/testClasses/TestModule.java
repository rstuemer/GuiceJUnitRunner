package de.qudosoft.guicejunitrunner.testClasses;

import com.google.inject.Binder;
import com.google.inject.Module;

public class TestModule implements Module{

	@Override
	public void configure(Binder binder) {
		binder.bind(ITestClass.class).toInstance(new TestClass());
		
	}

}
