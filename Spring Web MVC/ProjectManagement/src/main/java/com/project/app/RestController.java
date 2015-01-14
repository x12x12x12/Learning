package com.project.app;

import com.project.model.Account;
import com.project.model.Project;
import com.project.model.Task;
import com.project.service.AccountService;
import com.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RestController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value="/rest/check",method= RequestMethod.POST)
    public @ResponseBody Account checkAccount(@RequestBody Account account){
        Account check=accountService.getAccount(account.getEmail());
        if(check!=null){
            account.setEmail("fail");
        }else{
            accountService.add(account);
        }
        account.setPassword("");
        return account;
    }

    @RequestMapping(value="/rest/project/{name}",method=RequestMethod.GET)
    public @ResponseBody List<Project> getListProject(@PathVariable("name") String accountOwner){
        List<Project> list=projectService.getListProject(accountOwner);
        return list;
    }

    @RequestMapping(value="/rest/task/{parent}",method=RequestMethod.GET)
    public @ResponseBody List<Task> getListTask(@PathVariable("parent") String parent){
        List<Task> list=projectService.getListTask(parent);
        return list;
    }


}
