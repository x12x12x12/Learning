package com.proj.app;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.proj.model.Project;
import com.proj.model.Task;
import com.proj.repository.ProjectRepository;
import com.proj.repository.TaskRepository;
import com.proj.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Created by fuck on 22/01/15.
 */
@Controller
public class HomeController {

	@Autowired
	ProjectService projectService;

	@Resource
	ProjectRepository projectRepository;

	@Resource
	TaskRepository taskRepository;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		for (int i = 0; i <4 ; i++) {
			Project project=new Project();
			project.setName("IOS-"+i);
			project.setAccountOwner("Do Minh Quan-" + i);
			projectRepository.save(project);
			Task task=new Task();
			task.setName("Requirement-"+i);
			task.setProjectRoot(project);
			taskRepository.save(task);
		}
//		Iterable<Project> list=projectRepository.findAll();
//
//		for (Project entry : list) {
//			entry.getName();
//		}
//


		Project project_find=projectRepository.findOne(1);
		Task task_find=taskRepository.findOne(1);
		Project proj_name=projectRepository.findByName("IOS-1");
		projectRepository.count();
		return "home";
	}
	
}
