package com.project.app;

import com.project.model.Account;
import com.project.model.Project;
import com.project.repository.ProjectRepository;
import com.project.service.AccountService;
import com.project.service.ProjectService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {

	@Autowired
	AccountService accountService;
	@Autowired
	ProjectService projectService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Resource
	ProjectRepository projectRepository;

	@RequestMapping(value = {"/*","/login*"}, method = RequestMethod.GET)
	public String home(Model model,HttpSession httpSession) {

//		if(validateSession(httpSession)){
//			return "redirect:/index";
//		}
//		String expired=httpSession.getAttribute("sessionExpired")!=null ? httpSession.getAttribute("sessionExpired").toString() : "";
//		if(expired.equalsIgnoreCase("true")){
//			model.addAttribute("sessionExpired","User session expired please login again !!!!");
//			httpSession.removeAttribute("sessionExpired");
//		}
//		model.addAttribute("account",new Account());
		projectRepository.findAll();
		projectRepository.delete("PROJ-859838451");
		return "login";
	}
	@RequestMapping(value = {"/index*"}, method = RequestMethod.GET)
	public String index(Model model,HttpSession httpSession) {
		if(validateSession(httpSession)){
			Account result_login=(Account) httpSession.getAttribute("account");
			model.addAttribute("account",result_login);
			List<Project> list=projectService.getListProject(result_login.getEmail());
			model.addAttribute("listProject",list);
			return "index";
		}
		httpSession.setAttribute("sessionExpired","true");
		return "redirect:/login";
	}

	@RequestMapping(value ={"/login"}, method= RequestMethod.POST)
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
				}else{
					ObjectError error=new ObjectError("account.email", "Email not in database ! ");
					result.addError(error);
					return "login";
				}
				account.setPassword("");
				httpSession.setAttribute("account", result_login);
				return "redirect:/index";
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

	public boolean validateSession(HttpSession httpSession){
		Account account= ((httpSession.getAttribute("account")!=null) ? (Account) httpSession.getAttribute("account")  :new Account());
		if(account.getEmail()==null || account.getEmail().equalsIgnoreCase("")){
			return false;
		}
		return true;
	}
}
