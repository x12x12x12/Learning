package com.project.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.project.model.Account;
import com.project.model.Project;
import com.project.model.Task;
import com.project.repository.*;
import com.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	AccountRepository accountRepository;

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
//		for (int i = 0; i <4 ; i++) {
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
//		}

		/**
		 * Test method : OK
		 */
		Account account_find=accountRepository.findOne(1);
		List<Project> list_account_project=accountRepository.listOfProjectByOwnerId(account_find.getAccountId());
		for (Project project_entry : list_account_project){
			project_entry.setListOfTask(projectRepository.getListOfTaskById(project_entry.getProjectId()));
			for (Task entrytask : project_entry.getListOfTask()){
				entrytask.setTaskChild(taskRepository.getListOfTaskChildById(entrytask.getTaskId()));
			}
		}
//		Account account_find_by_name=accountRepository.findByName("quan");
//		List<Project> list_account_project_by_name=accountRepository.listOfProjectByOwnerName(account_find.getAccountName());

		Iterable<Project> list_project=projectRepository.findAll();

		Iterable<Task> list_task=taskRepository.findAll();

//		accountRepository.delete(1);

//		Long count= projectRepository.count();
//
//		projectRepository.findByName("IOS%");
//		projectRepository.findByName("%1");
//		projectRepository.findByName("%O%");
//		projectRepository.findByName("%abc%");
//
//		Page<Project> list_project_paging=pagingAndSortingProject.findAll(new PageRequest(0,5));
//		list_project_paging.getTotalPages();
//		list_project_paging.getTotalElements();
//		list_project_paging.getNumber();
//
//
//		Project project_find = projectRepository.findOne(1);
//		project_find.setName("Test update");
//		projectRepository.save(project_find);
//		projectRepository.delete(1);
//		projectRepository.deleteAll();


//		Page<Task> page_task = pagingAndSortingTask.findAll(new PageRequest(0, 5));
//		int num_pages=page_task.getTotalPages();
//		for (int i=1;i<num_pages;i++){
//			pagingAndSortingTask.findAll(new PageRequest(i, 5));
//			page_task.getContent();
//		}

		/*
		 * Test method : testing
		 */

		return "home";


	}
}
