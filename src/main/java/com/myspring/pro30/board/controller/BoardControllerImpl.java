package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl implements BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardControllerImpl.class);

	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";

	@Autowired
	ArticleVO articleVO;

	@Autowired
	BoardService boardService;
	
	HttpSession session;

	@Override
	@RequestMapping(value = "/board/listArticles.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		logger.info(viewName);

		List<ArticleVO> articlesList = boardService.listArticles();

		ModelAndView mav = new ModelAndView(viewName);

		mav.addObject("articlesList", articlesList);
		logger.info("이것은 아티클리스트 객체" + articlesList);
		return mav;

	}

	
	
	@RequestMapping(value = "/board/articleForm.do", method =  RequestMethod.GET)
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
	
	
	@RequestMapping(value = "/board/replyForm.do", method =  RequestMethod.GET)
	private ModelAndView form2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		
		int parentNO = Integer.parseInt(request.getParameter("parentNO"));
		System.out.println("답글쓰기에서 넘어온 부모 글번호 : " + parentNO);
		//로그인한 사람이 계속 그 정보를 가지고 다녀야하므로 세션에 저장
		session=request.getSession();
		
		session.setAttribute("parentNO", parentNO);
		
		//logger.info("부모글" + parentNO);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
	
	
	
	@Override
	// 한개의 이미지 보여주기
	@RequestMapping(value = "/board/viewArticle.do", method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		
		articleVO = boardService.viewArticle(articleNO);
		logger.info("선택한 글번호" + articleNO);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article", articleVO);
		
		return mav;
	}

	@Override
	@RequestMapping(value = "/board/removeArticle.do", method = RequestMethod.POST)
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			boardService.removeArticle(articleNO);	
			message = "<script>";
			message += " alert('글을 삭제했습니다.');";
			message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}catch(Exception e) {
			message = "<script>";
			message += " alert('작업중 오류가 발생했습니다.다시 시도해 주세요.');";
			message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		    e.printStackTrace();
		}

		
		return resEnt;
	}

	
	
	
	
	@Override
	@RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			logger.info("새글 추가시 파라미터 이름 : " + name + "파라미터 값:" +  value);
			articleMap.put(name,value);
		}
			
			String imageFileName= upload(multipartRequest);
			logger.info("새글 추가시 이미지 파일명 : " + imageFileName );
			
			
			//해당 요청에 대한 세션에서 회원(member)가져오고 해당 멤버의 id가져와서 설정  
			HttpSession session = multipartRequest.getSession();
			MemberVO memberVO = (MemberVO) session.getAttribute("member");
			
			//새글 추가시 이것은 답글이 아니므로 부모번호로 0을 설정 후 글목록(articleMap)에 id 및 이미지 파일명 저장
			String id = memberVO.getId();
			articleMap.put("parentNO", 0);
			articleMap.put("id", id);
			articleMap.put("imageFileName", imageFileName);
			
			String message;
			ResponseEntity resEnt=null;
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/html; charset=utf-8");
			
			try {
				
				int articleNO = boardService.addNewArticle(articleMap);
				logger.info("새글 추가 후 글번호" + articleNO);
				if(imageFileName!=null && imageFileName.length()!=0) {
					File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
					FileUtils.moveFileToDirectory(srcFile, destDir,true);
				}
				
				
				message = "<script>";
				message += " alert('새글을 추가했습니다.');";
				message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
				message +=" </script>";
			    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
				
				
				
			}catch(Exception e) {
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				srcFile.delete();
				
				message = " <script>";
				message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
				message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
				message +=" </script>";
				resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
				e.printStackTrace();
			}	
			
		
			
			
		
		return resEnt;
	}
	
	
	// 답글 쓰기 추가
	
	@Override
	@RequestMapping(value="/board/addReply.do" ,method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addReplyArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			logger.info("답글 추가시 파라미터 이름 : " + name + "파라미터 값:" +  value);
			articleMap.put(name,value);
		}
			
			String imageFileName= upload(multipartRequest);
			logger.info("답글 추가시 이미지 파일명 : " + imageFileName );
			
			
			//해당 요청에 대한 세션에서 회원(member)가져오고 해당 멤버의 id가져와서 설정  
			HttpSession session = multipartRequest.getSession();
			
			MemberVO memberVO = (MemberVO) session.getAttribute("member");
			logger.info("답글 사는 사람의 id" + memberVO.getId());
			
			
			
			int parentNO = (Integer) session.getAttribute("parentNO");
			
			
			//답글 추가시 이것은 답글므로 부모번호를 가져와서  설정 후 글목록(articleMap)에  부모 글 번호(30) 및 id 및 이미지 파일명 저장
			

			
			logger.info("답글의 부모글 번호 : " +parentNO);
			String id = memberVO.getId();
			articleMap.put("parentNO",parentNO);
			articleMap.put("id", memberVO.getId());
			articleMap.put("imageFileName", imageFileName);
			
			String message;
			ResponseEntity resEnt=null;
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/html; charset=utf-8");
			
			try {
				
				int articleNO = boardService.addNewArticle(articleMap);
				logger.info("새글 추가 후 글번호" + articleNO);
				if(imageFileName!=null && imageFileName.length()!=0) {
					File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
					FileUtils.moveFileToDirectory(srcFile, destDir,true);
				}
				
				
				message = "<script>";
				message += " alert('답글을 추가했습니다.');";
				message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
				message +=" </script>";
			    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
				
				
				
			}catch(Exception e) {
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				srcFile.delete();
				
				message = " <script>";
				message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
				message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
				message +=" </script>";
				resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
				e.printStackTrace();
			}	
			
		
			
			
		
		return resEnt;
	}
	
	
	
	//한개 이미지 업로드하기
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
			logger.info("여기는 upload 메서드 영역");
			String imageFileName= null;
			Iterator<String> fileNames = multipartRequest.getFileNames();
			logger.info( "멀티파트리퀘스트가 가져온  파일 전체 객체"+fileNames);
			
			while(fileNames.hasNext()){
				String fileName = fileNames.next();
				logger.info( "멀티파트리퀘스트가 가져온  파일 객체"+fileName);
				
				/* MultipartFile
				 * 멀티파트 요청에서 수신된 업로드된 파일의 표현입니다.
				 * 
				 * 파일 내용은 메모리에 저장되거나 디스크에 임시로 저장됩니다. 두 경우 모두 사용자는 원하는 경우 파일 내용을 세션 수준 또는 영구 저장소에
				 * 복사해야 합니다. 임시 저장소는 요청 처리가 끝나면 지워집니다.
				 */
				MultipartFile multipartFile = multipartRequest.getFile(fileName);
				imageFileName=multipartFile.getOriginalFilename();
				logger.info( "멀티파트리퀘스트가 가져온  이미지 파일 명"+imageFileName);
				File file = new File(ARTICLE_IMAGE_REPO +"\\"+ fileName);
				logger.info("파일 객체:" + file);
				
				logger.info("멀피파트파일용량:" + multipartFile.getSize());
				
				if(multipartFile.getSize()!=0){ //File Null Check
					logger.info("파일 존재 여부 " + file.exists());  // 이 시점의 파일 경로에는 파일이 없음
					if(! file.exists()){ //경로상에 파일이 존재하지 않을 경우
						logger.info("디렉토리 만들었는지 여부 " + file.getParentFile().mkdirs()); //경로에 해당하는 디렉토리들을 생성
						
						if(file.getParentFile().mkdirs()){ //경로에 해당하는 디렉토리들을 생성
								file.createNewFile(); //이후 파일 생성
						}
					}
					multipartFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+imageFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송
				}
			}
			return imageFileName;
		}
		
		
		
		
	
	
	@Override
	@RequestMapping(value="/board/modArticle.do" ,method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String,Object> articleMap = new HashMap<String, Object>();
		
		Enumeration enu=multipartRequest.getParameterNames();
	
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			articleMap.put(name,value);
		}
		
		String imageFileName= upload(multipartRequest);
		articleMap.put("imageFileName", imageFileName);
		
		String articleNO=(String)articleMap.get("articleNO");
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			boardService.modArticle(articleMap);
			
			if(imageFileName!=null && imageFileName.length()!=0) {
		         File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
		         File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
		         FileUtils.moveFileToDirectory(srcFile, destDir, true);
		         
		         String originalFileName = (String)articleMap.get("originalFileName");
		         File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
		         oldFile.delete();
		       }	
			
			   message = "<script>";
			   message += " alert('글을 수정했습니다.');";
			   message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
			   message +=" </script>";
		       resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);			
			
		}catch(Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
		      srcFile.delete();
		      message = "<script>";
			  message += " alert('오류가 발생했습니다.다시 수정해주세요');";
			  message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
			  message +=" </script>";
		      resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}
		
		return resEnt;
	}



	
	
	
	
	
	
	


	
	
	
	


	
