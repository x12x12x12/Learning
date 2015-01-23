package com.proj.repository;

import com.proj.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by F.U.C.K on 23-Jan-15.
 */
public interface PagingAndSortingTask extends PagingAndSortingRepository <Task,Integer>{
}
