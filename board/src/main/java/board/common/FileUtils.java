package board.common;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.dto.BoardFileDto;

@Component	// FileUtils 클래스를 스프링의 빈으로 등록합니다.
public class FileUtils {
	public List<BoardFileDto> parseFileInfo(int boardIdx, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		if(ObjectUtils.isEmpty(multipartHttpServletRequest)) {
			return null;
		}
		List<BoardFileDto> fileList = new ArrayList<>();
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		ZonedDateTime current = ZonedDateTime.now();	// ZonedDateTime = 오늘날짜를 확인하기 위해 사용
		String path = "images/" + current.format(format);
		File file = new File(path);
		if(file.exists() == false) {
			file.mkdirs();
		}
		
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames(); // 파일 내용을 Iterator 형식으로 가져옴
		String newFileName, originalFileExtension, contentType;
		
		while(iterator.hasNext()) {
			List<MultipartFile> list = multipartHttpServletRequest.getFiles(iterator.next());
			for(MultipartFile multipartFile : list) {
				if(multipartFile.isEmpty() == false) {
					contentType = multipartFile.getContentType();
					if(ObjectUtils.isEmpty(contentType)) {
						break;
					}else {
						// 파일형식확인 (실제로 이렇게사용 하면 위변조 확인이 어렵기 때문에 매우 위험한 방법)
						if(contentType.contains("image/jpeg")) {
							originalFileExtension = ".jpg";
						}else if(contentType.contains("image/png")) {
							originalFileExtension = ".png";
						}else if(contentType.contains("image/gif")) {
							originalFileExtension = ".gif";
						}else {
							break;
						}
					}
				
				
				newFileName = Long.toString(System.nanoTime()) 
						+ originalFileExtension;	// 저장될 파일 이름 생성
				// 데이터베이스에 저장할 파일 정보를 앞에서 만든 BoardFileDto에 저장한다.
				BoardFileDto boardFile = new BoardFileDto();
				boardFile.setBoardIdx(boardIdx);
				boardFile.setFileSize(multipartFile.getSize());
				boardFile.setOriginalFileName(multipartFile.getOriginalFilename());
				boardFile.setStoredFiledPath(path + "/" + newFileName);
				fileList.add(boardFile);
				
				// 지정된 경로에 저장
				file = new File(path + "/" + newFileName);
				multipartFile.transferTo(file);
			}
		}
		
	}
		return fileList;
	}
}
