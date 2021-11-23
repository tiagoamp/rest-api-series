package com.tiagoamp.booksapi.repository;

import com.tiagoamp.booksapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    Role findByName(String name);

}