//	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//		response.setContentType("text/html; charset=utf-8");
//		String action = request.getPathInfo();
//		System.out.println("action:" + action);
//		
//		String nextPage = "";
//		
//		if(action == null || (action.equals("/"))  || action.equals("/listArticles.do")) {
//			
//			String _section=request.getParameter("section");
//			String _pageNum=request.getParameter("pageNum");
//			
//			int section = Integer.parseInt(((_section==null)? "1":_section) );
//			
//			int pageNum = Integer.parseInt(((_pageNum==null)? "1":_pageNum));
//			
//			System.out.println("섹션번호  : " + section + "," + "페이지번호" + pageNum);
//			
//			
//			Map pagingMap = new HashMap();
//			
//			pagingMap.put("section", section);
//			pagingMap.put("pageNum", pageNum);
//			
//			Map articlesMap=boardService.listArticles(pagingMap);
//			
//			articlesMap.put("section", section);
//			articlesMap.put("pageNum", pageNum);
//			request.setAttribute("articlesMap", articlesMap);
//			
//			
////			List<ArticleVO>  articlesList=new ArrayList<ArticleVO>();;
////			articlesList=boardService.listArticles();
////			request.setAttribute("articlesList", articlesList);
//			nextPage = "/board01/listArticles.jsp";
//			
//			
//			
//			
//		}else if(action.equals("/articleForm.do")) {
//			nextPage = "/board01/articleForm.jsp";
//		}else if(action.equals("/addArticle.do")) {
//			
//			
//			Map<String, String> articleMap=upload(request,response );
//			String title = articleMap.get("title");
//			String content = articleMap.get("content");
//			String imageFileName = articleMap.get("imageFileName");
//			
//			articleVO.setParentNO(0);
//			articleVO.setId("hong");
//			articleVO.setTitle(title);
//			articleVO.setContent(content);
//			articleVO.setImageFileName(imageFileName);
//			
//			int articleNO=boardService.addArticle(articleVO);
//			
//			//이미지 파일 유효성 체크
//			if(imageFileName!=null && imageFileName.length()!=0) {
//				File srcfile=new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
//				File desDir=new File(ARTICLE_IMAGE_REPO+"\\" + articleNO);
//				desDir.mkdirs();   //File.mkdirs() 메서드는 전체 디렉토리 경로를 생성하면서 디렉토리를 만드는 반면, File.mkdir() 메서드는 단일 디렉토리를 생성합니다.
//				System.out.println(desDir.mkdirs());  // 필요한 부모 디렉토리는 이미 있으니 false, 필요한 모든 상위 디렉토리와 함께 디렉토리가 생성된 경우에만 true이고, 그렇지 않으면 false입니다.
//				FileUtils.moveFileToDirectory(srcfile, desDir, true);  //true로 설정하면 디렉토리가 없을 경우 자동 생성, false로  설정할 경우, 대상 디렉토리가 없을 경우 예외 발생
//			}
//			
//			PrintWriter pw=response.getWriter();
//			pw.print("<script>"
//					+ "alert('새글 추가함');"
//					+ " location.href='"+request.getContextPath()+ "/board/listArticles.do';"
//					+ "</script>");
//			
//			
//			
//			return;
//		
//		}else if(action.equals("/viewArticle.do")) {
//			
//			//글번호 가져와서 그 글번호로 객체를 조회(sql)해서 다시 글 정보를 articleVO에 저장
//			String articleNO=request.getParameter("articleNO");
//			System.out.println("글번호 :" + articleNO);
//			
//			ArticleVO article = boardService.viewArticle(Integer.parseInt(articleNO));
//			
//			request.setAttribute("article", article);
//			nextPage = "/board01/viewArticle.jsp";
//			
//		}else if(action.equals("/modArticle.do")){
//			Map<String, String> articleMap = upload(request, response);
//			
//			int articleNO = Integer.parseInt(articleMap.get("articleNO"));
//			
//			articleVO.setArticleNO(articleNO);
//			String title = articleMap.get("title");
//			String content = articleMap.get("content");
//			String imageFileName = articleMap.get("imageFileName");
//			
//			articleVO.setParentNO(0);
//			articleVO.setId("hong");
//			articleVO.setTitle(title);
//			articleVO.setContent(content);
//			articleVO.setImageFileName(imageFileName);
//			
//			boardService.modArticle(articleVO);
//			
//			
//			
//			if (imageFileName != null && imageFileName.length() != 0) {
//				
//				String originalFileName = articleMap.get("originalFileName");
//				
//				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
//				
//				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
//				destDir.mkdirs();
//				FileUtils.moveFileToDirectory(srcFile, destDir, true);
//				
//				File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
//				oldFile.delete();
//			}
//			
//			
//			
//			PrintWriter pw = response.getWriter();
//			pw.print("<script>" + "  alert('글을 수정했습니다.');" + " location.href='" + request.getContextPath()
//					+ "/board/viewArticle.do?articleNO=" + articleNO + "';" + "</script>");
//			return;
//			
//			
//			
//		}else if(action.equals("/removeArticle.do")) {
//			int articleNO = Integer.parseInt(request.getParameter("articleNO"));
//			List<Integer> articleNOList = boardService.removeArticle(articleNO);
//			
//			for (int _articleNO : articleNOList) {
//				File imgDir = new File(ARTICLE_IMAGE_REPO + "\\" + _articleNO);
//				if (imgDir.exists()) {
//					FileUtils.deleteDirectory(imgDir);
//				}
//			}
//			
//			
//			 PrintWriter pw = response.getWriter(); pw.print("<script>" + "  alert('글을 삭제했습니다.');" +
//			 " location.href='" + request.getContextPath() + "/board/listArticles.do';" +
//			 "</script>"); 
//			 return;
//			
//			
//		}else if(action.equals("/replyForm.do")){
//			
//			try {
//				int parentNO = Integer.parseInt(request.getParameter("parentNO"));
//				System.out.println("답글쓰기에서 넘어온 부모 글번호 : " + parentNO);
//				//로그인한 사람이 계속 그 정보를 가지고 다녀야하므로 세션에 저장
//				session=request.getSession();
//				
//				session.setAttribute("parentNO", parentNO);
//			}catch(Exception e) {
//				
//			}
//			
//			
//			
//		
//			
//			nextPage = "/board01/replyForm.jsp";
//			
//		}else if(action.equals("/addReply.do")){
//			session = request.getSession();
//			int parentNO = (Integer) session.getAttribute("parentNO");
//			System.out.println();
//			//이미 부모글 가져왔으므로
//			//세션에는 남기지 않는것이 좋다. 추후에 세션셋팅시 키값이 같을 경우 데이터가 잘 못 들어갈 수 있기 때문에 , 가급적 가져온 후에는 삭제하는 버릇을 들이시기 바랍니다.
//			
////			session.removeAttribute("parentNO");
//			
//			//각각에 대한 데이터 타입이 다를 경우 해쉬맵으로 저장
//			
//			Map<String, String> articleMap = upload(request, response);
//			
//			String title = articleMap.get("title");
//			String content = articleMap.get("content");
//			String imageFileName = articleMap.get("imageFileName");
//			
//			articleVO.setParentNO(parentNO);
//			articleVO.setId("lee");
//			articleVO.setTitle(title);
//			articleVO.setContent(content);
//			articleVO.setImageFileName(imageFileName);
//			
//			int articleNO=boardService.addReply(articleVO);
//			
//			if (imageFileName != null && imageFileName.length() != 0) {
//				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
//				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
//				destDir.mkdirs();
//				FileUtils.moveFileToDirectory(srcFile, destDir, true);
//			}
//			
//			
//			PrintWriter pw = response.getWriter();
//			pw.print("<script>" + "  alert('답글을 추가했습니다.');" + " location.href='" + request.getContextPath()
//					+ "/board/viewArticle.do?articleNO="+articleNO+"';" + "</script>");
//			return;
//			
//			
//		}else {
//			nextPage = "/board01/listArticles.jsp";
//		}
//		
//		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
//		dispatch.forward(request, response);
//	}
//	

