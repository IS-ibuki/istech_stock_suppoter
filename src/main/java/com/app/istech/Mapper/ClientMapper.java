package com.app.istech.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;

import com.app.istech.Model.Client;

@Mapper
public interface ClientMapper {
	
	Long count();

	List<Client> findAll();
	
	List<Client> findAll(RowBounds rowBounds);
	
	Client findById(Integer clientId);

	Client findPageById(@Param("clientId")Integer clientId,@Param("pageable")Pageable pageable);
	
	Client findByClientNum(String clientNum);

	boolean deleteClient(Integer clientId);
	
	// 顧客表示切り替え
	void updateDeletedFlg(Client client);

	boolean insertClient(Client client);

	boolean updateClient(Client client);

}
