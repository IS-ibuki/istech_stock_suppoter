package com.app.istech.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.istech.Form.OrderSearchForm;
import com.app.istech.Mapper.ClientMapper;
import com.app.istech.Model.Client;

@Service
public class ClientService {

	@Autowired
	ClientMapper clientMapper;
	
	public List<Client> findAll() {
		return clientMapper.findAll();
	}

	
	public Page<Client> findAll(Pageable pageable) {
		RowBounds rowBounds = new RowBounds((int)pageable.getOffset(),pageable.getPageSize());
		List<Client> clientList = clientMapper.findAll(rowBounds);
		Long total = clientMapper.count();
		return new PageImpl<Client>(clientList,pageable,total);
	}

	
	public Client findById(Integer clientId) {
		Client client = clientMapper.findById(clientId);
		return client;
	}

	
	public Client findPageById(Integer clientId,Pageable pageable,OrderSearchForm orderSearchForm) {
		orderSearchForm.setClientId(clientId);
		Client client = clientMapper.findPageById(clientId,pageable);
		return client;
	}
	

	public Client findByClientNum(String clientNum) {
		Client client = clientMapper.findByClientNum(clientNum);
		return client;
	}
	
	@Transactional
	public boolean deleteClient(Integer clientId) {
		return clientMapper.deleteClient(clientId);
	}

	@Transactional
	public boolean updateClient(Client client) {
		return clientMapper.updateClient(client);
	}
	
	@Transactional
	public void updateDeletedFlg(Client client) {
		client.setDeleted(!client.isDeleted());
		clientMapper.updateDeletedFlg(client);
	}

	@Transactional
	public void insertClient(Client client) throws DuplicateKeyException{
		clientMapper.insertClient(client);
	}

}
