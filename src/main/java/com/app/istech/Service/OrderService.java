package com.app.istech.Service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.istech.Form.OrderSearchForm;
import com.app.istech.Mapper.OrderMapper;
import com.app.istech.Model.Order;

@Service
public class OrderService {

	private final OrderMapper orderMapper;
	
	@Autowired
	public OrderService(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}
	
	public Long count(OrderSearchForm orderSearchForm,Integer clientId) {
		orderSearchForm.setClientId(clientId);
		return orderMapper.count(orderSearchForm);
	}
	
	public List<Order> findAll(){
		List<Order> orderList = orderMapper.findAll();
		return orderList;
	}

	public Page<Order> findAll(Pageable pageable,OrderSearchForm orderSearchForm) {
		RowBounds rowBounds = new RowBounds((int)pageable.getOffset(),pageable.getPageSize());
		List<Order> orderList = orderMapper.findAll(rowBounds,orderSearchForm);
		Long total = orderMapper.count(orderSearchForm);
		return new PageImpl<Order>(orderList,pageable,total);
	}
	public Page<Order> findPageByClientId(Pageable pageable,OrderSearchForm orderSearchForm,Integer clientId) {
		RowBounds rowBounds = new RowBounds((int)pageable.getOffset(),pageable.getPageSize());
		orderSearchForm.setClientId(clientId);
		List<Order> orderList = orderMapper.findAll(rowBounds,orderSearchForm);
		Long total = orderMapper.count(orderSearchForm);
		return new PageImpl<Order>(orderList,pageable,total);
	}
	
	public Order findById(Integer orderId) {
		return orderMapper.findById(orderId);
	}

	public Order findByOrderNum(String orderNum) {
		return orderMapper.findByOrderNum(orderNum);
	}

	public void insertOrder(Order order) {
		orderMapper.insertOrder(order);
	}

	public boolean deleteOrder(Integer orderId) {
		return orderMapper.deleteOrder(orderId);
	}

	public boolean updateOrder(Order order) {
		return orderMapper.updateOrder(order);
		
	}
	
	// 注文表示切り替え
	public void updateDeletedFlg(Order order) {
		order.setDeleted(!order.isDeleted());
		orderMapper.updateDeletedFlg(order);
	}
	
	
}
