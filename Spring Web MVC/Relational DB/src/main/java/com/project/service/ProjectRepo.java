package com.project.service;

import com.project.model.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepo extends CrudRepository<Project,Integer> {

}
