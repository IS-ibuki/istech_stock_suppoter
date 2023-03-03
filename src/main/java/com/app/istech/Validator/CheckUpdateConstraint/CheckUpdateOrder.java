package com.app.istech.Validator.CheckUpdateConstraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {CheckUpdateOrderValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
public @interface CheckUpdateOrder {
	
	String message() default "{com.app.istech.Validator.CheckUpdateValidator}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default{};
	
	String id();
	String orderNum();
	
	@Target({ ElementType.TYPE,ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface List{
		CheckUpdateOrder[] values();
	}
	
}
