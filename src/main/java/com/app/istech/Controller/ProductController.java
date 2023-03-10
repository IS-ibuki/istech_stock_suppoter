package com.app.istech.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.istech.Exception.DataNotFoundException;
import com.app.istech.Form.CompositionForm;
import com.app.istech.Form.CompositionFormList;
import com.app.istech.Form.CompositionSearchForm;
import com.app.istech.Form.ProductForm;
import com.app.istech.Form.ProductSearchForm;
import com.app.istech.Form.UploadForm;
import com.app.istech.Form.groups.CreateProduct;
import com.app.istech.Form.groups.UpdateProduct;
import com.app.istech.Model.Client;
import com.app.istech.Model.Composition;
import com.app.istech.Model.Product;
import com.app.istech.Service.ClientService;
import com.app.istech.Service.CompositionService;
import com.app.istech.Service.ProductService;

@Controller
@RequestMapping("/products")
@SessionAttributes(types = {ProductSearchForm.class,CompositionForm.class,UploadForm.class})
public class ProductController {

	private final ProductService productService;
	
	private final ClientService clientService;
	
	private final CompositionService compositionService;
	
	@Autowired
	public ProductController(ProductService productService,
			ClientService clientService,CompositionService compositionService){
		this.productService = productService;
		this.clientService = clientService;
		this.compositionService = compositionService;
	}
	
	
	@ModelAttribute(value = "productForm")
	public ProductForm setUpProductForm() {
		ProductForm productForm = new ProductForm();
		productForm.setClientList(clientService.findAll());
		
		return productForm;
	}

	@ModelAttribute(value = "compositionForm")
	public CompositionForm setUpCompositionForm() {
		CompositionForm compositionForm = new CompositionForm();
		compositionForm.setProductList(productService.findAll());
		
		return compositionForm;
	}

	@ModelAttribute(value = "uploadForm")
	public UploadForm setUpUploadForm() {
		UploadForm uploadForm = new UploadForm();
		return uploadForm;
	}
	
	@ModelAttribute(value = "productSearchForm")
	public ProductSearchForm setUpProductSearchForm() {
		ProductSearchForm productSearchForm = new ProductSearchForm();
		return productSearchForm;
	}
	
	@ModelAttribute(value = "compositionSearchForm")
	public CompositionSearchForm setUpCompositionSearchForm() {
		CompositionSearchForm compositionSearchForm = new CompositionSearchForm();
		return compositionSearchForm;
	}
	

	/*
	 *  ?????????????????????
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public String productIndex(Model model,@ModelAttribute("productSearchForm")ProductSearchForm productSearchForm,
			@PageableDefault(size = 20) Pageable pageable) {
		Page<Product> productPage = productService.findAllBySearchForm(pageable,productSearchForm);
		model.addAttribute("page",productPage);
		return "product/index";
	}

	
	/*
	 *  ?????????????????????
	 */
	@RequestMapping(path = "/{productId}", method = RequestMethod.GET)
	public String productDetail(Model model, @PathVariable("productId") Integer productId,
			@ModelAttribute("compositionSearchForm")CompositionSearchForm compositionSearchForm,
			@PageableDefault(size = 20) Pageable pageable) {
		
		Product product = productService.findPageById(productId,pageable,compositionSearchForm);
		Long count = compositionService.count(compositionSearchForm,productId);
		Page<Composition> compositionList = new PageImpl<Composition>(product.getChilds(),pageable,count);
		
		CompositionFormList compositionFormList = new CompositionFormList();
		model.addAttribute("product", product);
		model.addAttribute("page", compositionList);
		model.addAttribute("compositionFormList", compositionFormList);
		return "product/detail";
	}

	
	/*
	 *  ???????????????????????????
	 */
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public String productCreate(Model model, RedirectAttributes redirectAttributes,
			@ModelAttribute("productForm") @Validated({CreateProduct.class}) ProductForm productForm, BindingResult result) {
		
		if(result.hasErrors()) {
			return "product/add";
		}
		
		try {
			Product product = new Product(productForm);
			Client client = clientService.findByClientNum(productForm.getClientForm().getClientNum());
			product.setClient(client);
			
			productService.insertProduct(product);
		}catch(DuplicateKeyException e) {
			result.rejectValue("serialNum","data.duplicate", new String[]{"??????"},"");
			return "product/add";
		}catch(DataNotFoundException e) {
			result.rejectValue("clientForm.clientNum", "data.not-found",new String[]{"??????"},"");
			return "product/add";			
		}

		
		redirectAttributes.addFlashAttribute("status", "success");
		return "redirect:/products";
	}

