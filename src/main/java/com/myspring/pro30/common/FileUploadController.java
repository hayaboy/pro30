package com.myspring.pro30.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;



//@Controller("fileUploadController")
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	private static final String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String form(HttpServletRequest request, HttpServletResponse response)throws Exception{
		return "uploadForm";
	}

	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView upload(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception{
		logger.info("여기는 upload 메서드 영역");
		Map map = new HashMap();
		
		Enumeration enu=multipartRequest.getParameterNames();
		
		while(enu.hasMoreElements()) {
			String name=(String) enu.nextElement();
			String value=multipartRequest.getParameter(name);
			logger.info("이것은 멀티파트리퀘스트가 가져온 파라미터(매개변수)이름들과 값 : " + name + "," + value);
			map.put(name, value);
			
		}
		List<String> fileList= fileProcess(multipartRequest);
		
		map.put("fileList", fileList);
		ModelAndView mav = new ModelAndView();
		mav.addObject("map", map);
		mav.setViewName("result");
		
		return mav;
	}
	
	public List<String>fileProcess(MultipartHttpServletRequest multipartRequest) throws Exception{
		
		List<String> fileList= new ArrayList<String>();
		
		
		Iterator<String> fileNames=multipartRequest.getFileNames();
		
		logger.info( "멀티파트리퀘스트가 가져온  파일 전체 객체"+fileNames);
		
		while(fileNames.hasNext()) {
			String fileName=fileNames.next();
			logger.info( "멀티파트리퀘스트가 가져온  파일 객체"+fileName);
			
			/* MultipartFile
			 * 멀티파트 요청에서 수신된 업로드된 파일의 표현입니다.
			 * 
			 * 파일 내용은 메모리에 저장되거나 디스크에 임시로 저장됩니다. 두 경우 모두 사용자는 원하는 경우 파일 내용을 세션 수준 또는 영구 저장소에
			 * 복사해야 합니다. 임시 저장소는 요청 처리가 끝나면 지워집니다.
			 */
			
			MultipartFile  multipartFile=multipartRequest.getFile(fileName);
			String originalFileName = multipartFile.getOriginalFilename();
			logger.info( "멀티파트리퀘스트가 가져온 오리지널 파일 명"+originalFileName);
			fileList.add(originalFileName);
			
			File file = new File(CURR_IMAGE_REPO_PATH +"\\"+ fileName);
			logger.info("파일 객체:" + file);
			
			
			logger.info("멀피파트파일용량:" + multipartFile.getSize());
			
			if(multipartFile.getSize()!=0){  ////File Null Check
				
				logger.info("파일 존재 여부 " + file.exists());  // 이 시점의 파일 경로에는 파일이 없음
				
				if(! file.exists()){
					logger.info("디렉토리 만들었는지 여부 " + file.getParentFile().mkdirs()); //경로에 해당하는 디렉토리들을 생성
					
					if(file.getParentFile().mkdirs()) {
						file.createNewFile(); //이후 파일 생성
					}
				}
				
				multipartFile.transferTo(new File(CURR_IMAGE_REPO_PATH +"\\"+ originalFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송
				
			}
			
		}
		
		
		return fileList;
	}
}
