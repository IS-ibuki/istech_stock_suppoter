package com.app.istech.Validator.Register;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.istech.Model.Product;
import com.app.istech.Service.ProductService;

public class NotRegisterProductValidator implements ConstraintValidator<NotRegisterProduct, String>{
	
	private final ProductService productService;
	
	@Autowired
	public NotRegisterProductValidator(ProductService productService) {
		this.productService = productService;
	}


	/**
	 * データが登録されていないことを検証する
	 * @param value 
	 * 2つめの型パラメータに指定したクラス
	 * @return 検証結果
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		Product product = productService.findBySerialNum(value);
		
		if(product == null) {
			return false;
		}
		
		return true;
	}
	
}
