package com.project.repository;

import com.project.model.Account;
import com.project.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RestResource(path = "account")
public interface AccountRepository extends CrudRepository<Account,Integer> {

    @RestResource(exported = false)
    @Query("select account from Account account where account.accountName=?1")
    Account findByName(String name);

    @RestResource(exported = false)
    @Query("select account from Account account where account.accountName like ?1")
    List<Account> findLikeName(String name);

    @RestResource(exported = false)
    @Query("select project from Project project where project.accountOwner.accountId=?1")
    List<Project> listOfProjectByOwnerId(Integer accountId);

    @RestResource(exported = false)
    @Query("select project from Project project where project.accountOwner.accountName=?1")
    List<Project> listOfProjectByOwnerName(String name);

    @Override
    @RestResource(exported = true)
    void delete(Integer integer);

//    @Override
//    @RestResource(exported = false)
//    void delete(Account account);

}