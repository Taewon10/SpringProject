//SpringProject/src/main/java/com/controller/SpringProject/MainController.java
package com.controller.SpringProject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping(value = "/")
	public String index() {
		System.out.println("index 호출");
		return "/index";
	}

}
