package cdw.hk2.shop.laptop.controller;

import cdw.hk2.shop.laptop.model.Notify;
import cdw.hk2.shop.laptop.model.Order;
import cdw.hk2.shop.laptop.services.BillServices;
import cdw.hk2.shop.laptop.services.NotifyServices;
import cdw.hk2.shop.laptop.services.OrderServices;
import cdw.hk2.shop.laptop.services.TransactionServices;
import cdw.hk2.shop.laptop.utils.StringUtils;
import cdw.hk2.shop.laptop.utils.TimeUtlis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/manage")
public class AdStatisticalController {
	@Autowired
	private BillServices billServices;


//	@GetMapping("/thongke")
//	public String listOrder(Model model){
//
//
//		List<Object> thongke = billServices.thongkethang();
//
//		model.addAttribute("listTK", thongke);
//
//		return "/admin/manage_statistical";
//	}

	@GetMapping("/thongke")
	public String listOrder(Model model, @RequestParam(required = false) Map<String, String> prams){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date fromDate = null;Date toDate = null;
		String kw = prams.getOrDefault("kw", null);


		try{
			String from = prams.getOrDefault("fromDate", null);

			String to = prams.getOrDefault("toDate", null);

			if (from != null) {
				fromDate = f.parse(from);
			}
			if (to != null) {
				toDate = f.parse(to);
			}

		}catch (ParseException ex)
		{
			ex.printStackTrace();
		}

		List<Object> thongkesanpham = billServices.thongkesanphamthang(fromDate, toDate, kw);
		model.addAttribute("listTKSP", thongkesanpham);
		System.out.println("sphammmm: "+ thongkesanpham.size());
		return "/admin/manage_statistical";
	}
	@GetMapping("/thongkedoanhthu")
	public String listDT(Model model, @RequestParam(required = false) Map<String, String> prams){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date fromDate = null;Date toDate = null;
		try{
			String from = prams.getOrDefault("fromDate", null);

			String to = prams.getOrDefault("toDate", null);

			if (from != null) {
				fromDate = f.parse(from);
			}
			if (to != null) {
				toDate = f.parse(to);
			}

		}catch (ParseException ex)
		{
			ex.printStackTrace();
		}


		List<Object> thongke = billServices.thongkethang(fromDate,toDate);
		model.addAttribute("listTK", thongke);

		return "/admin/manage_statisticalDT";
	}
}

