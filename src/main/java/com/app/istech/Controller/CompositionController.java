package com.app.istech.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.istech.Form.CompositionForm;
import com.app.istech.Form.CompositionFormList;
import com.app.istech.Form.CompositionSearchForm;
import com.app.istech.Form.ProductForm;
import com.app.istech.Form.UploadForm;
import com.app.istech.Form.groups.CreateComposition;
import com.app.istech.Form.groups.UpdateComposition;
import com.app.istech.Model.Client;
import com.app.istech.Model.Composition;
import com.app.istech.Model.Product;
import com.app.istech.Service.ClientService;
import com.app.istech.Service.CompositionService;
import com.app.istech.Service.ProductService;

@Controller
@RequestMapping("/compositions")
@SessionAttributes(types = {UploadForm.class})
public class CompositionController {
	
	private final CompositionService compositionService;
	
	private final ProductService productService;
	
	private final ClientService clientService;
	
	@Autowired
	public CompositionController(CompositionService compositionService,ProductService productService,
			ClientService clientService) {
		this.compositionService = compositionService;
		this.productService = productService;
		this.clientService = clientService;
	}
	
	@ModelAttribute(value="uploadForm")
	public UploadForm setUpUploadForm() {
		UploadForm uploadForm = new UploadForm();
		return uploadForm;
	}
	
	@ModelAttribute(value = "compositionForm")
	public CompositionForm setUpProductForm() {
		CompositionForm compositionForm = new CompositionForm();
		
		List<Product> list = productService.findAll();
		compositionForm.setProductList(list);		
		return compositionForm;
	}
	
	@ModelAttribute(value = "compositionSearchForm")
	public CompositionSearchForm setUpCompositionSearchForm() {
		CompositionSearchForm compositionSearchForm = new CompositionSearchForm();
		return compositionSearchForm;
	}
	
	
	// 製品構成マスタ作成画面
	@RequestMapping(path = "/add", method = RequestMethod.GET)
	public String compositionAdd(Model model, @RequestParam(required=true) Integer parentId) {
		
		CompositionForm compositionForm = new CompositionForm();
		
		if(parentId != null) {
			compositionForm.getParent().setProductId(parentId);
		}
		
		model.addAttribute("compositionForm", compositionForm);
		return "composition/add";
	}
	

	// 製品構成マスタ登録処理
	@RequestMapping(path = "/{parentId}/create", method = RequestMethod.POST)
	public String compositionCreate(Model model,@PathVariable Integer parentId,
			@PageableDefault(size = 20) Pageable pageable,
			@ModelAttribute("compositionSearchForm")CompositionSearchForm compositionSearchForm,
			@ModelAttribute @Validated({CreateComposition.class})CompositionFormList compositionFormList,BindingResult result) {
		
		if(result.hasErrors()) {
			Product product = productService.findPageById(parentId,pageable,compositionSearchForm);
			Long count = compositionService.count(compositionSearchForm,parentId);
			Page<Composition> compositionList = new PageImpl<Composition>(product.getChilds(),pageable,count);
			
			model.addAttribute("product", product);
			model.addAttribute("page", compositionList);
			model.addAttribute("error","error");
			return "product/detail";
		}
		
		List<Composition> compositionList = new ArrayList<Composition>();
		for(CompositionForm form : compositionFormList.getCompositionFormList()){
			Composition composition = new Composition(form);
			// 親製品のIDをセット
			composition.getParent().setProductId(parentId);
			// 入力された品番から製品を検索
			Product child = productService.findBySerialNum(form.getChild().getSerialNum());
			composition.setChild(child);
			compositionList.add(composition);
		}	
		compositionService.bulkInsert(compositionList);	
		
		return "redirect:/products/" + parentId;
	}

	// 製品構成マスタ更新処理
	@RequestMapping(path = "{childId}/update", method = RequestMethod.POST)
	public String compositionUpdate(Model model ,@PathVariable Integer childId,
			@RequestParam(required=true) Integer parentId,
			@ModelAttribute @Validated({UpdateComposition.class}) CompositionForm compositionForm,BindingResult result) {
		
		if(result.hasErrors()) {
			model.addAttribute("compositionForm",compositionForm);
			
			return "composition/edit";
		}
		
		Composition composition = new Composition(compositionForm);
		composition.getParent().setProductId(parentId);
		Product child = productService.findById(childId);
		composition.setChild(child);
		compositionService.updateComposition(composition,childId);
		
		return "redirect:/products/" + parentId;
	}

