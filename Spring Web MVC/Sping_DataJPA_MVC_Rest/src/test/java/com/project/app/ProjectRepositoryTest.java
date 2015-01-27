package com.project.app;

import com.project.config.ApplicationConfig;
import com.project.repository.ProjectRepository;
import com.project.service.ProjectService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles("test")
@Transactional
public class ProjectRepositoryTest {

//    @Resource
//    ProjectRepository projectRepository;

    @Before
    public void init(){

    }

    @Test
    public void testInsert(){

    }
}
