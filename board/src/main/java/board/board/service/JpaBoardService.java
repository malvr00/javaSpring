package board.board.service;

import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.dto.BoardUpdateRequestDto;
import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;

public interface JpaBoardService {
	
	List<BoardEntity> selectBoardList() throws Exception;
	void saveBoard(BoardEntity board, MultipartHttpServletRequest mltipartHttpServletRequest) throws Exception;
	BoardEntity selectBoardDetail(int boardIdx) throws Exception;
	void deleteBoard(int boardIdx);
	BoardFileEntity selectBoardFileInformation(int boardIdx, int idx) throws Exception;
	BoardEntity updateBoard(BoardUpdateRequestDto dto);
}
