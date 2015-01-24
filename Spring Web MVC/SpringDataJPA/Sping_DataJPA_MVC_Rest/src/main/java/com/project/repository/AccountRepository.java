package com.project.repository;

import com.project.model.Account;
import com.project.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account,Integer> {

    @Query("select account from Account account where account.accountName=?1")
    Account findByName(String name);

    @Query("select account from Account account where account.accountName like ?1")
    List<Account> findLikeName(String name);

    @Query("select project from Project project where project.accountOwner.accountId=?1")
    List<Project> listOfProjectByOwnerId(Integer accountId);

    @Query("select project from Project project where project.accountOwner.accountName=?1")
    List<Project> listOfProjectByOwnerName(String name);

}
