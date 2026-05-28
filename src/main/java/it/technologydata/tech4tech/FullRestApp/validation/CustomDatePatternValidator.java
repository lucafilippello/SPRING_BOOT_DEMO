package it.technologydata.tech4tech.FullRestApp.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomDatePatternValidator implements ConstraintValidator<CustomDatePattern, CharSequence> {
	
    private SimpleDateFormat simpleDateFormat;

    @Override
    public void initialize(CustomDatePattern annotation) {
    	simpleDateFormat = new SimpleDateFormat(annotation.dateFormat());
    	simpleDateFormat.setLenient(false);
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        boolean isValid = true;
        try {
			simpleDateFormat.parse(value.toString());
		} catch (ParseException e) {
			isValid = false;
		}
    	
    	return isValid;
    }
}
