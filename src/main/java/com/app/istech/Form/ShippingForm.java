package com.app.istech.Form;

import java.util.List;

import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SessionScope
public class ShippingForm {
	
	List<ProductForm> shippingProductList;
	
}