//	public Map<String, String> upload(HttpServletRequest request, HttpServletResponse response){
//		
//		String encoding = "utf-8";
//		
//		
//		// 새로운 글 쓰기에 대한 데이터를 여러 행태를 저장하지 좋은 키, 밸류의 맵 형태로 저장
//		
//		Map<String, String> articleMap = 	new HashMap<String, String>();
//		
//		// 파일 객체
//		
//		File currentDirPath =new File(ARTICLE_IMAGE_REPO);
//		
//		
//		//파일 업로드 시 사용한 라이브러리(fileupload, common-io)
//				// 업로드될 경로 
//				
//				
//				// 파일을 올리는 공간에다가 파일을 올리고자하는 용량과 경로 설정
//						// 파일 공간에 대한 설정을 위한 클래스 DiskFileItemFactory
//
//
//		DiskFileItemFactory factory = new DiskFileItemFactory(); // 파일 설정과 관련된 여러가지 일을 하는 클래스, 파일과 관련된 내용을 항목(item) 형태로
//		// 저장함
//		// 파일 아이템이란 예를 들어, 파일이름, 파일 크기
//factory.setRepository(currentDirPath);
//
//// DiskFileItemFactory 클래스의 setSizeThreshold() 메서드는 메모리에 보관할 임시 파일의 크기 임계값을 설정하는
//// 데 사용됩니다. 이 메서드에서 사용되는 단위는 바이트(Byte)입니다.
//
//// setSizeThreshold() 메서드에 전달하는 매개변수는 바이트 단위로 지정되며,
//// 임시 파일의 크기가 이 임계값보다 크면 디스크에 파일이 저장됩니다. 임시 파일의 크기가 임계값보다 작으면 메모리에 보관됩니다.
//// 이렇게 함으로써 작은 파일은 메모리에 보관되어 더 빠른 처리가 가능하고, 큰 파일은 디스크에 저장되어 메모리 사용량을 줄일 수 있습니다.
//factory.setSizeThreshold(1024 * 1024); // 1024 byte * 1024 = 1KB
//
//// 파일을 올리는 행위를 하는 클래스
//
//ServletFileUpload upload = new ServletFileUpload(factory);
//System.out.println("파일 업로드 객체 : " + upload);
//
//// FileItem 은 인코딩 타입이 multipart/form-data 일 때 , POST로 요청시 받을 수 있는 항목 클래스
//
//				
//				try {
//					
//					List<FileItem> items = upload.parseRequest(request);
//					
//					for (int i = 0; i < items.size(); i++) {
//						
//						
//						System.out.println("---------------------");
//						FileItem fileItem = (FileItem) items.get(i);
//						System.out.println(fileItem);
//						System.out.println(fileItem.isFormField());
//						// 폼필드 내용만 가져옴
//						if(fileItem.isFormField()) {
//							System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
//							
//							articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
//							
//							
//						}else {
//							System.out.println("매개변수 이름 :" + fileItem.getFieldName());
//							System.out.println("파일이름 :" + fileItem.getName());
//							System.out.println("파일 크기" + fileItem.getSize() + "bytes");
//
//							
//							
//							if (fileItem.getSize() > 0) {
//								System.out.println(fileItem.getName());
//								// 이전 버전의 익스플로러에서는 전체 경로를 가져오는 경우에 대비...중간에 혹시 폴더명이 끼여있는지 확인하는 코드
//								int idx = fileItem.getName().lastIndexOf("\\");
//								
//								System.out.println("인덱스" + idx);
//								
//								if (idx == -1) {
//									idx = fileItem.getName().lastIndexOf("/");     //익스플로러의 경우 대비
//									System.out.println(idx);
//								}
//								
//								String fileName = fileItem.getName().substring(idx + 1);
//								
//								//중간에 파일명에 //나 \가 포함될 경우 예외가 발생함, 예외가 발생하지 않도록 하는 코드 추가함
////								String fileName=fileItem.getName() + "\\hi" + "/nice";
//								System.out.println("현재 경로" + currentDirPath);
//								System.out.println("파일명" + fileName);
//								
//								articleMap.put(fileItem.getFieldName(), fileName);
//								
//								File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
//								
//								fileItem.write(uploadFile);
//							}
//							
//							
//							
//						}
//						
//						
//					}
//					
//					
//				}catch(Exception e) {
//					System.out.println("파일 업로드시 에러");
////					e.printStackTrace();
//				}
//				
//				
//				
//				
//				return articleMap;
//				
//	}
//	
//	
//	
//	

}
