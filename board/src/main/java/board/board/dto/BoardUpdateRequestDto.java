package board.board.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardUpdateRequestDto {
	
	private String idx;
	
	private String contents;

}
