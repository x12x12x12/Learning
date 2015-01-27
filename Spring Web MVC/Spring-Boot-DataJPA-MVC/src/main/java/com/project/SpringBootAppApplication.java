package com.project;

import com.project.model.Account;
import com.project.model.Project;
import com.project.model.Task;
import com.project.repository.AccountRepository;
import com.project.repository.ProjectRepository;
import com.project.repository.TaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class SpringBootAppApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(SpringBootAppApplication.class, args);
        AccountRepository accountRepository= context.getBean(AccountRepository.class);
        ProjectRepository projectRepository = context.getBean(ProjectRepository.class);
        TaskRepository taskRepository=context.getBean(TaskRepository.class);

        Account account=new Account();
        account.setAccountName("quan");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateInString = "20-11-1993 00:00:00";
        try {
            Date date = sdf.parse(dateInString);
            account.setDateOfBirth(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        accountRepository.save(account);
        Project project_ios=new Project();
        project_ios.setName("IOS");
        project_ios.setAccountOwner(account);
        projectRepository.save(project_ios);
        Project project_android=new Project();
        project_android.setName("Android");
        project_android.setAccountOwner(account);
        projectRepository.save(project_android);
        Project project_wp=new Project();
        project_wp.setName("Window Phone");
        project_wp.setAccountOwner(account);
        projectRepository.save(project_wp);

        for (int j = 0; j < 5; j++) {
            Task task=new Task();
            task.setName("IOS-Main Task-"+j);
            task.setProject(project_ios);
            taskRepository.save(task);
            for (int k = 0; k <3 ; k++) {
                Task task_child=new Task();
                task_child.setName("IOS-Child Task-"+j);
                task_child.setProject(project_ios);
                task_child.setTaskParent(task);
                taskRepository.save(task_child);
            }
        }
        for (int j = 0; j < 5; j++) {
            Task task=new Task();
            task.setName("Android-Main Task-"+j);
            task.setProject(project_android);
            taskRepository.save(task);
            for (int k = 0; k <3 ; k++) {
                Task task_child=new Task();
                task_child.setName("Android-Child Task-"+j);
                task_child.setProject(project_android);
                task_child.setTaskParent(task);
                taskRepository.save(task_child);
            }
        }
        for (int j = 0; j < 5; j++) {
            Task task=new Task();
            task.setName("WP-Main Task-"+j);
            task.setProject(project_wp);
            taskRepository.save(task);
            for (int k = 0; k <3 ; k++) {
                Task task_child=new Task();
                task_child.setName("WP-Child Task-"+j);
                task_child.setProject(project_wp);
                task_child.setTaskParent(task);
                taskRepository.save(task_child);
            }
        }
    }
}
