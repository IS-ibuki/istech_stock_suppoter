package com.app.istech.Form;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.web.context.annotation.SessionScope;

import com.app.istech.Form.groups.CreateComposition;
import com.app.istech.Form.groups.UpdateComposition;
import com.app.istech.Model.Client;
import com.app.istech.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SessionScope
public class CompositionForm {
	
	//親品番
	@Valid
	private ProductForm parent;
	
	//品番
	@Valid
	private ProductForm child;
	
	private String parentSerialNum;

	private String childSerialNum;
	
	//入数
	@PositiveOrZero(groups = {CreateComposition.class,UpdateComposition.class})
	@NotBlank(groups = {CreateComposition.class,UpdateComposition.class})
	private String quantity;
	
	private List<Product> productList;
	
	//子仕入先リスト
	List<Client> clients;
	
	//子製品マスタ
	private Product product;
	
	public CompositionForm(Integer parentId) {
		this.parent.setProductId(parentId);
	}
	
	public CompositionForm(Map<String,String> map) {
		this.parent = new ProductForm();
		this.child = new ProductForm();
		
		this.parent.setSerialNum(map.get("parentSerialNum"));
		this.child.setSerialNum(map.get("childSerialNum"));
		this.quantity = map.get("quantity");
	}
	
	
}
