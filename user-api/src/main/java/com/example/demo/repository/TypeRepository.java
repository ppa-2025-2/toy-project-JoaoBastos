package com.example.demo.repository;

import com.example.demo.repository.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Set<Type> findByNameIn(Collection<String> names);
}