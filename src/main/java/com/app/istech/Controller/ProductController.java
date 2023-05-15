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
import com.app.istech.Form.ProductFormParams;
import com.app.istech.Form.ProductSearchForm;
import com.app.istech.Form.ShippingForm;
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
@SessionAttributes(types = {ProductSearchForm.class,CompositionForm.class,ProductForm.class,ProductFormParams.class})
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

	@ModelAttribute(value = "shippingForm")
	public ShippingForm setUpShippingForm() {
		ShippingForm shippingForm = new ShippingForm();
		return shippingForm;
	}
	
	@ModelAttribute(value = "productFormParams")
	public ProductFormParams setUpProductFormParams() {
		ProductFormParams productFormParams = new ProductFormParams();
		return productFormParams;
	}
	
	
	/*
	 *  製品マスタ一覧
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public String productIndex(Model model,@ModelAttribute("productSearchForm")ProductSearchForm productSearchForm,
			@PageableDefault(size = 20) Pageable pageable) {
		Page<Product> productPage = productService.findAllBySearchForm(pageable,productSearchForm);
		model.addAttribute("page",productPage);
		return "product/index";
	}
	
	/*
	 * 入出庫管理画面
	 */
	@RequestMapping(path = "/ship/choice",method = RequestMethod.GET)
	public String getProductShipping(Model model,@PageableDefault(size = 5) Pageable pageable,
			@ModelAttribute("productSearchForm")ProductSearchForm productSearchForm,
			@ModelAttribute("shippingForm") ShippingForm shippingForm,
			@ModelAttribute("productFormParams")ProductFormParams productFormParams) {
		
		// セッションに入出庫対象の製品を格納
		/*
		for(ProductForm productForm : productFormParams.getProductFormList()) {
			if(productForm.isTarget()) {
				shippingForm.getShippingProductList().add(productForm);
			}
		}
		*/
		//shippingForm.setShippingProductList(productFormList);
		
		// フォームリストをページネーション処理
		Page<Product> productPage = productService.findAllBySearchForm(pageable,productSearchForm);

		List<ProductForm> productFormList = new ArrayList<ProductForm>();
		for(Product product : productPage.toList()) {
			productFormList.add(new ProductForm(product));
		}
		productFormParams.setProductFormList(productFormList);
		model.addAttribute("page",productPage);
		return "product/ship_target";
	}

	
	/*
	 * 入出庫管理画面
	 */
	@RequestMapping(path = "/ship/choice",method = RequestMethod.POST)
	public String postProductShipping(Model model,@PageableDefault(size = 20) Pageable pageable,
			@ModelAttribute("productSearchForm")ProductSearchForm productSearchForm,
			@ModelAttribute ShippingForm shippingForm) {
		
		// フォームリストをページネーション処理
		Page<Product> productPage = productService.findAllBySearchForm(pageable,productSearchForm);
		List<ProductForm> productFormList = new ArrayList<ProductForm>();
		for(Product product : productPage.toList()) {
			productFormList.add(new ProductForm(product));
		}
		
		shippingForm.setShippingProductList(productFormList);
		model.addAttribute("shippingForm",shippingForm);
		model.addAttribute("page",productPage);
		return "product/ship_target";
	}
	
	/*
	 * 
	 */
	@RequestMapping(path = "/ship", method = RequestMethod.POST)
	public String ShipProduct(Model model,@ModelAttribute ShippingForm shippingForm,
			@ModelAttribute("productFormParams")ProductFormParams productFormParams) {
		
		return "product/ship";
	}
	
	/*
	 *  製品マスタ詳細
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
	 *  製品マスタ登録処理
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
			result.rejectValue("serialNum","data.duplicate", new String[]{"品番"},"");
			return "product/add";
		}catch(DataNotFoundException e) {
			result.rejectValue("clientForm.clientNum", "data.not-found",new String[]{"顧客"},"");
			return "product/add";			
		}

		
		redirectAttributes.addFlashAttribute("status", "success");
		return "redirect:/products";
	}

	/*
	 *  製品マスタ作成画面
	 */
	@RequestMapping(path = "/add", method = RequestMethod.GET)
	public String productAdd(Model model,@ModelAttribute("productForm") ProductForm productForm) {
		return "product/add";
	}

	/*
	 *  製品マスタ一括作成処理
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
	 *  製品マスタ一括作成処理
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
		
		// セッション破棄
		httpSession.removeAttribute("user");
		httpSession.invalidate();
		sessionStatus.setComplete();
		
		redirectAttributes.addFlashAttribute("status", "success");
		return "redirect:/products";
	}
	
	/*
	 * 製品マスタ一括作成画面
	 */
	@RequestMapping(path = "/add/bulk", method = RequestMethod.GET)
	public String productBulkAdd(Model model,@ModelAttribute("uploadForm")UploadForm uploadForm) {
		return "product/bulkInsert";
	}
	

	/* 
	 * 製品マスタ一括作成結果画面
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
	 *  製品マスタ一時削除処理
	 */
	@RequestMapping(path = "/{productId}/delete", method = RequestMethod.GET)
	public String productDelete(Model model, @PathVariable("productId") Integer productId) {
		Product product = productService.findById(productId);
		productService.updateDeletedFlg(product);
		return "redirect:/products/" + product.getProductId() + "?success";
	}

	
	/*
	 *  製品マスタ削除処理
	 */
	@RequestMapping(path = "/{productId}/parmanent-delete", method = RequestMethod.GET)
	public String productPermanentDelete(Model model, @PathVariable("productId") Integer productId) {
		productService.deleteProduct(productId);
		return "redirect:/products";
	}

	
	/*
	 *  製品マスタ更新処理
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
	
	// 製品マスタ編集画面
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
