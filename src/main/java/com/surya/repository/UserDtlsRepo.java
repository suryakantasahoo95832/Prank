package com.surya.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surya.entity.UserDtlsEntity;

public interface UserDtlsRepo extends JpaRepository<UserDtlsEntity, Integer>{

	 UserDtlsEntity findByEmail(String email);

	 UserDtlsEntity findByEmailAndPwd(String email,String pwd);
}
