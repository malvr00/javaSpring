package board.board.controller;

import java.util.List;

import board.board.dto.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BoardController {
	
	@Autowired
	private BoardSerice boardService;
	
	@RequestMapping("/board/openBoardList.do")
	public ModelAndView openBoardList() throws Exception{
		// /boadr/boardList는 template 더 아래에 있는 board/boardList.html 가르킴
		ModelAndView mv = new ModelAndView("/board/boardList");
		
		List<BoardDto> list = boardService.selectBoardList();
		// 결과값 담긴 list를 list에 담아 html에 뿌려줌
		mv.addObject("list", list);
		
		return mv;
	}
}
