package de.qudosoft.guicejunitrunner.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 
 *
 */
public class GuiceJunitRunner extends BlockJUnit4ClassRunner {
	
	private static final Logger LOGGER = Logger.getLogger(GuiceJunitRunner.class.getName());
	private final Injector injector;
	
	/**
	 * @param klass
	 * @throws InitializationError
	 */
	public GuiceJunitRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		Annotation annotation = findAnnotationSuperClasses(Guice.class, clazz);
		Guice guice = (Guice) annotation;
		Module[] modules = getModules(guice, clazz);
		injector = createInjectorFor(modules);
	}
	
	
	@Override
	public Object createTest() throws Exception {
		Object obj = super.createTest();
		injector.injectMembers(obj);
		return obj;
	}
	
	
	/**
	 * Returns the Modules defined on TestClass with {@link Guice} Annotation
	 * 
	 * @param guice Annotation
	 * @param testClass
	 * @return
	 */
	private Module[] getModules(Guice guice, Class<?> testClass) {
		List<Module> result = new ArrayList<Module>();
		for (Class<? extends Module> moduleClass : guice.modules()) {
			Module instance;
			try {
				instance = moduleClass.newInstance();
				LOGGER.info("Create Instance for Module:" + instance.getClass().getName());
				result.add(instance);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		return result.toArray(new Module[result.size()]);
	}
	
	/**
	 * 
	 * Create an Injctor with the given modules
	 * 
	 * @param modules
	 * @return
	 */
	private Injector createInjectorFor(Module[] modules) {
		return com.google.inject.Guice.createInjector(modules);
		
	}
	
	
	// TODO: move to Utils
	private static Annotation findAnnotationSuperClasses(Class annotationClass, Class c) {
		while (c != null) {
			Annotation result = c.getAnnotation(annotationClass);
			if (result != null) {
				return result;
			}
			else {
				c = c.getSuperclass();
			}
		}
		return null;
	}
	
	
}
