package board.board.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;

@Mapper
public interface BoardMapper {
	List<BoardDto> selectBoardList() throws Exception;
	void insertBoard(BoardDto board) throws Exception;
	void updateHitCount(int boardIdx) throws Exception;
	BoardDto selectBoardDetail(int boardIdx) throws Exception;
	void updateBoard(BoardDto board) throws Exception;
	void deleteBoard(int boardIdx) throws Exception;
	void insertBoardFileList(List<BoardFileDto> list) throws Exception;
	List <BoardFileDto> selectBoardFileList(int boardIdx) throws Exception;
	// @Param 어노테이션을 이용하면 해당 파라미터들이 Map에 저장되어 DTO 를 만들지 않더라도 
	// 여러개의 파마리터를 전달할 수 있다.
	// @Param 을 이용해서 파라미터를 지정하고 키를 저장
	BoardFileDto selectBoardFileInformation(@Param("idx") int idx, 
											@Param("boardIdx") int boardIdx);
}
