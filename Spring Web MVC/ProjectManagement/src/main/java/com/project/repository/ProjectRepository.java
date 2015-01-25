package com.project.repository;

import com.project.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project,String> {
}
