package board.board.dto;
import lombok.Data;

@Data
public class BoardFileDto {
	private int idx;
	private int boardIdx;
	private String originalFileName;
	private String storedFiledPath;
	private long fileSize;
}
