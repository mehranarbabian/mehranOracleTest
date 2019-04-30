package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<EntityUser,Integer> {

}
