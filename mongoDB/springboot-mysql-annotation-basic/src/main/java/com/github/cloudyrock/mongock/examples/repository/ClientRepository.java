package com.github.cloudyrock.mongock.examples.repository;


import com.github.cloudyrock.mongock.examples.domain.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {

}