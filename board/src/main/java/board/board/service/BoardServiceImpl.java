package board.board.service;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.mapper.BoardMapper;
import board.common.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
//@Transactional // 인터페이스나 클래스, 메서드에 사용할 수 있다. 어노테이션이 적용된 대상은 설정된 트랜잭션이 처리됨
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;
	
//	@Autowired	//	무한 재귀호출 되는 경우가 있어서 @AutowiredArgsConstructor 어노테이션 활용해서 
//	private FileUtils fileUtils;
	
	private final FileUtils fileUtils;
	
	@Override
	public List<BoardDto> selectBoardList() throws Exception{
		return boardMapper.selectBoardList();
	}
	
	@Override
	public void insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		boardMapper.insertBoard(board);
		
		// 업로드된 파일을 서버에 저장하고 파일의 정보 가저옴
		List<BoardFileDto> list = fileUtils.parseFileInfo(board.getBoardIdx(), multipartHttpServletRequest);
		
		// 마이바티스의 foreach 기능을 사용하여 저장
		if(CollectionUtils.isEmpty(list) == false) {
			boardMapper.insertBoardFileList(list);
		}
		
		//		// 업로드 파일 정보 확인 ( 테스트 )
		//		if(ObjectUtils.isEmpty(multipartHttpServletRequest) == false) {
		//			Iterator<String> iterator = multipartHttpServletRequest.getFileNames();	// 파일 내용을 Iterator 형식으로 가져옴
		//			String name;
		//			while(iterator.hasNext()) {
		//				name = iterator.next();
		//				log.debug("file tag name : " + name);
		//				List<MultipartFile> list = multipartHttpServletRequest.getFiles(name);
		//				for(MultipartFile multipartFile : list) {
		//					// 파일 정보확인
		//					log.debug("start file information");
		//					log.debug("file name : " + multipartFile.getOriginalFilename());
		//					log.debug("file size : " + multipartFile.getSize());
		//					log.debug("file content type : " + multipartFile.getContentType());
		//					log.debug("end file information");
		//				}
		//			}
		//		}
	}
	
	@Override
	public BoardDto selectBoardDetail(int boardIdx) throws Exception{
		boardMapper.updateHitCount(boardIdx);
		BoardDto board = boardMapper.selectBoardDetail(boardIdx);
		
		List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
		board.setFileList(fileList);
		
		boardMapper.updateHitCount(boardIdx);
		return board;
	}
	
	@Override
	public void updateBoard(BoardDto board) throws Exception{
		boardMapper.updateBoard(board);
	}
	
	@Override
	public void deleteBoard(int boardIdx) throws Exception{
		boardMapper.deleteBoard(boardIdx);
	}
}
