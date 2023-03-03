package com.app.istech.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.app.istech.Form.CompositionSearchForm;
import com.app.istech.Model.Composition;

@Mapper
public interface CompositionMapper {
	
	public Long count();

	public Long count(@Param("compositionSearchForm")CompositionSearchForm compositionSearchForm);

	public List<Composition> findAll();

	public List<Composition> findByParentId(int parentId);
	
	public void insertComposition(Composition composition);
	
	public void bulkInsertComposition(List<Composition> compositionList);
	
	public List<Composition> findCompositionByProductId(int productId);
	
	public boolean updateComposition(@Param("composition")Composition composition,@Param("previousId")int previousId);

	public boolean deleteComposition(int childId);
	
	public boolean updateCurrentNum(int productId);
	
}
