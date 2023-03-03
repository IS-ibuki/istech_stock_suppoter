package com.app.istech.Validator.DuplicateValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.istech.Model.Client;
import com.app.istech.Service.ClientService;

public class NotDuplicateClientNumValidator implements ConstraintValidator<NotDuplicateClientNum, String>{
	
	private final ClientService clientService;
	
	@Autowired
	public NotDuplicateClientNumValidator(ClientService clientService) {
		this.clientService = clientService;
	}


	/*
	 * @param value 
	 * 2つめの型パラメータに指定したクラス
	 * @return 検証結果
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		Client client = clientService.findByClientNum(value);
		
		if(client != null) {
			return false;
		}
		
		return true;
	}
	
}