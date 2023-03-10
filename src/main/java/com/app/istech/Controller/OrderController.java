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
	
	// ???????????????
	@RequestMapping(path = "", method = RequestMethod.GET)
	public String orderIndex(Model model,@PageableDefault(size = 20) Pageable pageable,
			@ModelAttribute("orderSearchForm")OrderSearchForm orderSearchForm) {
		Page<Order> orderPage = orderService.findAll(pageable,orderSearchForm);
		model.addAttribute("page", orderPage);
		return "order/index";
	}

	// ???????????????
	@RequestMapping(path = "/{orderId}", method = RequestMethod.GET)
	public String orderDetail(Model model,@PathVariable("orderId")Integer orderId) {
		Order order = orderService.findById(orderId);
		model.addAttribute("order", order);
		return "order/detail";
	}

	// ???????????????
	@RequestMapping(path = "/add", method = RequestMethod.GET)
	public String orderAdd(Model model) {
		return "order/add";
	}

	// ????????????????????????
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public String orderCreate(@ModelAttribute("orderForm")@Validated({CreateOrder.class}) OrderForm orderForm
			,BindingResult result) {
		
		if(result.hasErrors()) {
			return "order/add";
		}
		
		try {
			Order order = new Order(orderForm);
			// ???????????????????????????
			Product product = productService.findBySerialNum(orderForm.getProductForm().getSerialNum());
			order.setProduct(product);
			
			// ???????????????????????????
			Client client = clientService.findByClientNum(orderForm.getClientForm().getClientNum());
			order.setClient(client);
			
			orderService.insertOrder(order);
			
			// ??????????????????????????????
			productService.updateCurrentNum(product, order.getType(),order.getDeliveryNum());
			
			// ??????????????????????????????
			List<Composition> compositionList = compositionService.findCompositionByProductId(product.getProductId());
			productService.updateChildCurrentNum(compositionList, order.getType(),order.getDeliveryNum());
		}catch(Exception e) {
			e.printStackTrace();
			return "order/add";
		}
		
		return "redirect:/orders";
	}

	// ?????????????????????
	@RequestMapping(path = "/{orderId}/update", method = RequestMethod.POST)
	public String orderUpdate(@ModelAttribute @Validated({UpdateOrder.class})OrderForm orderForm,
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
			
			//?????????????????????????????????????????????
			Order previousOrder = orderService.findById(orderId);
			Product previousProduct = previousOrder.getProduct();

			int diff =  order.getDeliveryNum() - previousOrder.getDeliveryNum();
			// ???????????????????????????
			if(!previousProduct.getProductId().equals(product.getProductId())) {
				// ???????????????????????????????????????
				productService.updateCurrentNum(previousProduct, 1,previousOrder.getDeliveryNum());
				
				// ?????????????????????????????????
				List<Composition> compositionList = compositionService.findCompositionByProductId(previousProduct.getProductId());
				productService.updateChildCurrentNum(compositionList, 1,previousOrder.getDeliveryNum());
				
				diff = order.getDeliveryNum();
			}
			
			// ??????????????????????????????
			productService.updateCurrentNum(order.getProduct(), order.getType(),diff);
			// ??????????????????????????????
			List<Composition> compositionList = 
					compositionService.findCompositionByProductId(order.getProduct().getProductId());
			productService.updateChildCurrentNum(compositionList, order.getType(),diff);
			
			// ??????????????????
			orderService.updateOrder(order);		
				
		}catch(Exception e) {
			e.printStackTrace();
			return "order/edit";
		}
				
		return "redirect:/orders/" + orderId;
	}

	// ?????????????????????
	@RequestMapping(path = "/{orderId}/edit", method = RequestMethod.GET)
	public String orderEdit(Model model, @PathVariable("orderId")Integer orderId) {
		
		Order order = orderService.findById(orderId);
		OrderForm orderForm = new OrderForm(order);
		List<Client> clientList = clientService.findAll();
		orderForm.setClientList(clientList);
		
		List<Product> productList = productService.findAll();	
		orderForm.setProductList(productList);
		
		model.addAttribute("orderForm",orderForm );
		return "order/edit";
	}
	
	// ?????????????????????????????????
	@RequestMapping(path = "/{orderId}/delete", method = RequestMethod.GET)
	public String orderDelete(Model model, @PathVariable("orderId") Integer orderId) {
		Order order = orderService.findById(orderId);
		orderService.updateDeletedFlg(order);
		return "redirect:/orders/" + order.getOrderId();
	}

	// ?????????????????????
	@RequestMapping(path = "/{orderId}/parmanent-delete", method = RequestMethod.GET)
	public String orderPermanentDelete(Model Model, @PathVariable("orderId") Integer orderId) {
		orderService.deleteOrder(orderId);
		return "redirect:/orders";
	}

}
