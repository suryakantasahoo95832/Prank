package com.surya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surya.entity.StudentEnqEntity;

public interface StudentEnqRepo extends JpaRepository<StudentEnqEntity, Integer>{

	 List<StudentEnqEntity> findByUser_UserId(Integer userId);

     StudentEnqEntity findByPhno(Long phno);
}
