package com.app.istech.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.app.istech.Form.OrderForm;
import com.app.istech.Form.OrderSearchForm;
import com.app.istech.Form.groups.CreateOrder;
import com.app.istech.Form.groups.UpdateOrder;
import com.app.istech.Model.Client;
import com.app.istech.Model.Composition;
import com.app.istech.Model.Order;
import com.app.istech.Model.Product;
import com.app.istech.Service.ClientService;
import com.app.istech.Service.CompositionService;
import com.app.istech.Service.OrderService;
import com.app.istech.Service.ProductService;

@Controller
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	private final ProductService productService;
	
	private final ClientService clientService;
	
	private final CompositionService compositionService;
	
	@Autowired
	public OrderController(OrderService orderService,ProductService productService,
			ClientService clientService,CompositionService compositionService) {
		this.orderService = orderService;
		this.productService = productService;
		this.clientService = clientService;
		this.compositionService = compositionService;
	}
	
	@ModelAttribute(value="orderForm")
	public OrderForm setUpOrderForm() {
		OrderForm orderForm = new OrderForm();
		List<Client> clientList = clientService.findAll();
		orderForm.setClientList(clientList);
		
		List<Product> productList = productService.findAll();	
		orderForm.setProductList(productList);
		
		return orderForm;
	}
	
	// 注文書一覧
	@RequestMapping(path = "", method = RequestMethod.GET)
	public String getIndex(Model model,@PageableDefault(size = 20) Pageable pageable,
			@ModelAttribute("orderSearchForm")OrderSearchForm orderSearchForm) {
		Page<Order> orderPage = orderService.findAll(pageable,orderSearchForm);
		model.addAttribute("page", orderPage);
		return "order/index";
	}

	// 注文書詳細
	@RequestMapping(path = "/{orderId}", method = RequestMethod.GET)
	public String getDetail(Model model,@PathVariable("orderId")Integer orderId) {
		Order order = orderService.findById(orderId);
		model.addAttribute("order", order);
		return "order/detail";
	}

	// 注文書入力
	@RequestMapping(path = "/add", method = RequestMethod.GET)
	public String getAdd(Model model) {
		return "order/add";
	}

	// 注文書マスタ作成
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public String postCreate(@ModelAttribute("orderForm")@Validated({CreateOrder.class}) OrderForm orderForm
			,BindingResult result) {
		
		if(result.hasErrors()) {
			return "order/add";
		}
		
		try {
			Order order = new Order(orderForm);
			// 製品データのセット
			Product product = productService.findBySerialNum(orderForm.getProductForm().getSerialNum());
			order.setProduct(product);
			
			// 顧客データのセット
			Client client = clientService.findByClientNum(orderForm.getClientForm().getClientNum());
			order.setClient(client);
			
			orderService.insertOrder(order);
			
			// 親製品の現在個数更新
			productService.updateCurrentNum(product, order.getType(),order.getDeliveryNum());
			
			// 子製品の現在個数更新
			List<Composition> compositionList = compositionService.findCompositionByProductId(product.getProductId());
			productService.updateChildCurrentNum(compositionList, order.getType(),order.getDeliveryNum());
		}catch(Exception e) {
			e.printStackTrace();
			return "order/add";
		}
		
		return "redirect:/orders";
	}

	// 注文書更新処理
	@RequestMapping(path = "/{orderId}/update", method = RequestMethod.POST)
	public String postUpdate(@ModelAttribute @Validated({UpdateOrder.class})OrderForm orderForm,
			@PathVariable Integer orderId,BindingResult result) {
		
		if(result.hasErrors()) {
			return "order/edit";
		}
		
		try {
			Product product = productService.findBySerialNum(orderForm.getProductForm().getSerialNum());
			Client client = clientService.findByClientNum(orderForm.getClientForm().getClientNum());
			
			Order order = new Order(orderForm);
			order.setProduct(product);			
			order.setClient(client);
			
			//前回の注文書と製品データを取得
			Order previousOrder = orderService.findById(orderId);
			Product previousProduct = previousOrder.getProduct();

			int diff =  order.getDeliveryNum() - previousOrder.getDeliveryNum();
			// 注文製品の変更あり
			if(!previousProduct.getProductId().equals(product.getProductId())) {
				// 元注文製品の現在個数を戻す
				productService.updateCurrentNum(previousProduct, 1,previousOrder.getDeliveryNum());
				
				// 子製品の現在個数を戻す
				List<Composition> compositionList = compositionService.findCompositionByProductId(previousProduct.getProductId());
				productService.updateChildCurrentNum(compositionList, 1,previousOrder.getDeliveryNum());
				
				diff = order.getDeliveryNum();
			}
			
			// 親製品の現在個数更新
			productService.updateCurrentNum(order.getProduct(), order.getType(),diff);
			// 子製品の現在個数更新
			List<Composition> compositionList = 
					compositionService.findCompositionByProductId(order.getProduct().getProductId());
			productService.updateChildCurrentNum(compositionList, order.getType(),diff);
			
			// 注文書の更新
			orderService.updateOrder(order);		
				
		}catch(Exception e) {
			e.printStackTrace();
			return "order/edit";
		}
				
		return "redirect:/orders/" + orderId;
	}
	
	@RequestMapping(path = "/{orderId}/update/create-ts")
	public String postUpdateCreateTs(@PathVariable("orderId")Integer orderId,@RequestParam(required=true) boolean isCompleated) {
		Order order = orderService.findById(orderId);
		orderService.updateDeliveryDate(order);
		return "redirect:/orders/" + orderId;
	}

	// 注文書編集画面
	@RequestMapping(path = "/{orderId}/edit", method = RequestMethod.GET)
	public String getEdit(Model model, @PathVariable("orderId")Integer orderId) {
		
		Order order = orderService.findById(orderId);
		OrderForm orderForm = new OrderForm(order);
		List<Client> clientList = clientService.findAll();
		orderForm.setClientList(clientList);
		
		List<Product> productList = productService.findAll();	
		orderForm.setProductList(productList);
		
		model.addAttribute("orderForm",orderForm );
		return "order/edit";
	}
	
	// 顧客マスタ一時削除処理
	@RequestMapping(path = "/{orderId}/delete", method = RequestMethod.POST)
	public String postDelete(Model model, @PathVariable("orderId") Integer orderId) {
		Order order = orderService.findById(orderId);
		// 削除済みフラグ更新
		orderService.updateDeletedFlg(order);
		return "redirect:/orders/" + order.getOrderId();
	}

	// 注文書完全削除処理
	@RequestMapping(path = "/{orderId}/parmanent-delete", method = RequestMethod.DELETE)
	public String orderPermanentDelete(Model Model, @PathVariable("orderId") Integer orderId) {
		orderService.deleteOrder(orderId);
		return "redirect:/orders";
	}

}
