package com.tiagoamp.booksapi.repository;

import com.tiagoamp.booksapi.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Integer> {

    AppUser findByUsername(String username);
 
}
