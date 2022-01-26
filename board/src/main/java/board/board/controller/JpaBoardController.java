package board.board.controller;

import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import board.board.dto.BoardUpdateRequestDto;
import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import board.board.service.JpaBoardService;


@Controller
public class JpaBoardController {

	@Autowired
	private JpaBoardService jpaBoardService;
	
	// RESTful 서비스에서는 주소와 요청방법, 이 두가지 속성은 꼭 지정해야한다.
	// 먼저 value 속성으로 주소를 지정하고, method 속성으로 요청방식을 정의함.
	// GetMapping, PostMapping, PutMapping, DeleteMapping 어노테이션 활용 가능 이러면
	// 주소만 정의해서 사용하면 됨
	@RequestMapping(value="/jpa/board", method=RequestMethod.GET)
	public ModelAndView openBoardList() throws Exception{
		ModelAndView mv = new ModelAndView("/board/jpaBoardList");
		
		List<BoardEntity> list = jpaBoardService.selectBoardList();
		mv.addObject("list", list);
		
		return mv;
	}
	
	@PutMapping("/board/update")
	public String boardUpdate(@RequestBody BoardUpdateRequestDto dto) {
		jpaBoardService.updateBoard(dto);
		return "redirect:/board/openBoardList.do";
	}
	
	@RequestMapping(value="/jpa/board/write", method=RequestMethod.GET)
	public String openBoardWrite() throws Exception{
		return "/board/jpaBoardWrite";
	}
	
	@RequestMapping(value="/jpa/board/write", method=RequestMethod.POST)
	public String insertBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		jpaBoardService.saveBoard(board, multipartHttpServletRequest);
		return "redirect:/jpa/board";
	}
	
	// {boardIdx} 가 @PathVariable 어노테이션에의해 boardIdx에 매핑됨
	// 메서드의 파라미터가 URI의 변수로 사용되는 것을 의미
	@RequestMapping(value="/jpa/board/{boardIdx}", method=RequestMethod.GET)
	public ModelAndView openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception{
		ModelAndView mv = new ModelAndView("/board/jpaBoardDetail");
		
		BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);
		mv.addObject("board", board);
		
		return mv;
	}
	
	@RequestMapping(value="/jpa/board/{boardIdx}", method=RequestMethod.PUT)
	public String updateBoard(BoardEntity board, @PathVariable("boardIdx") int boardIdx) throws Exception{
		jpaBoardService.saveBoard(board, null);
		return "redirect:/jpa/board";
	}
	
	@RequestMapping(value="/jpa/board/{boardIdx}", method=RequestMethod.DELETE)
	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
		jpaBoardService.deleteBoard(boardIdx);
		return "redirect:/jpa/board";
	}
	
	@RequestMapping(value="/jpa/board/file", method=RequestMethod.GET)
	public void downladBoardFile(@RequestParam int idx, @RequestParam int boardIdx,
			HttpServletResponse response) throws Exception{
		BoardFileEntity boardFile = jpaBoardService.selectBoardFileInformation(idx, boardIdx);
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
