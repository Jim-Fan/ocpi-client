package org.sdf.jimfan.ocpiclient.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OcpiConfigViewController {
	
	@GetMapping("/thymeleaf/message")
	public String messages(Model model) {
		model.addAttribute("message", "Hello, Thymeleaf");
		return "thymeleaf-1.html";
	}
}
