package com.app.istech.Validator.Register;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = NotRegisterProductValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface NotRegisterProduct {
	
	String message() default "{com.app.istech.Validator.registerValidator.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    @Target({ ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface List{
		NotRegisterProduct[] values();
	}
	
}