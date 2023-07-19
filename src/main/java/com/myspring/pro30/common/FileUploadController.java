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
		logger.info("����� upload �޼��� ����");
		Map map = new HashMap();
		
		Enumeration enu=multipartRequest.getParameterNames();
		
		while(enu.hasMoreElements()) {
			String name=(String) enu.nextElement();
			String value=multipartRequest.getParameter(name);
			logger.info("�̰��� ��Ƽ��Ʈ������Ʈ�� ������ �Ķ����(�Ű�����)�̸���� �� : " + name + "," + value);
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
		
		logger.info( "��Ƽ��Ʈ������Ʈ�� ������  ���� ��ü ��ü"+fileNames);
		
		while(fileNames.hasNext()) {
			String fileName=fileNames.next();
			logger.info( "��Ƽ��Ʈ������Ʈ�� ������  ���� ��ü"+fileName);
			
			/* MultipartFile
			 * ��Ƽ��Ʈ ��û���� ���ŵ� ���ε�� ������ ǥ���Դϴ�.
			 * 
			 * ���� ������ �޸𸮿� ����ǰų� ��ũ�� �ӽ÷� ����˴ϴ�. �� ��� ��� ����ڴ� ���ϴ� ��� ���� ������ ���� ���� �Ǵ� ���� ����ҿ�
			 * �����ؾ� �մϴ�. �ӽ� ����Ҵ� ��û ó���� ������ �������ϴ�.
			 */
			
			MultipartFile  multipartFile=multipartRequest.getFile(fileName);
			String originalFileName = multipartFile.getOriginalFilename();
			logger.info( "��Ƽ��Ʈ������Ʈ�� ������ �������� ���� ��"+originalFileName);
			fileList.add(originalFileName);
			
			File file = new File(CURR_IMAGE_REPO_PATH +"\\"+ fileName);
			logger.info("���� ��ü:" + file);
			
			
			logger.info("������Ʈ���Ͽ뷮:" + multipartFile.getSize());
			
			if(multipartFile.getSize()!=0){  ////File Null Check
				
				logger.info("���� ���� ���� " + file.exists());  // �� ������ ���� ��ο��� ������ ����
				
				if(! file.exists()){
					logger.info("���丮 ��������� ���� " + file.getParentFile().mkdirs()); //��ο� �ش��ϴ� ���丮���� ����
					
					if(file.getParentFile().mkdirs()) {
						file.createNewFile(); //���� ���� ����
					}
				}
				
				multipartFile.transferTo(new File(CURR_IMAGE_REPO_PATH +"\\"+ originalFileName)); //�ӽ÷� ����� multipartFile�� ���� ���Ϸ� ����
				
			}
			
		}
		
		
		return fileList;
	}
}