	/*
	 *  ???????????????????????????
	 */
	@RequestMapping(path = "/add", method = RequestMethod.GET)
	public String productAdd(Model model,@ModelAttribute("productForm") ProductForm productForm) {
		return "product/add";
	}

	/*
	 *  ?????????????????????????????????
	 */
	@RequestMapping(path = "/bulk-insert/next", method = RequestMethod.POST)
	public String toCompositoinBulkInsert(Model model,@ModelAttribute("productForm") ProductForm productForm,
			@ModelAttribute @Validated UploadForm uploadForm,BindingResult result) {
		
		if(result.hasErrors()) {
			return "product/bulkInsert";
		}
		
		return "composition/bulkInsert";
	}
	
	
	/*
	 *  ?????????????????????????????????
	 */
	@RequestMapping(path = "/bulk-insert", method = RequestMethod.POST)
	public String productBulkInsert(Model model,RedirectAttributes redirectAttributes,
			HttpSession httpSession,SessionStatus sessionStatus,
			@ModelAttribute @Validated({CreateProduct.class}) UploadForm uploadForm,BindingResult result) {
		
		if(result.hasErrors()) {
			return "product/bulkInsert";
		}
		
		try {
			List<Product> productList = new ArrayList<Product>();
			for(ProductForm productForm : uploadForm.getProductList()) {
				Product product = new Product(productForm);	
				Client client = clientService.findByClientNum(productForm.getClientForm().getClientNum());
				product.setClient(client);
				productList.add(product);
			}
			productService.bulkInsert(productList);
			
		}catch(Exception e) {
			e.printStackTrace();
			return "product/bulkInsert";
		}
		
		// ?????????????????????
		httpSession.removeAttribute("user");
		httpSession.invalidate();
		sessionStatus.setComplete();
		
		redirectAttributes.addFlashAttribute("status", "success");
		return "redirect:/products";
	}
	
	/*
	 * ?????????????????????????????????
	 */
	@RequestMapping(path = "/add/bulk", method = RequestMethod.GET)
	public String productBulkAdd(Model model,@ModelAttribute("uploadForm")UploadForm uploadForm) {
		return "product/bulkInsert";
	}
	

	/* 
	 * ???????????????????????????????????????
	 */
	@RequestMapping(path = "/add/bulk", method = RequestMethod.POST)
	public String productBulkResult(Model model, @ModelAttribute("uploadForm") UploadForm uploadForm) {
		
		List<CSVRecord> resultSet = new ArrayList<CSVRecord>();
		
		try {
			resultSet = ProductService.parseCSV(uploadForm.getFile(), "Shift-JIS");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<ProductForm> resultFormList = ProductService.csvRecordToFormList(resultSet);
		uploadForm.setProductList(resultFormList);
		
		model.addAttribute("uploadForm", uploadForm);
		
		return "product/bulkInsert";
	}
	

	/*
	 *  ?????????????????????????????????
	 */
	@RequestMapping(path = "/{productId}/delete", method = RequestMethod.GET)
	public String productDelete(Model model, @PathVariable("productId") Integer productId) {
		Product product = productService.findById(productId);
		productService.updateDeletedFlg(product);
		return "redirect:/products/" + product.getProductId() + "?success";
	}

	
	/*
	 *  ???????????????????????????
	 */
	@RequestMapping(path = "/{productId}/parmanent-delete", method = RequestMethod.GET)
	public String productPermanentDelete(Model model, @PathVariable("productId") Integer productId) {
		productService.deleteProduct(productId);
		return "redirect:/products";
	}

	/*
	 *  ???????????????????????????
	 */
	@RequestMapping(path = "/{productId}/update", method = RequestMethod.POST)
	public String productUpdate(Model model,@PathVariable("productId") Integer productId ,
			@ModelAttribute("productForm") @Validated({UpdateProduct.class}) ProductForm productForm, BindingResult result) {
		
		if(result.hasErrors()) {
			return "product/edit";
		}
		
		try {
			Product product = new Product(productForm);
			Client client = clientService.findByClientNum(productForm.getClientForm().getClientNum());
			product.setClient(client);
			
			productService.updateProduct(product);
		}catch(Exception e) {
			e.printStackTrace();
			return "product/edit";
		}
		
		return "redirect:/products/" + productId;
	}
	
	// ???????????????????????????
	@RequestMapping(path = "/{productId}/edit", method = RequestMethod.GET)
	public String productEdit(Model model,@PathVariable("productId") Integer productId) {
		
		Product product = productService.findById(productId);
		ProductForm productForm = new ProductForm(product);
		List<Client> clientList = clientService.findAll();
		productForm.setClientList(clientList);
		
		model.addAttribute("productForm", productForm);
		return "product/edit";
	}

}
