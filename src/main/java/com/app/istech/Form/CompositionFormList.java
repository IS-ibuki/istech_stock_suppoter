package com.app.istech.Form;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompositionFormList {
	
	@Valid
	private List<CompositionForm> compositionFormList;

	
}
