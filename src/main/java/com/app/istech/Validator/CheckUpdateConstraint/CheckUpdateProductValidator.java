package com.app.istech.Validator.CheckUpdateConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.istech.Model.Product;
import com.app.istech.Service.ProductService;

public class CheckUpdateProductValidator implements ConstraintValidator<CheckUpdateProduct, Object>{

    private String id;
    
    private String serialNum;
    
    private String message;
    
    private final ProductService productService;
    
    @Autowired
    public CheckUpdateProductValidator(ProductService productService) {
    	this.productService = productService;
    }
 
    public void initialize(CheckUpdateProduct constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.serialNum = constraintAnnotation.serialNum();
        this.message = constraintAnnotation.message();
    }

    @Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// 2つのプロパティ値を取得
		BeanWrapper beanWrapper = new BeanWrapperImpl(value);
		Object value1 = beanWrapper.getPropertyValue(id);
		Object value2 = beanWrapper.getPropertyValue(serialNum);
		Product product = productService.findBySerialNum(value2.toString());
		if(product == null) {
			return true;
		}else if(product.getProductId().equals(Integer.valueOf(value1.toString()))) {
			return true;
		}else {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(serialNum).addConstraintViolation();
			return false;
		}
	}

}
