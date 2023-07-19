package com.myspring.pro30.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("fileDownloadController1")
public class FileDownloadController1 {

	private static String CURR_IMAGE_REPO_PATH ="C:\\board\\article_image";
	
	@RequestMapping(value = "/download1", method = RequestMethod.GET)
	public void download(@RequestParam("imageFileName") String imageFileName,  	@RequestParam("articleNO") String articleNO, HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		
		OutputStream out = response.getOutputStream();
		
		String downFile = CURR_IMAGE_REPO_PATH + "\\" + articleNO+ "\\" + imageFileName;
		
		
		File file = new File(downFile);
		
		String encodedFileName = URLEncoder.encode(imageFileName, "UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		
		//HTTP ���� ����� "Cache-Control" �ʵ带 �����Ͽ� ĳ�� ��� �����ϴ� ���� �ǹ��մϴ�. "no-cache" ���� ĳ�õ� ������ ������� �ʰ� �׻� �����κ��� ���ο� ������ ��û�ؾ� ���� ��Ÿ���ϴ�. 
		//�̷��� �����ϸ� �������� ĳ�õ� ������ ������� �ʰ� �׻� �����κ��� ���ο� �����͸� �������� �˴ϴ�.
		response.addHeader("Content-disposition", "attachment; fileName=" + encodedFileName);
//		response.addHeader("Content-disposition", "attachment; fileName=" + imageFileName);
		//HTTP ���� ����� "Content-disposition" �ʵ带 �߰��Ͽ� ÷�� ���� �ٿ�ε带 �����ϴ� ���� �ǹ��մϴ�. "attachment" ���� ���������� ������ ÷�� ���Ϸ� ó���ϵ��� �����ϴ� ������ �մϴ�. "fileName=" �κ��� ÷�� ������ �̸��� �����ϴ� �κ�����, ���⼭�� ���� fileName�� ����� ���� ����Ͽ� �ٿ�ε�Ǵ� ������ �̸��� �������� �����ϰ� �ֽ��ϴ�. �̷��� ������ ����� ���� �������� ������ ���Ϸ� �ٿ�ε��ϰ� �˴ϴ�.
		
		
		FileInputStream in = new FileInputStream(file);
		
		byte[] buffer = new byte[1024 * 8];
		
		while (true) {
			int count = in.read(buffer);
			if (count == -1) {
				break;
			}
			
			out.write(buffer, 0, count);
			
		}
		in.close();
		out.close();
	}
	
}
