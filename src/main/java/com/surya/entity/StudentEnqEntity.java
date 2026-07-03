package com.surya.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class StudentEnqEntity {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer enquiryId;

	    private String studentName;

	    @Column(unique = true, nullable = false)
	    private Long phno;

	    private String classMode;

	    private String courseName;

	    private String enquiryStatus ; // Default value

	    @CreationTimestamp
	    private LocalDate createdDate;

	    @UpdateTimestamp
	    private LocalDate updatedDate;

	    // Many enquiries belong to one user
	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private UserDtlsEntity user;
	

	
	
	
	
	
	
	
}
