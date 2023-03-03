package com.app.istech.Validator.CheckUpdateConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.istech.Model.Client;
import com.app.istech.Service.ClientService;

public class CheckUpdateClientValidator implements ConstraintValidator<CheckUpdateClient, Object>{

    private String id;
    
    private String clientNum;
    
    private String message;
    
    private final ClientService clientService;
    
    @Autowired
    public CheckUpdateClientValidator(ClientService clientService) {
    	this.clientService = clientService;
    }
 
    public void initialize(CheckUpdateClient constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.clientNum = constraintAnnotation.clientNum();
        this.message = constraintAnnotation.message();
    }

    @Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// 2つのプロパティ値を取得
		BeanWrapper beanWrapper = new BeanWrapperImpl(value);
		Object value1 = beanWrapper.getPropertyValue(id);
		Object value2 = beanWrapper.getPropertyValue(clientNum);
		Client client = clientService.findByClientNum(value2.toString());
		if(client == null) {
			return true;
		}else if(client.getClientId() == Integer.valueOf(value1.toString())) {
			return true;
		}else {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(clientNum).addConstraintViolation();
			return false;
		}
	}

}