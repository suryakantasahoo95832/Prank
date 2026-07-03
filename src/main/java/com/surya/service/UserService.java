package com.surya.service;

import com.surya.Binding.LoginForm;
import com.surya.Binding.SignUpForm;
import com.surya.Binding.UnlockForm;

public interface UserService {

	public String login(LoginForm form);
	public boolean signUp(SignUpForm form); 
	public String unlockAccount(UnlockForm form);
	public String forgotPwd(String email);
}
