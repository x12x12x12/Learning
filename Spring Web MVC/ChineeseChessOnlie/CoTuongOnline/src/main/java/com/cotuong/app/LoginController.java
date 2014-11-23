package com.cotuong.app;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.cotuong.model.Account;
import com.cotuong.service.AccountService;
import com.cotuong.service.MailService;

@Controller
public class LoginController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping(value = {"/*","/login*"}, method = RequestMethod.GET)
	public String home(Model model,HttpSession httpSession) {
		if(validateSession(httpSession)){
			return "redirect:order";
		}
		model.addAttribute("account",new Account());
		return "login";
	}
	
	@RequestMapping(value = {"/active*"}, method = RequestMethod.GET)
	public String activeAccount() {
		return "active";
	}
	
	@RequestMapping(value ={"/","/login*"}, method=RequestMethod.POST)
	public String login_submit(@ModelAttribute("account") Account account,BindingResult result,HttpSession httpSession,Model model){
		if(account !=null ){
			model.addAttribute("account",account);
			if(account.getPassword().length() <4){
				ObjectError error1=new ObjectError("account.password", "Password length must be greater than 4 ");
				result.addError(error1);
				return "login";
			}else{
				Account result_login=accountService.getAccount(account.getEmail());
				if(result_login!=null){
					if(!passwordEncoder.matches(account.getPassword(),result_login.getPassword())){
						ObjectError error=new ObjectError("account.password", "Wrong password ! ");
						result.addError(error);
						return "login";
					}
					if(result_login.getStatus()==2){   // 2 : not activated , 0 : online , 1 : offline
						ObjectError error=new ObjectError("account.email", "Your account is not activated, Pls check your email ! ");
						result.addError(error);
						return "login";
					}
					result_login.setPassword("");
					result_login.setStatus(0);
					httpSession.setAttribute("account", result_login);
					return "redirect:main";
				}else{
					ObjectError error=new ObjectError("account.email", "Email not in database ! ");
					result.addError(error);
					return "login";
				}
			}
		}
		model.addAttribute("account",account);
		return "login";
	}
	
	@RequestMapping(value = {"/logout*"}, method = RequestMethod.GET)
	public String logout(Model model,SessionStatus sessionStatus,HttpSession httpSession) {
		httpSession.removeAttribute("account");
		model.addAttribute("account",new Account());
		model.addAttribute("sessionExpired","Logout successful !!!!");
		return "login";
	}
	
	@RequestMapping(value = {"/main*"}, method = RequestMethod.GET)
	public String main(Model model,HttpSession httpSession) {
		if(validateSession(httpSession)){
			return "game";
		}
		model.addAttribute("sessionExpired","User session expired please login again !!!!");
		model.addAttribute("account",new Account());
		return "login";
	}
	
	
	
	
	public boolean validateSession(HttpSession httpSession){
		Account account= ((httpSession.getAttribute("account")!=null) ? (Account) httpSession.getAttribute("account")  :new Account());
		if(account.getEmail()==null || account.getEmail().equalsIgnoreCase("")){
			return false;
		}
		return true;
	}
}
