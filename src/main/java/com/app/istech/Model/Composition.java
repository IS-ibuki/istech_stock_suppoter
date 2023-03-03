package com.app.istech.Model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.app.istech.Form.CompositionForm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Composition {
	
	private Integer parentId;

	private Integer childId;
	
	//親品番
	private Product parent;
	
	//品番
	private Product child;
	
	//入数
	private Integer quantity;
	
	//子仕入先リスト
	List<Client> clients;
	
	//子製品構成マスタリスト
	private List<Composition> childs; 
	
	private Timestamp createTs;
	
	private Timestamp updateTs;
	
	public Composition(CompositionForm compositionForm) {
		this.quantity = Integer.valueOf(compositionForm.getQuantity());
		this.parent = new Product();
		this.child = new Product();
	}
	
	public Composition(Map<String,String> map) {
		this.parent = new Product();
		this.child = new Product();
		
		this.parent.setSerialNum(map.get("parentSerialNum"));
		this.child.setSerialNum(map.get("childSerialNum"));
		this.quantity = Integer.valueOf(map.get("quantity"));
	}
	
}
