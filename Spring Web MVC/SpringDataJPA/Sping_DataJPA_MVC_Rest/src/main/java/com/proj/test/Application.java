package com.proj.test;

import com.proj.repository.ProjectRepository;
import com.proj.repository.TaskRepository;
import com.proj.service.ProjectService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;

/**
 * Created by F.U.C.K on 23-Jan-15.
 */
public class Application {

    @Resource
    ProjectRepository projectRepository;

    @Resource
    TaskRepository taskRepository;

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/servlet-context.xml");

        ProjectService projectService=context.getBean("projectService",ProjectService.class);
    }
}
