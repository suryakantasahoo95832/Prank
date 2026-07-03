package com.surya.runner;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.surya.entity.CourseEntity;
import com.surya.entity.EnqStatusEntity;
import com.surya.repository.CourseRepo;
import com.surya.repository.EnqStatusRepo;

@Component
public class DataLoader implements ApplicationRunner{

	@Autowired
	CourseRepo courseRepo;
	
	@Autowired
	EnqStatusRepo enqRepo;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
	
		courseRepo.deleteAll();
		enqRepo.deleteAll();
		CourseEntity e1=new CourseEntity();
		CourseEntity e2=new CourseEntity();
		CourseEntity e3=new CourseEntity();
		e1.setCourseName("Java");
		e2.setCourseName("Python");
		e3.setCourseName("Mern");
		List<CourseEntity> l=Arrays.asList(e1,e2,e3);
		
		
		EnqStatusEntity eq1=new EnqStatusEntity();
		EnqStatusEntity eq2=new EnqStatusEntity();
		EnqStatusEntity eq3=new EnqStatusEntity();
		
		eq1.setStatusName("New");
		eq2.setStatusName("Enrolled");
		eq3.setStatusName("Lost");
		List<EnqStatusEntity> enq=Arrays.asList(eq1,eq2,eq3);
		
		
		courseRepo.saveAll(l);
		enqRepo.saveAll(enq);
		
	}

}
