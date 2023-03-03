package com.app.istech.Validator.DuplicateValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.istech.Model.Order;
import com.app.istech.Service.OrderService;

public class NotDuplicateOrderNumValidator implements ConstraintValidator<NotDuplicateOrderNum, String>{
	
	private final OrderService orderService;
	
	@Autowired
	public NotDuplicateOrderNumValidator(OrderService orderService) {
		this.orderService = orderService;
	}


	/*
	 * @param value 
	 * 2つめの型パラメータに指定したクラス
	 * @return 検証結果
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		Order order = orderService.findByOrderNum(value);
		
		if(order != null) {
			return false;
		}
		
		return true;
	}
	
}