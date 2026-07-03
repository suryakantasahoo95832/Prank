package com.surya.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.surya.Binding.DashboardResponse;
import com.surya.Binding.EnquiryForm;
import com.surya.Binding.EnquirySearchCriteria;
import com.surya.entity.StudentEnqEntity;
import com.surya.repository.StudentEnqRepo;
import com.surya.service.EnquiryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EnquiryController {

	@Autowired
	HttpSession session;
	@Autowired
	EnquiryService service;
	
	@Autowired
	StudentEnqRepo repo;
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "index";
	}
	
	
	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {
		 
		int userid=(Integer) session.getAttribute("userid");
		
		DashboardResponse getdashboardData = service.getdashboardData(userid);
		if(getdashboardData.getTotalEnquiresCnt()==0	) {
		
			model.addAttribute("empty", "no student enquiry by You..");
		}
		model.addAttribute("data", getdashboardData);
		
		
		
		
		return "dashboard";
	}
	
	
	@GetMapping("/enquiry")
	public String addEnquiryPage(Model model) {
		EnquiryForm form =new EnquiryForm();
		
		init(model);
		
		model.addAttribute("enqForm", form);
		model.addAttribute("mode","add");
		return "add-enquiry";
	}
	
	public void init(Model model) {
		List<String> courses = service.getCourseNames();
		List<String> statuses=service.getEnqStatus();
		model.addAttribute("courses",courses);
		model.addAttribute("statuses", statuses);
	}
	
	@PostMapping("/add")
	public String HandleAddEnqSave(@ModelAttribute("enqForm") EnquiryForm form, @RequestParam("mode") String mode, Model model) {
		
	
	    if (mode == null) {
	        mode = "add";  // default
	    }

	    init(model);
	    boolean status = service.upsertEnquiry(form, mode);

	    if (status) {
	        model.addAttribute("succmsg", mode.equals("edit") ? "Details Updated Successfully" : "Details Added Successfully");
	    } else {
	        model.addAttribute("errmsg", "Phone number already exists!");
	    }

	  
	    return "add-enquiry";
	}

	
	
	@GetMapping("/enquires")
	public String viesEnquiryPage(Model model) {
		
		init(model);
		
		Integer userid=(Integer)session.getAttribute("userid");
		List<StudentEnqEntity> list = service.getEnquiry(userid);
		model.addAttribute("list", list);
		if(list==null) {
			   model.addAttribute("empty","No Record Found");
		}
	 
	    
		return "view-enquires";
	}
	
	@GetMapping("/edit")
	public String editEnq(@RequestParam("id") Integer sid,Model model) {
	//	System.out.println("Editing id-----------------------"+sid);
		StudentEnqEntity byId = repo.findById(sid).get();
		
		EnquiryForm form=new EnquiryForm();
		init(model);
		BeanUtils.copyProperties(byId, form);
		model.addAttribute("enqForm", form);
		model.addAttribute("mode","edit");
		
		return "add-enquiry";
	}
	
	@GetMapping("/filter")
	public String filter(Model model,@RequestParam String cname,@RequestParam String status,@RequestParam String mode) {
		
		EnquirySearchCriteria criteria=new EnquirySearchCriteria();
		criteria.setClassMode(mode);
		criteria.setCourseName(cname);
		criteria.setEnquiryStatus(status);
	
		Integer userid = (Integer)session.getAttribute("userid");
		
		List<StudentEnqEntity> filter = service.getEnquiriesFilter(userid, criteria);
		model.addAttribute("list", filter);
		
		return "view-filter-enquires";
	}
	
}
