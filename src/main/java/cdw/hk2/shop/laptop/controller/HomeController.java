package cdw.hk2.shop.laptop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cdw.hk2.shop.laptop.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cdw.hk2.shop.laptop.model.*;
import cdw.hk2.shop.laptop.model.Brand;
import cdw.hk2.shop.laptop.model.Cart;
import cdw.hk2.shop.laptop.model.Category;
import cdw.hk2.shop.laptop.model.Product;
import cdw.hk2.shop.laptop.model.User;
import cdw.hk2.shop.laptop.security.AuthenticationFacade;

@Controller
public class HomeController {
	@Autowired
	private ProductServices productServices;
	@Autowired
	private BannerServices bannerS;
	@Autowired
	private AuthenticationFacade authentication;
	@Autowired
	private CategoryServices icategory;
	@Autowired
	private BrandServices brandServer;
	@Autowired
	private CartServices cartServices;
	@Autowired
	private UserServiceImpl userS;
	@Autowired
	private CartItemServices cartItemServices;

	@GetMapping("/")
	public String homeView(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String check = authentication.getNameUser();
		int sl=0;
		long tongtien=0;
		System.out.println(check+"fffffffffffffffff");;
		if (!check.equals("anonymousUser")) {
			User user = userS.findUserById(authentication.getIdUser());



			List<Cart_Item> sessionC = cartItemServices.findCartByUserId(user.getId());
			for (int i = 0; i < sessionC.size(); i++) {
				sl = sl + sessionC.get(i).getQuantity();
				tongtien = (long) (tongtien + sessionC.get(i).getProduct().getPrice() * sessionC.get(i).getQuantity());
			}
			session.setAttribute("cartItem", sessionC);
			session.setAttribute("total",tongtien);
			session.setAttribute("sl",sl);



		}
		List<Category> listCategory = icategory.findAllCategoryBy();
		session.setAttribute("listCategory", listCategory);
		List<Brand> listBrand = brandServer.findAllBrand();
		session.setAttribute("listBrand", listBrand);
		List<Product> listProducts = productServices.findAllProduct();
		model.addAttribute("listproduct", listProducts);
		List<Product> listTop = productServices.findAllByCategory(5);
		List<Product> listTopSale = productServices.findAllByCategory(5);
		List<Product> listNewProduct = productServices.findAllByCategory(5);
		List<Product> listliht = productServices.findAllByCategory(5);
		List<Product> listFactory = productServices.findAllByCategory(6);
		List<Banner> listBannerns = bannerS.findAllBanner();
		model.addAttribute("listBanner", listBannerns);
		model.addAttribute("top", listTopSale);
		model.addAttribute("newlist", listNewProduct);
		model.addAttribute("highlights", listliht);
		model.addAttribute("listfactory", listFactory);





		return "index";
	}

	@RequestMapping(value = "/tim-kiem/tu-khoa/san-pham", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> searchKeyWord(Model model, @RequestParam("search") String search,
			@RequestParam("key") String key) throws Exception {
		List<String> listKey = new ArrayList<String>();
		
			listKey = productServices.searchProduct(key, search);
		return new ResponseEntity<List<String>>(listKey,HttpStatus.OK);
	}
}
