package board.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.io.FileUtils;
// 구현체 사용않고 import해서 의존성만 사용. 장점은 다른 라이브러리로 쉽게 변경할 수 있음.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j		// 어노베이션을 사용하면 로거를 따로 생성할 필요가 없음.
@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	private Logger log = LoggerFactory.getLogger(this.getClass()); // Logger 클래스 객체생
			
	@RequestMapping("/board/openBoardList.do")
	public ModelAndView openBoardList() throws Exception{
		// /boadr/boardList는 template 더 아래에 있는 board/boardList.html 가르킴
		ModelAndView mv = new ModelAndView("/board/boardList");
		log.debug("openBoardList");	// 테스트용 - openBoardLis 문자열 출력
		
		List<BoardDto> list = boardService.selectBoardList();
		// 결과값 담긴 list를 list에 담아 html에 뿌려줌
		mv.addObject("list", list);
		
		return mv;
	}
	
	@RequestMapping("/board/openBoardWrite.do")
	public String openBoardWrite() throws Exception{
		return "/board/boardWrite";
	}
	
	@RequestMapping("/board/insertBoard.do")
	public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		// MultipartHttpServletRequest는 ServletRequest를 상속받아 구현된 인터페이스 ( 업로드된 파일을 처리하기 위한 여러가지 메소드 제공)
		boardService.insertBoard(board, multipartHttpServletRequest);
		return "redirect:/board/openBoardList.do";
	}
	
	@RequestMapping("/board/openBoardDetail.do")
	// @RequestParam는 Get Parameter 에 전송된 변수 값 가져옴
	public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception{
		ModelAndView mv = new ModelAndView("/board/boardDetail");
		
		BoardDto board = boardService.selectBoardDetail(boardIdx);
		mv.addObject("board", board);
		
		return mv;
	}
	
	@RequestMapping("/board/updateBoard.do")
	public String updateBoard(BoardDto board) throws Exception{
		boardService.updateBoard(board);
		return "redirect:/board/openBoardList.do";
	}
	
	@RequestMapping("/board/deleteBoard.do")
	public String deleteBoard(int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		return "redirect:/board/openBoardList.do";
	}
	
	@RequestMapping("/board/downloadBoardFile.do")
	public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, 
			HttpServletResponse response) throws Exception{
		// HttpServletRequest는 사용자로부터 들어오는 모든 요청을 담고있음.
		// 그 반대인 HttpServletResponse는 사용자에게 전달 할 데이터를 담고 있음.
		// HttpSErvletResponse 클래스에 적절히 설정을 해 주면 사용자에게 전달할 결과값을 원하는데로
		// 만들거나 변경 할 수 있음
		BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx); // 데이터베이스에서 선택도니 파일의 정보를 조회
		
		// Objectutils.isEmptu = 객체가 비어있거나 null 인지 확인 할 수 있음
		// null = true
		if(ObjectUtils.isEmpty(boardFile) == false) {
			String fileName = boardFile.getOriginalFileName();
			
			// 조회된 파일의 정보 중 저장위치, 즉 storedFilePath 값을 이용해서 실제로 저장되어있는
			// 파일을 읽어온 후 byte[] 형태로 변환.
			// 여기서 사용되는 FileUtils 클래스는 org.apache.commons.io 패키지의 FileUtils 클래스
			byte [] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));
			
			// response의 헤더에 콘텐츠 타입, 크기 및 형태등을 설정
			// 파일 이름은 반드시 UTF-8로 설정
			response.setContentType("application/octet-stream");
			response.setContentLength(files.length);
			response.setHeader("Content-Dispostion", "attachment; fileName=\"" + 
					 URLEncoder.encode(fileName, "UTF-8") + "\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			
			// 파일 정보의 바이트 배열 데이터를 response에 작성
			response.getOutputStream().write(files);
			// 버퍼 정리 후 닫기
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
}
