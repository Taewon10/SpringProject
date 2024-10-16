package user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import user.bean.UserDTO;
import user.service.UserService;

@Controller
@RequestMapping(value = "user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "writeForm", method = RequestMethod.GET)
	public String writeForm() {
		return "/user/writeForm";
	}

	@RequestMapping(value = "getExistId", method = RequestMethod.POST)
	@ResponseBody
	public String getExistId(String id) {
		return userService.getExistId(id); // 단순 문자열이 아니라 JSP 파일명으로 인식
	}

	@RequestMapping(value = "write", method = RequestMethod.POST)
	@ResponseBody
	public void write(@ModelAttribute UserDTO userDTO) {
		userService.write(userDTO);
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(required = false, defaultValue = "1") String pg, Model model) {
		Map<String, Object> map2 = userService.list(pg);

		model.addAttribute("map2", map2);

		// model.addAttribute("list", map2.get("list"));
		// model.addAttribute("userPaging", map2.get("userPaging"));
		return "/user/list"; // => /WEB-INF/user/list.jsp
	}

	@RequestMapping(value = "updateForm", method = RequestMethod.GET)
	public String updateForm(@RequestParam String id, @RequestParam String pg, ModelMap modelMap) {
		UserDTO userDTO = userService.getUser(id);

		modelMap.put("pg", pg);
		modelMap.put("userDTO", userDTO);
		return "/user/updateForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public void update(@ModelAttribute UserDTO userDTO) {
		userService.update(userDTO);
	}

	@RequestMapping(value = "deleteForm", method = RequestMethod.GET)
	public ModelAndView deleteForm(@RequestParam String id, String pg) {
		ModelAndView mav = new ModelAndView();
		
	    UserDTO userDTO = userService.getUser(id); // 서비스 레이어를 사용한 데이터 가져오기

	    mav.addObject("userDTO", userDTO); // 가져온 userDTO 객체를 모델에 추가
		mav.addObject("id", id);
		mav.addObject("pg", pg);
		mav.setViewName("/user/deleteForm");
		return mav;
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public void delete(@RequestParam String id) {
		userService.delete(id);
	}

	@RequestMapping(value = "getExistPwd", method = RequestMethod.POST)
	@ResponseBody
	public UserDTO getExistPwd(@RequestParam String id, Model model) {
		//Spring은 객체를 자동으로 JSON 형태로 변환해줌.
		
		return userService.getExistPwd(id);
	}
	

	
	
}