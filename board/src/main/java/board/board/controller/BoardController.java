package board.board.controller;

import java.util.List;


import board.board.dto.BoardDto;
import board.board.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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
	public String insertBoard(BoardDto board) throws Exception{
		boardService.insertBoard(board);
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
}
