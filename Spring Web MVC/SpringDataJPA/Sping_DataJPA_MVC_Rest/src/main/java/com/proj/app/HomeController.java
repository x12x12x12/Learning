package com.proj.app;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.proj.model.Project;
import com.proj.model.Task;
import com.proj.repository.PagingAndSortingProject;
import com.proj.repository.PagingAndSortingTask;
import com.proj.repository.ProjectRepository;
import com.proj.repository.TaskRepository;
import com.proj.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
public class HomeController {

	@Autowired
	ProjectService projectService;

	@Resource
	ProjectRepository projectRepository;

	@Resource
	TaskRepository taskRepository;

	@Resource
	PagingAndSortingProject pagingAndSortingProject;

	@Resource
	PagingAndSortingTask pagingAndSortingTask;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		for (int i = 0; i <4 ; i++) {
			Project project=new Project();
			project.setName("IOS-"+i);
			project.setAccountOwner("Do Minh Quan");
			projectRepository.save(project);
			for (int j = 0; j < 5; j++) {
				Task task=new Task();
				task.setName("IOS-"+i+"-"+j);
				task.setProject(project);
				taskRepository.save(task);
			}
		}

		/**
		 * Test method : OK
		 */
//		Long count= projectRepository.count();

//		Iterable<Project> list_project=projectRepository.findAll();
//		Iterable<Task> list_task=taskRepository.findAll();
//
//		List<Project> list = projectRepository.findByAccountOwner("Do Minh Quan");
//		List<Project> list_fail=projectRepository.findByAccountOwner("ABC");
//
//		List<Project> list_findByNameOK_1=projectRepository.findByName("IOS%");
//		List<Project> list_findByNameOK_2=projectRepository.findByName("%1");
//		List<Project> list_findByNameOK_3=projectRepository.findByName("%O%");
//		List<Project> list_findByNameFail=projectRepository.findByName("%abc%");

//		Page<Project> list_project_paging=pagingAndSortingProject.findAll(new PageRequest(0,5));
//		list_project_paging.getTotalPages();
//		list_project_paging.getTotalElements();
//		list_project_paging.getNumber();


//		Project project_find = projectRepository.findOne(1);
//		project_find.setAccountOwner("Test update bla bla bla");
//		projectRepository.save(project_find);
//		projectRepository.delete(1);
//		projectRepository.deleteAll();

//		taskRepository.findAll();

//		Page<Task> page_task_1 = pagingAndSortingTask.findAll(new PageRequest(0, 5));
//		int num_pages=page_task_1.getTotalPages();
//		for (int i=1;i<num_pages;i++){
//			Page<Task> page_task = pagingAndSortingTask.findAll(new PageRequest(i, 5));
//			page_task.getContent();
//		}

		/*
		 * Test method : testing
		 */

		return "home";
	}
	
}
