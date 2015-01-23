package com.proj.repository;

import com.proj.model.Task;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PagingAndSortingTask extends PagingAndSortingRepository <Task,Integer>{
}
