package cdw.hk2.shop.laptop.controller;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cdw.hk2.shop.laptop.model.*;
import cdw.hk2.shop.laptop.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import cdw.hk2.shop.laptop.dto.*;
import cdw.hk2.shop.laptop.security.AuthenticationFacade;
import cdw.hk2.shop.laptop.utils.TimeUtlis;

@Controller
@RequestMapping("/tai-khoan")
public class ShoppingCartController {
	@Autowired
	private ProductServices pServer;
	@Autowired
	private ProvinceServices prServices;
	@Autowired
	private AuthenticationFacade authentication;
	@Autowired
	private CartServices cartServices;
	@Autowired
	private UserServiceImpl userServices;
	@Autowired
	private TimeUtlis time;
	@Autowired
	private NotifyServices notifyServer;
	@Autowired
	private CartItemServices cartItemServices;

	@GetMapping("/gio-hang")
	public String viewCart(Model model, HttpServletRequest request) {
		long id = authentication.getIdUser();
		Cart cart = cartServices.findCartById(id);
		int sl = 0;
		long tongtien = 0;

		List<Cart_Item> c = cartItemServices.findCartByUserId(id);
		List<Product> cartP = new ArrayList<Product>();
		AddressOrder ao = new AddressOrder();
		model.addAttribute("cart", c);

//			HttpSession session = request.getSession();
//			session.removeAttribute("cartuser");
//			session.setAttribute("cartuser", c);
		for (int i = 0; i < c.size(); i++) {
			sl = sl + c.get(i).getQuantity();
			tongtien = (long) (tongtien + c.get(i).getProduct().getPrice() * c.get(i).getQuantity());
		}
		List<ProvinceDto> listProvince = prServices.findAllProvince();
		List<Product> listOther = pServer.findAllProduct();
		model.addAttribute("listProvince", listProvince);
		model.addAttribute("sl", sl);
		model.addAttribute("tongtien", tongtien);
//			model.addAttribute("listOther", listOther);
		model.addAttribute("address_order", ao);

		return "shopping_cart";
	}

	@GetMapping("/themgiohang")
	public String addProductCart(Model model, HttpServletRequest request) {
		int sl=0;
		long tongtien=0;
		if (!authentication.getNameUser().equals("")) {

			User user = userServices.findUserById(authentication.getIdUser());
			String getID = request.getParameter("ajaxID");
			long id = Long.parseLong(getID);
			Product product = new Product();

			Cart_Item cart_item = new Cart_Item();


			Optional<Cart_Item> cart_item1 = Optional.empty();

			cart_item1 = cartItemServices.findCartByProductId(id);
			//check dieu kien
			if (cart_item1.isEmpty()) {
				product = pServer.findByIdProduct(id);
				cart_item.setUser(user);
				cart_item.setProduct(product);
				cart_item.setQuantity(1);
				Cart_Item c = cartItemServices.saveCartItem(cart_item);
			} else {
				product = pServer.findByIdProduct(id);
				cart_item = cartItemServices.findCartByProductId1(id);
				cart_item.setUser(user);
				cart_item.setProduct(product);
				cart_item.setQuantity(cart_item.getQuantity() + 1);
				Cart_Item c = cartItemServices.saveCartItem(cart_item);

			}

			List<Cart_Item> sessionC = cartItemServices.findCartByUserId(authentication.getIdUser());

			for (int i = 0; i <  sessionC.size(); i++) {
				sl = sl +  sessionC.get(i).getQuantity();
				tongtien = (long) (tongtien +  sessionC.get(i).getProduct().getPrice() *  sessionC.get(i).getQuantity());
			}

			HttpSession session = request.getSession();
			session.removeAttribute("cartItem");
			session.removeAttribute("total");
			session.removeAttribute("sl");


			session.setAttribute("cartItem", sessionC);
			session.setAttribute("total",tongtien);
			session.setAttribute("sl",sl);


//			Cart cart =cartServices.updateCart(user,product);
//			HttpSession session = request.getSession();
//			session.removeAttribute("cartuser");
//			session.setAttribute("cartuser", cart);
//			Notify carts= new Notify();
//			carts.setChecks(false);carts.setKeyword("cart");carts.setContent( user.getName()+"-"+"?� th�m s?n ph?m:");carts.setNameId(carts.getId());
//			carts.setTime(time.convertToDateViaSqlTimestamp());carts.setUser(user);
//			notifyServer.Save(carts);
			return "fragments/cart";

		} else {
			return "login";
		}

	}

//	@GetMapping("/capnhatdonhang")
//	public String updatecart(Model model) {
//		return null;
//
//	}


	@GetMapping("/gio-hang/xoa/{id}")
	public RedirectView deleteProductCart(HttpServletRequest request, Model model, @PathVariable("id") long id) {

		Cart_Item c1 = cartItemServices.findCartByProductId1(id);
		cartItemServices.delete(c1.getId());
		return new RedirectView("/tai-khoan/gio-hang");

	}


	@GetMapping(path = "/capnhatgiohang")
	public ResponseEntity<Boolean> UpdateItemList(HttpServletRequest request,@RequestParam(value = "up[]") Long[] itemIDs) {
		try {
			System.out.println(itemIDs[0]);
			List<Cart_Item> upList = cartItemServices.findCartByUserId(authentication.getIdUser());
			Cart_Item cart_item_up = new Cart_Item();
			User user = userServices.findUserById(authentication.getIdUser());

			if (upList.size() > 0) {
				for (int i = 0; i < upList.size(); i++) {
					if (itemIDs[i] == 0) {
						Product product = pServer.findByIdProduct(upList.get(i).getProduct().getId());
						Cart_Item c1 = cartItemServices.findCartByProductId1(product.getId());
						cartItemServices.delete(c1.getId());
					} else {
						Product product = pServer.findByIdProduct(upList.get(i).getProduct().getId());
						cart_item_up = cartItemServices.findCartByProductId1(upList.get(i).getProduct().getId());
						cart_item_up.setUser(user);
						cart_item_up.setProduct(product);
						cart_item_up.setQuantity(Math.toIntExact(itemIDs[i]));
						Cart_Item c = cartItemServices.saveCartItem(cart_item_up);
					}

				}

			}
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception e) {
		return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
	}


	}






}
