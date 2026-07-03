package com.surya.service;

import java.util.List;

import com.surya.Binding.DashboardResponse;
import com.surya.Binding.EnquiryForm;
import com.surya.Binding.EnquirySearchCriteria;
import com.surya.entity.StudentEnqEntity;

public interface EnquiryService {

	//for dropdown data use this 2 method for addEnquiry---
	public List<String> getCourseNames();
	public List<String> getEnqStatus();
   //-------------	
	
	
	
	public DashboardResponse getdashboardData(Integer userId);
	public boolean upsertEnquiry(EnquiryForm form,String mode);  //insert+update=upsert
	public List<StudentEnqEntity> getEnquiriesFilter(Integer userId ,EnquirySearchCriteria criteria);
	public List<StudentEnqEntity> getEnquiry(Integer userid);
}
