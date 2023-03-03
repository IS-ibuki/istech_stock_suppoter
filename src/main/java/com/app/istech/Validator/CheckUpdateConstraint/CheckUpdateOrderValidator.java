package com.app.istech.Validator.CheckUpdateConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.istech.Model.Order;
import com.app.istech.Service.OrderService;

public class CheckUpdateOrderValidator implements ConstraintValidator<CheckUpdateOrder, Object>{

    private String id;
    
    private String orderNum;
    
    private String message;
    
    private final OrderService orderService;
    
    @Autowired
    public CheckUpdateOrderValidator(OrderService orderService) {
    	this.orderService = orderService;
    }
 
    public void initialize(CheckUpdateOrder constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.orderNum = constraintAnnotation.orderNum();
        this.message = constraintAnnotation.message();
    }

    @Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// 2つのプロパティ値を取得
		BeanWrapper beanWrapper = new BeanWrapperImpl(value);
		Object value1 = beanWrapper.getPropertyValue(id);
		Object value2 = beanWrapper.getPropertyValue(orderNum);
		Order order = orderService.findByOrderNum(value2.toString());
		if(order == null) {
			return true;
		}else if(order.getOrderId() == Integer.valueOf(value1.toString())) {
			return true;
		}else {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(orderNum).addConstraintViolation();
			return false;
		}
	}

}
