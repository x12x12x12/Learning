package com.project.app;

import com.project.model.Account;
import com.project.model.Task;
import com.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProjectController {

    @Autowired
    ProjectService projectService;

//    @RequestMapping(value = {"/project"}, method = RequestMethod.GET)
//    public String taskInProject(Model model,HttpSession httpSession,@RequestParam("parent") String parent) {
////        if(validateSession(httpSession)){
////            return "redirect:index";
////        }
//        Account account= ((httpSession.getAttribute("account")!=null) ? (Account) httpSession.getAttribute("account")  :new Account());
//
//        model.addAttribute("account",new Account());
//        return "project_detail";
//    }

    @RequestMapping(value="/project/{id}", method = RequestMethod.GET)
    public String taskInProject(Model model,HttpSession httpSession,@PathVariable("id") String parent) {
        if(validateSession(httpSession)){
            List<Task> list=projectService.getListTask(parent);
            if(!list.isEmpty()){
                Account account= ((httpSession.getAttribute("account")!=null) ? (Account) httpSession.getAttribute("account")  :new Account());
                model.addAttribute("account",account);
                model.addAttribute("project",parent);
                model.addAttribute("task",list);
                return "project_detail";
            }
            return "redirect:/index";
        }
        httpSession.setAttribute("sessionExpired","true");
        return "redirect:/login";
    }

    public boolean validateSession(HttpSession httpSession){
        Account account= ((httpSession.getAttribute("account")!=null) ? (Account) httpSession.getAttribute("account")  :new Account());
        if(account.getEmail()==null || account.getEmail().equalsIgnoreCase("")){
            return false;
        }
        return true;
    }
}
