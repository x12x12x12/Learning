package com.project.app;

import com.project.model.Account;
import com.project.model.Project;
import com.project.service.AccountRepo;
import com.project.service.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class LoginController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("location","home");
//        Project project=new Project();
//        project.setName("Java EE");
//        project.setAccountOwner("Đỗ Minh Quân");
//        project.setStartDate(new Date());
//        project.setDueDate(new Date());
//        project.setCreateDate(new Date());
//        projectRepo.save(project);
        return "login";
    }

}
