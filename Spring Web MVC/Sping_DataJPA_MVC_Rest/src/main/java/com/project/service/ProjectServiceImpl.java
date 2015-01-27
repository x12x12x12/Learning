package com.project.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by F.U.C.K on 22-Jan-15.
 */
public class ProjectServiceImpl implements ProjectService {

    @PersistenceContext private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager){
        this.entityManager=entityManager;
    }
}
