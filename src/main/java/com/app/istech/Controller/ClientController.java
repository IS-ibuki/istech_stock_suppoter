package com.app.istech.Controller;

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

import com.app.istech.Form.ClientForm;
import com.app.istech.Form.OrderSearchForm;
import com.app.istech.Form.groups.CreateClient;
import com.app.istech.Form.groups.UpdateClient;
import com.app.istech.Model.Client;
import com.app.istech.Model.Order;
import com.app.istech.Service.ClientService;
import com.app.istech.Service.OrderService;

@Controller
@RequestMapping("/clients")
public class ClientController {

	private final ClientService clientService;
	
	
	private final OrderService orderService;
	
	
	@Autowired
	public ClientController(ClientService clientService,OrderService orderService){
		this.orderService = orderService;
		this.clientService = clientService;
	}
	
	@ModelAttribute(value="clientForm")
	public ClientForm setUpClientForm() {
		ClientForm clientForm = new ClientForm();
		return clientForm;
	}
	
	// 顧客マスタ一覧
	@RequestMapping(path="", method = RequestMethod.GET)
	public String clientIndex(Model model,@PageableDefault(size = 15) Pageable pageable) {
		Page<Client> clientList = clientService.findAll(pageable);
		model.addAttribute("page", clientList);
		return "client/index";
	}

	// 顧客照会画面
	@RequestMapping(path = "/{clientId}", method = RequestMethod.GET)
	public String clientDetail(Model model, @PathVariable("clientId") Integer clientId,
			@PageableDefault(size = 10) Pageable pageable,@ModelAttribute("orderSearchForm")OrderSearchForm orderSearchForm) {	
		
		Client client = clientService.findById(clientId);
		Page<Order> orderPage = orderService.findPageByClientId(pageable,orderSearchForm,clientId);
		
		model.addAttribute("page", orderPage);
		model.addAttribute("client", client);
		return "client/detail";
	}
	
	// 顧客マスタ登録処理
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public String clientCreate(Model model, 
			@ModelAttribute("clientForm") @Validated({CreateClient.class}) ClientForm clientForm,BindingResult result) {
		
		if(result.hasErrors()) {
			return "client/add";
		}
		
		try {
			Client client = new Client(clientForm);	
			clientService.insertClient(client);			
		}catch(Exception e) {
			e.printStackTrace();
			return "client/add";
		}
		
		return "redirect:/clients";
	}

	// 顧客マスタ作成画面
	@RequestMapping(path = "/add", method = RequestMethod.GET)
	public String clientAdd(Model model) {
		return "client/add";
	}


	// 顧客マスタ更新処理
	@RequestMapping(path = "/{clientId}/update", method = RequestMethod.POST)
	public String clientUpdate(Model model,@PathVariable String clientId,
			@ModelAttribute("clientForm") @Validated({UpdateClient.class}) ClientForm clientForm,BindingResult result) {
		
		if(result.hasErrors()) {
			return "client/edit";
		}
		
		try {
			Client client = new Client(clientForm);	
			clientService.updateClient(client);
		}catch(Exception e) {
			e.printStackTrace();
			return "client/edit";
		}
		
		return "redirect:/clients/" + clientId;
	}

	// 顧客マスタ編集画面
	@RequestMapping(path = "/{clientId}/edit", method = RequestMethod.GET)
	public String clientEdit(Model model, @PathVariable("clientId") Integer clientId) {
		Client client = clientService.findById(clientId);
		model.addAttribute("clientForm", new ClientForm(client));
		return "client/edit";
	}

	// 顧客マスタ一時削除処理
	@RequestMapping(path = "/{clientId}/delete", method = RequestMethod.GET)
	public String clientDelete(Model model, @PathVariable("clientId") Integer clientId) {
		Client client = clientService.findById(clientId);
		clientService.updateDeletedFlg(client);
		return "redirect:/clients/" + client.getClientId();
	}

	
	// 顧客マスタ完全削除処理
	@RequestMapping(path = "/{clientId}/parmanent-delete", method = RequestMethod.GET)
	public String clientPermanentDelete(Model model, @PathVariable("clientId") Integer clientId) {
		clientService.deleteClient(clientId);
		return "redirect:/clients";
	}

}
