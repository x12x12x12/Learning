package com.project.repository;

import com.project.model.Task;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PagingAndSortingTask extends PagingAndSortingRepository <Task,Integer>{
}
