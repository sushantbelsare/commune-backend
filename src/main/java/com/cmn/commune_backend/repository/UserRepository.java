package com.cmn.commune_backend.repository;

import com.cmn.commune_backend.entity.User;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

}
