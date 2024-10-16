package user.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import user.bean.UserUploadDTO;
import user.service.ObjectStorageService;
import user.service.UserService;
import user.service.UserUploadService;

@Controller
@RequestMapping(value="user")
public class UserUploadController {
	@Autowired
	private UserUploadService userUploadService;
	@Autowired
	private ObjectStorageService objectStorageService;
	
	private String bucketName = "bitcamp-9th-bucket-91";
	
	@RequestMapping(value="uploadForm")
	public String uploadForm() {
		return "/upload/uploadForm";
	}
	
	@RequestMapping(value="uploadAJaxForm")
	public String uploadAJaxForm() {
		return "/upload/uploadAJaxForm";
	}

	
	//1개
	/*
	@RequestMapping(value="upload", method=RequestMethod.POST)
	@ResponseBody
	public String upload(@ModelAttribute UserUploadDTO userUploadDTO,
						 @RequestParam MultipartFile img,
						 HttpSession session) {
		
		//실제폴더
		String filePath = session.getServletContext().getRealPath("WEB-INF/storage");
		System.out.println("실제 폴더 = " + filePath);
				
		String imageOriginalFileName = img.getOriginalFilename();
		
		File file = new File(filePath, imageOriginalFileName);
		
		try {
			img.transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String result = "<span>"
					  + "<img src='/spring/storage/" + imageOriginalFileName + "' width='200' height='200'>"
					  + "</span>";
		
		
		System.out.println(userUploadDTO.getImageName() + ", "
						 + userUploadDTO.getImageContent() + ", "
						 + userUploadDTO.getImageFileName() + ", "
						 + userUploadDTO.getImageOriginalFileName());
		
		userUploadDTO.setImageOriginalFileName(imageOriginalFileName);
		
		//DB
		
		
		return result;
	}
	*/
	
	//2개 이상
	/*
	@RequestMapping(value="upload", method=RequestMethod.POST)
	@ResponseBody
	public String upload(@ModelAttribute UserUploadDTO userUploadDTO,
						 @RequestParam MultipartFile[] img,
						 HttpSession session) {
		
		//실제폴더
		String filePath = session.getServletContext().getRealPath("WEB-INF/storage");
		System.out.println("실제 폴더 = " + filePath);
				
		String imageOriginalFileName;
		File file;
		String result;
		
		//-----------------------
		imageOriginalFileName = img[0].getOriginalFilename();
		file = new File(filePath, imageOriginalFileName);
		
		try {
			img[0].transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		result = "<span>"
			   + "<img src='/spring/storage/" + imageOriginalFileName + "' width='200' height='200'>"
			   + "</span>";
		//-----------------------
		imageOriginalFileName = img[1].getOriginalFilename();
		file = new File(filePath, imageOriginalFileName);
		
		try {
			img[1].transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		result += "<span>"
			   + "<img src='/spring/storage/" + imageOriginalFileName + "' width='200' height='200'>"
			   + "</span>";

		return result;
	}
	*/
	
