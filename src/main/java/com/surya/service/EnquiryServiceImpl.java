package com.surya.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surya.Binding.DashboardResponse;
import com.surya.Binding.EnquiryForm;
import com.surya.Binding.EnquirySearchCriteria;
import com.surya.entity.CourseEntity;
import com.surya.entity.EnqStatusEntity;
import com.surya.entity.StudentEnqEntity;
import com.surya.entity.UserDtlsEntity;
import com.surya.repository.CourseRepo;
import com.surya.repository.EnqStatusRepo;
import com.surya.repository.StudentEnqRepo;
import com.surya.repository.UserDtlsRepo;

import jakarta.servlet.http.HttpSession;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	StudentEnqRepo repo;

	@Autowired
	CourseRepo courseRepo;

	@Autowired
	EnqStatusRepo enqRepo;

	@Autowired
	HttpSession session;

	@Autowired
	UserDtlsRepo userRepo;

	@Override
	public List<String> getCourseNames() {

		List<CourseEntity> all = courseRepo.findAll();

		ArrayList<String> arr = new ArrayList<>();

		for (CourseEntity course : all) {
			arr.add(course.getCourseName());
		}

		return arr;
	}

	@Override
	public List<String> getEnqStatus() {

		List<EnqStatusEntity> all = enqRepo.findAll();
		ArrayList<String> arr = new ArrayList<>();

		for (EnqStatusEntity enq : all) {
			arr.add(enq.getStatusName());
		}
		return arr;
	}

	@Override
	public DashboardResponse getdashboardData(Integer userId) {
		List<StudentEnqEntity> studentList = repo.findByUser_UserId(userId);

		if (studentList == null) {
			return null;
		}

		int total = studentList.size();
		int enrolled = (int) studentList.stream().filter(s -> "Enrolled".equalsIgnoreCase(s.getEnquiryStatus()))
				.count();

		int lost = (int) studentList.stream().filter(s -> "Lost".equalsIgnoreCase(s.getEnquiryStatus())).count();

		DashboardResponse response = new DashboardResponse();
		response.setTotalEnquiresCnt(total);
		response.setEnrolledCnt(enrolled);
		response.setLostCnt(lost);

		return response;
	}

	@Override
	public boolean upsertEnquiry(EnquiryForm form, String mode) {
        StudentEnqEntity entity =repo.findByPhno(form.getPhno());

	    if (mode.equals("add")) {
	        // ADD MODE
	        if (entity !=null) {
	            // phone already used → show error
	            return false;
	        }

	        // create new record
	       entity=new StudentEnqEntity();
	        BeanUtils.copyProperties(form, entity);

	        Integer userid = (Integer) session.getAttribute("userid");
	        Optional<UserDtlsEntity> byId = userRepo.findById(userid);
	       UserDtlsEntity userDtlsEntity = byId.get();
	       entity.setUser(userDtlsEntity);
	       

	        repo.save(entity);
	        return true;

	    } else if (mode.equals("edit")) {
	        // EDIT MODE
	        if (entity!=null) {
	            
	            BeanUtils.copyProperties(form, entity);
	            repo.save(entity);
	            return true;
	        }
	    }

	    return false;
	}


	@Override
	public List<StudentEnqEntity> getEnquiriesFilter(Integer userId, EnquirySearchCriteria criteria) {
		 Optional<UserDtlsEntity> byId = userRepo.findById(userId);
		 
	        if(byId.isPresent()) {
	        	UserDtlsEntity userDtlsEntity = byId.get();
	        	List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
	        	
	        	if(criteria.getCourseName()!=null & criteria.getCourseName()!="") {
	        		enquiries=enquiries.stream().filter(e-> e.getCourseName().equals(criteria.getCourseName())).collect(Collectors.toList());
	        	}
	        	
	        	if(criteria.getEnquiryStatus()!=null & criteria.getEnquiryStatus()!="") {
	        		enquiries=enquiries.stream().filter(e-> e.getEnquiryStatus().equals(criteria.getEnquiryStatus())).collect(Collectors.toList());
	        	}
	        	if(criteria.getClassMode()!=null & criteria.getClassMode()!="") {
	        		enquiries=enquiries.stream().filter(e-> e.getClassMode().equals(criteria.getClassMode())).collect(Collectors.toList());
	        	}
	        	
	        	return enquiries;
	        }
		return null;
	}

	@Override
	public List<StudentEnqEntity> getEnquiry(Integer userid) {
        Optional<UserDtlsEntity> byId = userRepo.findById(userid);
        if(byId.isPresent()) {
        	UserDtlsEntity userDtlsEntity = byId.get();
        	List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
        	return enquiries;
        }
		//List<StudentEnqEntity> byUser_UserId = repo.findByUser_UserId(userid);

		return null;
	}
	

}
