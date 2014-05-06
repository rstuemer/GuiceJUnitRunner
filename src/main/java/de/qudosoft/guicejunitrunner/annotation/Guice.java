package de.qudosoft.guicejunitrunner.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.Module;

/**
 * This annotation specifies what Guice modules should be used.
 * */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface Guice {
	/**
	 * @return the list of modules
	 */
	Class<? extends Module>[] modules () default {};
}
