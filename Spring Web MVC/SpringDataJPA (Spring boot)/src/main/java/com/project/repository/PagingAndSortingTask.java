package com.project.repository;

import com.project.model.Task;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

//@RestResource(exported = false)
public interface PagingAndSortingTask extends PagingAndSortingRepository <Task,Integer>{
}
