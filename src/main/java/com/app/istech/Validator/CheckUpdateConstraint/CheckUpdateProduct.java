package com.app.istech.Validator.CheckUpdateConstraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {CheckUpdateProductValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
public @interface CheckUpdateProduct {
	
	String message() default "{com.app.istech.Validator.DuplicateValidator}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default{};
	
	String id();
	String serialNum();
	
	@Target({ ElementType.TYPE,ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface List{
		CheckUpdateProduct[] values();
	}

	
}
