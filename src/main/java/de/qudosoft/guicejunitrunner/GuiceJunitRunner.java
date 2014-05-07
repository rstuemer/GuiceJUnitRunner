/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Qudosoft
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.qudosoft.guicejunitrunner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.google.inject.Injector;
import com.google.inject.Module;

import de.qudosoft.guicejunitrunner.annotation.Guice;

/**
 * 
 *
 */
public class GuiceJunitRunner extends BlockJUnit4ClassRunner {

	private static final Logger LOGGER = Logger
			.getLogger(GuiceJunitRunner.class.getName());
	private final Injector injector;

	/**
	 * Initialize the Runner with actual test class
	 * 
	 * @param clazz
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
	 * @param guice
	 *            Annotation
	 * @param testClass
	 * @return
	 */
	private Module[] getModules(Guice guice, Class<?> testClass) {
		List<Module> result = new ArrayList<Module>();
		for (Class<? extends Module> moduleClass : guice.modules()) {
			Module instance;
			try {
				instance = moduleClass.newInstance();
				LOGGER.info("Create Instance for Module:"
						+ instance.getClass().getName());
				result.add(instance);
			} catch (InstantiationException e) {
				LOGGER.log(Level.WARNING, "Error while instantiate module:"
						+ moduleClass.getName(), e);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				LOGGER.log(
						Level.WARNING,
						"Error while instantiate module class or its nullary constructor is not accessible: "
								+ moduleClass.getName(), e);
				e.printStackTrace();
			}

		}
		return result.toArray(new Module[result.size()]);
	}

	/**
	 * 
	 * Create an Injector with the given modules
	 * 
	 * @param modules
	 * @return
	 */
	private Injector createInjectorFor(Module[] modules) {
		return com.google.inject.Guice.createInjector(modules);

	}

	// TODO: move to Utils
	private static Annotation findAnnotationSuperClasses(Class annotationClass,
			Class clazzToFindAnnotation) {
		
		Class clazz = clazzToFindAnnotation;
		Annotation result = null;
		while (clazz != null && result == null) {
			 result = clazz.getAnnotation(annotationClass);
			if (result == null) {
				clazz = clazz.getSuperclass();
			}
		}
		return result;
	}

}
