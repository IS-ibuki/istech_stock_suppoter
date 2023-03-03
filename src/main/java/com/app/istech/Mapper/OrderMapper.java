package com.app.istech.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.app.istech.Form.OrderSearchForm;
import com.app.istech.Model.Order;
import com.app.istech.Model.Product;

@Mapper
public interface OrderMapper {
	
	//注文書全件数
	Long count(@Param("orderSearchForm") OrderSearchForm orderSearchForm);

	List<Order> findAll(); 

	List<Order> findAll(@Param("rowBounds")RowBounds rowBounds,
			@Param("orderSearchForm")OrderSearchForm orderSearchForm);
	
	List<Product> findAllBySearchForm(@Param("rowBounds")RowBounds rowBounds,
			@Param("orderSearchForm")OrderSearchForm orderSearchForm);
	
	Order findById(Integer orderId);
	
	Order findByOrderNum(String orderNum);

	void insertOrder(Order order);

	boolean deleteOrder(Integer orderId);

	boolean updateOrder(Order order);
	
	// 注文表示切り替え
	boolean updateDeletedFlg(Order order);


}
