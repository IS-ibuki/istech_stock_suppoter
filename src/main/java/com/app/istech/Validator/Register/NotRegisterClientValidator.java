package com.app.istech.Validator.Register;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.istech.Model.Client;
import com.app.istech.Service.ClientService;

public class NotRegisterClientValidator implements ConstraintValidator<NotRegisterClient, String>{
	
	private final ClientService clientService;
	
	@Autowired
	public NotRegisterClientValidator(ClientService clientService) {
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
		
		if(client == null) {
			return false;
		}
		
		return true;
	}
	
}
