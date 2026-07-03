package com.surya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.surya.Binding.LoginForm;
import com.surya.Binding.SignUpForm;
import com.surya.Binding.UnlockForm;
import com.surya.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService service;

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("login", new LoginForm());
		return "login";
	}
	
	@PostMapping("/login")
	public String handelLogin(@ModelAttribute("login") LoginForm from,Model model) {
		String status = service.login(from);
		if(status.contains("success")) {
			return "redirect:/dashboard";  //// sending to url of another controller method///
		}
		model.addAttribute("errmsg",status);
		return "login";
	}
	
	
	
//===========================================================================
	@GetMapping("/signup")
	public String signUpPage(Model model) {
		model.addAttribute("user", new SignUpForm());
		return "signup";
	}

	@PostMapping("/signup")
	public String handleSignup(@ModelAttribute("user") SignUpForm form, Model model) {

		boolean signUp = service.signUp(form);
		if (signUp) {
			model.addAttribute("sucmsg", "check your email");
		} else {
			model.addAttribute("unsucmsg", "Email is already exist");
		}

		return "signup";
	}

//==================================================================	

	@GetMapping("/unlock")
	public String unlockPage(@RequestParam("email") String email, Model model) {
		UnlockForm form = new UnlockForm();
		form.setEmail(email);
		model.addAttribute("unlockForm", form);
		return "unlock";
	}

	@PostMapping("/unlock")
	public String handleUnlock(@ModelAttribute("unlockForm") UnlockForm unlockForm, Model model) {

		if (unlockForm.getNewPwd().equals(unlockForm.getConformPwd())) {
			if (service.unlockAccount(unlockForm).equals("done")) {
				// System.out.println(unlockForm);
				model.addAttribute("done", "Login with your Userid and Password");
				return "unlock";
			} else if (service.unlockAccount(unlockForm).equals("notmatch")) {
				model.addAttribute("notdone", "Temp password is incorrect,check your email");
				return "unlock";
			} else if (service.unlockAccount(unlockForm).equals("unlock")) {
				model.addAttribute("notdone", "Account is already unlocked : Go and Login-- ");
				return "unlock";
			}
		} else {
			model.addAttribute("notdone", "new password and conform password is not same::");
			return "unlock";
		}
		return "unlock";
	}

	// ===============================================================

	@GetMapping("/forgot")
	public String forgotPwdPage() {
		return "forgotPwd";
	}
	
	@PostMapping("/forgot")
	public String handleForgotpwdPage(@RequestParam String email,Model model) {
		String status = service.forgotPwd(email);
		if(status.contains("success")) {
			model.addAttribute("succmsg", "Check Your Email...");
			return "forgotPwd";
		}
		model.addAttribute("errmsg", status);
		return "forgotPwd";
	}
	
}










