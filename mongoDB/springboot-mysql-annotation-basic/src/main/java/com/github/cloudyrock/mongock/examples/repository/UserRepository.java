package com.github.cloudyrock.mongock.examples.repository;


import com.github.cloudyrock.mongock.examples.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}