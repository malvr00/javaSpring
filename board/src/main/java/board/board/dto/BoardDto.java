package board.board.dto;

import java.time.LocalDateTime;
import lombok.Data;
@Data		// getter와 setter toString, hashCode, equals 메서드도 생성
public class BoardDto {
	private int boardIdx;
	private String title;
	private String contents;
	private int hitCnt;
	private String creatorId;
	private LocalDateTime createdDatetime;
	private String updaterId;
	private LocalDateTime updatedDatetime;
}
