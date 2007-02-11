package org.objenesis.instantiator.sun;

import java.lang.reflect.InvocationTargetException;

/**
 * Instantiates a class by making a call to internal Sun private methods.
 * It is only supposed to work on Sun HotSpot 1.3 JVM.
 * This instantiator will not call any constructors.
 * 
 * @author Leonardo Mesquita
 * @see org.objenesis.instantiator.ObjectInstantiator
 */
public class Sun13Instantiator extends Sun13InstantiatorBase {
	public Sun13Instantiator(Class type) {
		super(type);
	}
	
	public Object newInstance() {
		if (allocateNewObjectMethod == null) {
			return null;
		}
		try {
			return allocateNewObjectMethod.invoke(null, new Object[] {type, Object.class});		
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
	}
	
	
}
