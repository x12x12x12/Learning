package com.proj.repository;

import com.proj.model.Task;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by F.U.C.K on 22-Jan-15.
 */
public interface TaskRepository extends CrudRepository<Task,Integer> {
//    public List<Task> findByNameAdndId
}
