package com.kong.cc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kong.cc.entity.Complain;

public interface ComplainRepository extends JpaRepository<Complain, Integer> {

}