	//1개 또는 여러개(드래그)
	//파일명엔 한글 또는 공백이 있어도 업로드가 된다.
	@RequestMapping(value="upload", method=RequestMethod.POST, produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String upload(@ModelAttribute UserUploadDTO userUploadDTO,
						 @RequestParam("img[]") List<MultipartFile> list,
						 HttpSession session) {
		
		//실제폴더
		String filePath = session.getServletContext().getRealPath("WEB-INF/storage");
		System.out.println("실제 폴더 = " + filePath);
		
		String imageFileName;
		String imageOriginalFileName;
		File file;
		String result = "";
		
		//파일들을 모아서 DB로 보내기
		List<UserUploadDTO> imageUploadList = new ArrayList<>();
		
		for(MultipartFile img : list) {
			
			//imageFileName = UUID.randomUUID().toString();
			
			//네이버 클라우드 Object Storage -----------------------------
			imageFileName = objectStorageService.uploadFile(bucketName, "storage/", img);
			//--------------------------------------------------------
			
			imageOriginalFileName = img.getOriginalFilename();
			file = new File(filePath, imageOriginalFileName);
			
			try {
				img.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			try {
				result += "<span>"
					   + "<img src='/spring/storage/"
						
//					   + URLEncoder.encode(imageOriginalFileName, "UTF-8") => 파일명에 공백이 있으면 업로드가 안된다.
					   
					   + imageOriginalFileName
					   + "' width='200' height='200'>"
					   + "</span>";
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
			
			UserUploadDTO dto = new UserUploadDTO();
			dto.setImageName(userUploadDTO.getImageName());
			dto.setImageContent(userUploadDTO.getImageContent());
			dto.setImageFileName(imageFileName);
			dto.setImageOriginalFileName(imageOriginalFileName);
			
			imageUploadList.add(dto);
			
		}//for
		
		//DB
		userUploadService.upload(imageUploadList);
		
		return result;
	}
	
	@RequestMapping(value="uploadList")
	public ModelAndView uploadList() {
		List<UserUploadDTO> list = userUploadService.uploadList();
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);
		mav.setViewName("/upload/uploadList");
		return mav;
	}
	
	@RequestMapping(value="uploadView")
	public String uploadView(@RequestParam String seq, Model model) {
		UserUploadDTO userUploadDTO = userUploadService.getUploadDTO(seq);
		model.addAttribute("userUploadDTO", userUploadDTO);
		
		return "/upload/uploadView";
	}
	
	@RequestMapping(value="uploadUpdateForm")
	public String uploadUpdateForm(@RequestParam String seq, Model model) {
		UserUploadDTO userUploadDTO = userUploadService.getUploadDTO(seq);
		model.addAttribute("userUploadDTO", userUploadDTO);
		
		return "/upload/uploadUpdateForm";
	}
	
	@RequestMapping(value="uploadUpdate")
	@ResponseBody
	public String uploadUpdate(@ModelAttribute UserUploadDTO userUploadDTO,
						 @RequestParam("img") MultipartFile img) {
		
		userUploadService.uploadUpdate(userUploadDTO, img);
		return "이미지 수정 완료";
	}
	
	@RequestMapping(value="uploadDelete")
	@ResponseBody
	public void uploadDelete(@RequestParam String[] check) {
		for(String seq : check) {
			System.out.println(seq);			
		}
		userUploadService.uploadDelete(check);
	}
}


















/*
 * @Controller: 이 클래스가 Spring MVC 컨트롤러임을 나타냅니다. Spring이 이 클래스를 컨트롤러로 인식하게 됩니다.
 * 
 * @RequestMapping(value="user"): 이 컨트롤러의 모든 메소드에 대해 기본 URL 경로를 user로 설정합니다.
 * java 코드 복사
 * 
 * @RequestMapping(value="uploadForm") public String uploadForm() { return
 * "/upload/uploadForm"; } uploadForm 메소드: uploadForm URL을 처리하는 메소드입니다.
 * 
 * @RequestMapping(value="uploadForm"): /user/uploadForm URL 요청을 처리합니다. 반환값:
 * /upload/uploadForm 경로의 뷰를 반환합니다. 업로드 메소드 java 코드 복사
 * 
 * @RequestMapping(value="upload", method=RequestMethod.POST)
 * 
 * @ResponseBody public String upload(@ModelAttribute UserUploadDTO
 * userUploadDTO,
 * 
 * @RequestParam("img[]") List<MultipartFile> list, HttpSession session) {
 * upload 메소드: 파일 업로드를 처리하는 메소드입니다.
 * 
 * @RequestMapping(value="upload", method=RequestMethod.POST): /user/upload URL로
 * POST 요청을 처리합니다.
 * 
 * @ResponseBody: 메소드가 반환하는 값을 HTTP 응답 본문에 직접 작성하도록 합니다. 즉, 반환된 문자열이 응답으로 전송됩니다.
 * 매개변수:
 * 
 * @ModelAttribute UserUploadDTO userUploadDTO: 클라이언트에서 전송된 데이터를 UserUploadDTO
 * 객체로 매핑합니다.
 * 
 * @RequestParam("img[]") List<MultipartFile> list: 여러 파일을 업로드하기 위해
 * List<MultipartFile>로 매핑합니다. 클라이언트에서 img[] 이름으로 전송된 파일들이 이 리스트에 담깁니다.
 * HttpSession session: 현재 HTTP 세션을 통해 웹 애플리케이션의 특정 세션 정보를 가져옵니다. java 코드 복사
 * String filePath = session.getServletContext().getRealPath("WEB-INF/storage");
 * System.out.println("실제 폴더 = " + filePath); 파일 경로 설정: 서버의 실제 파일 저장 경로를 얻기 위해
 * getRealPath() 메소드를 사용합니다. WEB-INF/storage 폴더에 파일을 저장할 것입니다. 디버깅 로그 출력: 현재의 파일
 * 경로를 콘솔에 출력하여 확인합니다. java 코드 복사 String imageOriginalFileName; File file;
 * String result = ""; 변수 선언: imageOriginalFileName: 업로드된 파일의 원본 파일 이름을 저장할
 * 변수입니다. file: File 객체를 생성할 때 사용할 변수입니다. result: 최종적으로 반환할 HTML 문자열을 저장할 빈
 * 문자열입니다. java 코드 복사 List<UserUploadDTO> imageUploadList = new ArrayList<>();
 * 리스트 생성: 업로드된 파일에 대한 정보(UserUploadDTO 객체)를 저장할 List를 생성합니다. 이 리스트는 나중에 DB에 저장할
 * 때 사용할 수 있습니다. java 코드 복사 for(MultipartFile img : list) { 파일 업로드 반복 처리: list에
 * 있는 각 파일을 반복하면서 업로드를 처리합니다. java 코드 복사 imageOriginalFileName =
 * img.getOriginalFilename(); file = new File(filePath, imageOriginalFileName);
 * 파일 정보 가져오기: getOriginalFilename(): 업로드된 파일의 원본 파일 이름을 가져옵니다. new
 * File(filePath, imageOriginalFileName): 저장할 파일 객체를 생성합니다. 여기서 filePath와
 * imageOriginalFileName을 결합하여 전체 경로를 만듭니다. java 코드 복사 try {
 * img.transferTo(file); } catch (IllegalStateException e) {
 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } 파일 저장:
 * transferTo(file) 메소드를 사용하여 클라이언트에서 업로드한 파일을 지정한 경로에 저장합니다. 예외 처리:
 * IllegalStateException이나 IOException이 발생할 경우 스택 트레이스를 출력하여 에러를 진단합니다. java 코드
 * 복사 try { result += "<span>" + "<img src='/spring/storage/" +
 * URLEncoder.encode(imageOriginalFileName, "UTF-8") +
 * "' width='200' height='200'>" + "</span>"; } catch
 * (UnsupportedEncodingException e) { e.printStackTrace(); } HTML 결과 문자열 생성:
 * 업로드된 이미지 파일을 보여주기 위해 HTML <img> 태그를 생성합니다. src 속성에 이미지의 경로를 설정합니다.
 * URLEncoder.encode(...): 이미지 파일 이름을 URL 인코딩하여 공백이나 특수 문자가 포함된 경우에도 문제가 없도록
 * 합니다. 생성된 HTML 태그를 result 문자열에 추가합니다. 예외 처리: UnsupportedEncodingException이 발생할
 * 경우 스택 트레이스를 출력합니다. java 코드 복사 UserUploadDTO dto = new UserUploadDTO();
 * dto.setImageName(userUploadDTO.getImageName());
 * dto.setImageContent(userUploadDTO.getImageContent());
 * dto.setImageFileName("");
 * dto.setImageOriginalFileName(imageOriginalFileName);
 * 
 * imageUploadList.add(dto); DTO 객체 생성: 각 파일에 대한 UserUploadDTO 객체를 생성하고, 사용자로부터
 * 입력받은 정보(imageName, imageContent, imageOriginalFileName)를 설정합니다. 리스트에 추가: 생성한
 * UserUploadDTO 객체를 imageUploadList에 추가합니다. 이 리스트는 나중에 데이터베이스에 저장하는 데 사용할 수
 * 있습니다. java 코드 복사 //DB DB 저장 부분: 현재는 주석으로 되어 있지만, 이후에 imageUploadList를 사용하여
 * 데이터베이스에 파일 정보를 저장하는 로직이 들어갈 수 있습니다. java 코드 복사 return result; } } 메소드 반환:
 * 최종적으로 생성된 HTML 문자열을 반환합니다. 이 문자열은 클라이언트에게 전송되어 업로드한 이미지를 화면에 표시합니다. 이 코드의 전체
 * 흐름은 사용자가 파일을 업로드하면 해당 파일을 지정된 경로에 저장하고, 그 결과를 HTML 형식으로 만들어 반환하는 과정을 포함합니다.
 * 필요한 경우 데이터베이스에 파일 정보를 저장하는 로직을 추가하여 완전한 파일 업로드 기능을 구현할 수 있습니다
 * 
 * 
 * 
 * 
 * 
 */





