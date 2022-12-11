package cdw.hk2.shop.laptop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;

import cdw.hk2.shop.laptop.model.*;
import cdw.hk2.shop.laptop.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cdw.hk2.shop.laptop.security.AuthenticationFacade;
import cdw.hk2.shop.laptop.utils.EmailServiceImpl;
import cdw.hk2.shop.laptop.utils.StringUtils;
import cdw.hk2.shop.laptop.utils.TimeUtlis;

@Controller
public class PaymentController {
	@Autowired
	private CartServices cartServices;
	@Autowired
	private AuthenticationFacade authentication;
	@Autowired
	private ProvinceServices prServices;
	@Autowired
	private DistrictServices dServices;
	@Autowired
	private WardDtoServices wardServies;
	@Autowired
	private VillageServices villageS;
	@Autowired
	private ProductServices pServer;
	@Autowired
	private UserServiceImpl userSer;
	@Autowired
	private OrderServices orderServices;
	@Autowired
	private TransactionServices transService;
	@Autowired
	private StringUtils stricut;
	@Autowired
	private TimeUtlis time;
	@Autowired
	private NotifyServices notifyServer;
	@Autowired
	private EmailServiceImpl sendMail;
	@Autowired
	private CartItemServices cartItemServices;

	@PostMapping("/thanhtoan")
	public String viewPayment(Model model, @ModelAttribute(value = "address_order") AddressOrder addressOrder,
			HttpSession session) {
		long id = authentication.getIdUser();
		long tongtien = 0;
		int sl = 0;
		addressOrder.setProvince(prServices.FindByIdProvince(addressOrder.getProvince()).getName());
		addressOrder.setDistrict(dServices.findDistrictById(addressOrder.getDistrict()).getName());
		addressOrder.setWard(wardServies.findWardByID(addressOrder.getWard()).getName());
		addressOrder.setVillage(villageS.findVillageDtoByStringId(addressOrder.getVillage()).getName());
		Cart cart = cartServices.findCartById(authentication.getIdUser());


		List<Cart_Item> c = cartItemServices.findCartByUserId(id);

		for (int i = 0; i < c.size(); i++) {
			tongtien = (long) (tongtien + c.get(i).getProduct().getPrice() * c.get(i).getQuantity());
		}


		model.addAttribute("listProduct", cart.getProduct());
		session.setAttribute("address_order", addressOrder);
		String s = (String) model.getAttribute("total");
//		double totalPrice = pServer.GetPriceTotal(cart.getProduct(), 5.0);
		model.addAttribute("grandtotal", tongtien);
		return "checkout";

	}

	@PostMapping("/thanh-toan-don-hang")
	public ResponseEntity<Boolean> orderPayment(Model model, HttpServletRequest request,
			@RequestParam(name = "transaction") String transaction,
			@RequestParam(name = "paymentOrder") String raPayment, @RequestParam(name = "address") String address) {
		try {
		long id = authentication.getIdUser();
		User user = userSer.findUserById(id);
		Cart cart = cartServices.findCartById(authentication.getIdUser());
		List<Product> lsP= new ArrayList<>();

			long tongtien = 0;
			int sl = 0;
			List<Cart_Item> c = cartItemServices.findCartByUserId(id);
			for (int i = 0; i < c.size(); i++) {
				sl = sl + c.get(i).getQuantity();
				tongtien = (long) (tongtien + c.get(i).getProduct().getPrice() * c.get(i).getQuantity());
				lsP.add(c.get(i).getProduct());
			}
			String infor_Order="";

			for(int i=0;i< lsP.size();i++)

			{
				String sub=lsP.get(i).getName();
				int i_sub=sub.indexOf('(');
				String sub1=sub.substring(0,i_sub);
				infor_Order=infor_Order+sub1+"x("+String.valueOf(c.get(i).getQuantity())+")";
				if(i<lsP.size())
				{
					infor_Order=infor_Order+"<br>";
				}
			}



		HttpSession session = request.getSession();
		AddressOrder addressOrder = (AddressOrder) session.getAttribute("address_order");
		Payment payment = new Payment();
		payment.setName(raPayment);
		payment.setTransaction(transaction);
		String SKU = stricut.getSaltString("MDH_");
		Order order = new Order();
		order.setUser(user);
		
		order.setPayment(payment);
		order.setAddress(addressOrder);
		order.setSku_order(SKU);
		//tong tien
		order.setTotal(tongtien);
		//so luong
		order.setQuantity(sl);

		order.setInfor(infor_Order);

		order.setCreatedDate(time.convertToDateViaSqlTimestamp());
		Order orderS = orderServices.saveOrder(order);
		//update order
//		orderServices.updateProduct(cart.getProduct(), orderS);
			orderServices.updateProduct(lsP, orderS);
		// remove cart
//		cartServices.deleteCartByProduct(cart.getId());
		//
			for (int i = 0; i < c.size(); i++) {
				cartItemServices.delete(c.get(i).getId());
			}


		session.removeAttribute("cartuser");
		session.setAttribute("cartuser", new Cart());
		Notify orders= new Notify();
		orders.setChecks(false);orders.setKeyword("Order");orders.setContent( user.getName()+"Đã đặt hàng thành công:"+order.getSku_order());orders.setNameId(orders.getId());
		orders.setTime(time.convertToDateViaSqlTimestamp());orders.setUser(user);
		//gui mail
	//	sendMail.sendMail(orders.getContent(),orders.getKeyword(), user.getAccountDto().getEmail());
		notifyServer.Save(orders);
//		cart.setQuantity(0);cart.setTotal_price(0);
		cartServices.saveCart(cart);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
		}

	}
}
