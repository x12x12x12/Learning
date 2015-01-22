package com.proj.service;

import com.proj.model.Project;
import com.proj.repository.ProjectRepository;

import javax.annotation.Resource;

/**
 * Created by F.U.C.K on 22-Jan-15.
 */
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectRepository projectRepository;

    @Override
    public void add(Project project) {
        projectRepository.save(project);
    }
}
