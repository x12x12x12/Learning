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
import org.springframework.web.bind.annotation.RequestParam;
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
			return "redirect:main";
		}
		model.addAttribute("account",new Account());
		return "login";
	}
	
	@RequestMapping(value ={"/login*"}, method=RequestMethod.POST)
	public String login_submit(@ModelAttribute("account") Account account,BindingResult result,HttpSession httpSession,Model model){
		if(account !=null ){
			model.addAttribute("account",account);
			if(account.getPassword().length() <4){
				ObjectError error1=new ObjectError("account.password", "Password length must be greater than 4 ");
				result.addError(error1);
				return "login";
			}else{
				try{
					Account result_login=accountService.getAccount(account.getEmail());
					if(result_login!=null){
						if(!passwordEncoder.matches(account.getPassword(),result_login.getPassword())){
							ObjectError error=new ObjectError("account.password", "Wrong password ! ");
							result.addError(error);
							return "login";
						}
						if(result_login.getStatus()==2){   // 2 : not activated , 0 : offline , 1 : online
							ObjectError error=new ObjectError("account.email", "Your account is not activated, Pls check your email ! ");
							result.addError(error);
							return "login";
						}
						result_login.setPassword("");
						result_login.setStatus(0);
						httpSession.setAttribute("account", result_login);
						accountService.setStatusOnline(account.getEmail());
						return "redirect:main";
					}else{
						ObjectError error=new ObjectError("account.email", "Email not in database ! ");
						result.addError(error);
						return "login";
					}
				}catch(Exception ex){
					ObjectError error=new ObjectError("account.email", "Can't connect to server ! ");
					result.addError(error);
					return "login";
				}
			}
		}
		model.addAttribute("account",account);
		return "login";
	}
	

	@RequestMapping(value = {"/active*"}, method = RequestMethod.GET,params={"ref"})
	public String activeAccount(@RequestParam(value="ref") String email,Model model) {
		Account account=new Account();
		account.setEmail(email);
		model.addAttribute("account",account);
		return "active_account";
	}
	
	@RequestMapping(value = {"/active*"}, method = RequestMethod.POST)
	public String activeAccount_Post(@ModelAttribute("account") Account account,BindingResult result,Model model) {
		try {
			Account check=accountService.getAccount(account.getEmail());
			if(check!=null){
				if(passwordEncoder.matches(account.getEmail(),account.getPassword())){
					accountService.activateAccount(account.getEmail());	
					model.addAttribute("account",new Account());
					model.addAttribute("activeOK","Your account has been activated !!!!");
					return "login";
				}else{
					ObjectError error=new ObjectError("account.password", "Invalid Code ! ");
					result.addError(error);
					return "active_account";
				}
			}
			ObjectError error=new ObjectError("account.email", "Email not in database ! ");
			result.addError(error);
			return "active_account";
		} catch (Exception e) {
			ObjectError error=new ObjectError("account.email", "Can't connect to server ! ");
			result.addError(error);
			return "active_account";
		}
		
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
			Account account=(Account)httpSession.getAttribute("account");
			account.setPassword("");
			model.addAttribute("account",account);
			return "board";
		}
		model.addAttribute("sessionExpired","User session expired please login again !!!!");
		model.addAttribute("account",new Account());
		return "login";
	}
	
	
	
	/**
	 * Validate Account Session
	 * @param httpSession
	 * @return true || false
	 */
	public boolean validateSession(HttpSession httpSession){
		Account account= ((httpSession.getAttribute("account")!=null) ? (Account) httpSession.getAttribute("account")  :new Account());
		if(account.getEmail()==null || account.getEmail().equalsIgnoreCase("")){
			return false;
		}
		return true;
	}
}