	// 製品構成マスタ編集画面
	@RequestMapping(path = "/{childId}/edit", method = RequestMethod.GET)
	public String compositionEdit(Model model ,@PathVariable Integer childId ,
			@ModelAttribute("compositionForm") CompositionForm compositionForm,@RequestParam(required=true) Integer parentId) {
		
		Product parent = productService.findById(parentId);
		Product child = productService.findById(childId);
		
		compositionForm.setParent(new ProductForm(parent));
		compositionForm.setChild(new ProductForm(child));
		
		return "composition/edit";
	}
	
	
	/*
	 *  製品マスタ削除処理
	 */
	@RequestMapping(path = "/{childId}/delete", method = RequestMethod.GET)
	public String productPermanentDelete(Model model, @PathVariable("childId") Integer childId,@RequestParam(required=true) Integer parentId) {
		compositionService.deleteComposition(childId);
		return "redirect:/products/" + parentId;
	}

	
	// 製品構成マスタ一括作成処理
	@RequestMapping(path = "/bulk-insert", method = RequestMethod.POST)
	public String compositionBulkInsert(Model model,RedirectAttributes redirectAttributes,
			HttpSession httpSession,SessionStatus sessionStatus,
			@ModelAttribute @Validated({CreateComposition.class}) UploadForm uploadForm,BindingResult result) {
		
		if(result.hasErrors()) {
			return "composition/bulkInsert";
		}
		
		try {
			// 製品の一括登録
			if(!uploadForm.getProductList().isEmpty()) {
				List<Product> productList = new ArrayList<Product>();
				for(ProductForm productForm : uploadForm.getProductList()) {
					Product product = new Product(productForm);	
					Client client = clientService.findByClientNum(productForm.getClientForm().getClientNum());
					product.setClient(client);
					productList.add(product);
				}
				productService.bulkInsert(productList);				
			}
			// 構成品の登録
			if(!uploadForm.getCompositionList().isEmpty()) {
				
				List<Composition> compositionList = new ArrayList<Composition>();
				for(CompositionForm compositionForm : uploadForm.getCompositionList()) {
					Composition composition = new Composition(compositionForm);
					
					//親製品のセット
					String parentSerialNum = compositionForm.getParent().getSerialNum();
					Product parent = productService.findBySerialNum(parentSerialNum);
					composition.setParent(parent);
					
					//子製品のセット
					String childSerialNum = compositionForm.getChild().getSerialNum();
					Product child = productService.findBySerialNum(childSerialNum);
					composition.setChild(child);
					
					compositionList.add(composition);
				}
				
				compositionService.bulkInsert(compositionList);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return "composition/bulkInsert";
		}
		
		// セッション管理()
		httpSession.removeAttribute("uploadForm");
		httpSession.invalidate();
		sessionStatus.setComplete();
		
		redirectAttributes.addFlashAttribute("status", "success");
		return "redirect:/products";
	
	
	}
	
	
	// 製品マスタ一括作成結果画面
	@RequestMapping(path = "/add/bulk", method = RequestMethod.POST)
	public String compositionBulkResult(Model model,@ModelAttribute UploadForm uploadForm) {
		
		List<CSVRecord> resultSet = new ArrayList<CSVRecord>();
		
		try {
			resultSet = CompositionService.parseCSV(uploadForm.getFile(), "Shift-JIS");
		} catch (IOException e) {
			e.printStackTrace();
			return "composition/bulkInsert";
		}
		
		List<CompositionForm> resultFormList = CompositionService.csvRecordToFormList(resultSet);
		uploadForm.setCompositionList(resultFormList);
		return "composition/bulkInsert";
	}
	
	
	// 製品構成マスタ一括作成画面
	@RequestMapping(path = "/add/bulk", method = RequestMethod.GET)
	public String compositionBulkAdd(Model model,@ModelAttribute UploadForm uploadForm) {
		model.addAttribute("uploadForm", uploadForm);
		return "composition/bulkInsert";
	}



}
