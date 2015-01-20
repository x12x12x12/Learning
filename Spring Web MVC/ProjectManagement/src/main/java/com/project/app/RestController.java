package com.project.app;

import com.project.model.Account;
import com.project.model.Project;
import com.project.model.Task;
import com.project.service.AccountService;
import com.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @RequestMapping(value="/rest/{project}",method=RequestMethod.GET)
    public @ResponseBody Project getProject(@PathVariable("project") String project){
        return projectService.getProject(project);
    }


    @RequestMapping(value="/rest/project/{name}",method=RequestMethod.GET)
    public @ResponseBody List<Project> getListProject(@PathVariable("name") String accountOwner){
        List<Project> list=projectService.getListProject(accountOwner);
        return list;
    }

    @RequestMapping(value="/rest/project/create",method=RequestMethod.POST)
    public @ResponseBody Project createProject(@RequestBody Project project){
        try{
            if(project.getAccountOwner()!=null && project.getName()!=null){
                projectService.addProject(project);
            }
        }catch(Exception ex){
            project.setId("fail");
        }
        return project;
    }

    @RequestMapping(value="/rest/project/update",method=RequestMethod.POST)
    public @ResponseBody Project updateProject(@RequestBody Project project){
        try{
            if(project.getName()!=null){
                projectService.updateProject(project);
            }
        }catch(Exception ex){
            project.setId("fail");
        }
        return project;
    }

    @RequestMapping(value="/rest/task/{parent}",method=RequestMethod.GET)
    public @ResponseBody List<Task> getListTask(@PathVariable("parent") String parent){
        List<Task> list=projectService.getListTask(parent);
        return list;
    }

    @RequestMapping(value="/rest/task/create",method=RequestMethod.POST)
    public @ResponseBody Task createTask(@RequestBody Task task){
        try{
            if(task.getParent()!=null && task.getName()!=null){
                projectService.addTask(task);
            }
        }catch(Exception ex){
            task.setId("fail");
        }
        return task;
    }

    @RequestMapping(value="/rest/task/update",method=RequestMethod.POST)
    public @ResponseBody Task updateTask(@RequestBody Task task){
        try{
            if(task.getName()!=null &&task.getId()!=null){
                projectService.updateTask(task);
            }
        }catch(Exception ex){
            task.setId("fail");
        }
        return task;
    }

}
