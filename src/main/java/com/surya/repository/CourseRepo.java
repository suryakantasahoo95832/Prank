package com.surya.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surya.entity.CourseEntity;

public interface CourseRepo extends JpaRepository<CourseEntity,Integer>{

}
