package cdw.hk2.shop.laptop.controller;

import cdw.hk2.shop.laptop.dto.AccountDto;
import cdw.hk2.shop.laptop.model.Cart_Item;
import cdw.hk2.shop.laptop.model.User;
import cdw.hk2.shop.laptop.services.UserServiceImpl;
import cdw.hk2.shop.laptop.utils.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/taikhoan")
public class testcontroler {

    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private UserServiceImpl userServices;

    @Autowired
    private BCryptPasswordEncoder pass;

    @GetMapping("/quenmatkhau")
    public String deleteProductCart() {

        return "forgotpassword";

    }

    @PostMapping("/t1")
    public String createNewUser(Model model, @RequestParam(value = "username") String username) {



       if(userServices.checkUserName1(username))
       {
           int code = (int) Math.floor(((Math.random() * 899999) + 100000));
           model.addAttribute("code", code);
           String resetPass=String.valueOf(code);

           AccountDto account = userServices.getUserByUsername(username);

            String passNew = pass.encode(resetPass);
            userServices.updatePass(passNew, account.getId());



           emailService.sendSimpleEmail(username,"Lấy lại mật khẩu","Mật khẩu mới của bạn là :"+resetPass);
       }
       else {
           System.out.println("error_user name");
           model.addAttribute("checkemail", "Email không tồn tại");
           return "forgotpassword";
       }

        return "forgotpassword";

    }
}
