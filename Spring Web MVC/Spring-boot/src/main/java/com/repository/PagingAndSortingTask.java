package com.repository;

import com.model.Task;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PagingAndSortingTask extends PagingAndSortingRepository <Task,Integer>{
}
