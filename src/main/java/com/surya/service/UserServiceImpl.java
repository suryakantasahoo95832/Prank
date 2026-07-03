package com.surya.service;
import com.surya.utility.EmailUtils;
import com.surya.utility.PasswordUtils;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surya.Binding.LoginForm;
import com.surya.Binding.SignUpForm;
import com.surya.Binding.UnlockForm;
import com.surya.entity.UserDtlsEntity;
import com.surya.repository.UserDtlsRepo;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserDtlsRepo repo;
	@Autowired
    EmailUtils email;
	@Autowired
	HttpSession session;
	
    private final PasswordUtils passwordUtils;

    UserServiceImpl(PasswordUtils passwordUtils) {
        this.passwordUtils = passwordUtils;
    }

	@Override
	public String login(LoginForm form) {
		
		UserDtlsEntity entity = repo.findByEmailAndPwd(form.getUserName(), form.getPassword());
		
		if(entity==null) {
			return "Invallid Credential";
		}
		
		if(entity.getAccStatus().equals("Locked")) {
			return "Account is Locked,first unlock it..";
		}
		
		session.setAttribute("userid", entity.getUserId());
		
		return "success";
	}

	@Override
	public boolean signUp( SignUpForm form) {
		
		if(repo.findByEmail(form.getEmail())!=null) {
			return false;
		}
		
		
	  UserDtlsEntity entity =new UserDtlsEntity();
	  BeanUtils.copyProperties(form, entity);
	  
	  //create random Password==
	  String rndPass = passwordUtils.getRndPass();
	  entity.setPwd(rndPass);
	  
	  //set acnt status as locked=
	  entity.setAccStatus("Locked");
	  
	  //insert record==
	  
	  repo.save(entity);
	  
	  //mail sender==
		String to=form.getEmail();
		String subject="Unlock Your Account--";
		StringBuffer body = new StringBuffer("");
		body.append("<h1> Use bellow temporary passwod to unlock your account</h1>");
		body.append("Temporary password : "+rndPass);
		body.append("<br>");
		body.append("<a href=\"http://localhost:8080/unlock?email="+to+"\">Click Here..</a>");
		email.sendEmail(to, subject, body.toString());
		
	  
		
		return true;
	}

	@Override
	public String unlockAccount(UnlockForm form) {
		
		UserDtlsEntity entity = repo.findByEmail(form.getEmail());
	
		if(entity.getAccStatus().equals("UnLocked")) {
			return "unlock";
		}
		
		
		
		if(entity.getPwd().equals(form.getTempPwd())) {
			entity.setAccStatus("UnLocked");
			entity.setPwd(form.getConformPwd());
			repo.save(entity);
			return "done";
		}
		
		
		return "notmatch";
	}

	@Override
	public String forgotPwd(String gmail) {
		UserDtlsEntity entity = repo.findByEmail(gmail);
		if(entity==null) {
			return "Invallid Email";
		}
		
		if(entity.getAccStatus().equals("Locked")) {
			return "Your Account is Locked,,,Unlock and login...";
		}
		
		
		
		String to=gmail;
		String sub="Recovery Password--";
        
		StringBuffer body=new StringBuffer("");
		
		body.append("<h3>Use Your Password</h3>");
		body.append("Your Password : "+entity.getPwd());
		body.append("<br>");
		body.append("<a href=\"http://localhost:8080/login\">Click Here and LOGIN</a>");
		
		boolean flag = email.recoveryMail(to, sub, body.toString());
		if(flag)
		return "success";
		
		return "Something Error--";
	}

}
