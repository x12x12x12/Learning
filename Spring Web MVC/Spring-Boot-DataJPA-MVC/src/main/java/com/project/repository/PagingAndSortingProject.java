package com.project.repository;

import com.project.model.Project;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

//@RestResource(exported = false)
public interface PagingAndSortingProject extends PagingAndSortingRepository<Project,Integer>{
}
