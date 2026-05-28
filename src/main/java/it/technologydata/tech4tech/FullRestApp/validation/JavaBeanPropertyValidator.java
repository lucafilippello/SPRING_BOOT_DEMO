package it.technologydata.tech4tech.FullRestApp.validation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class JavaBeanPropertyValidator implements ConstraintValidator<JavaBeanProperty, CharSequence> {
	
    private Set<String> acceptedValues;

    @Override
    public void initialize(JavaBeanProperty annotation) {
    	
    	acceptedValues = new HashSet<>();
    	
    	try {
		    BeanInfo bi = Introspector.getBeanInfo(annotation.beanClass());
		    PropertyDescriptor[] pds = bi.getPropertyDescriptors();
		    for (PropertyDescriptor descriptor : pds) {
		    	String propName = descriptor.getName();
		        if(!"class".equals(propName)) {
		        	acceptedValues.add(propName.toUpperCase());
		        }
		    }
		} catch (IntrospectionException e) {
		}
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    	
        boolean isValid = true;
    	if (value == null) {
    		isValid = false;
        } else {
        	isValid = acceptedValues.contains(value.toString().toUpperCase());
        }
    	
    	return isValid;
    }
}
