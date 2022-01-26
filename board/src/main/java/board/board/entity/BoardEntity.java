package board.board.entity;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity	// 엔티티 클래스는 테이블과 매핑됨
@Table(name="t_jpa_board")	// t_jpa_board 매핑
@NoArgsConstructor	// 무분별한 객체 생성에 대해 한번 더 체크할 수 있음. ( 파라미터가 없는 생성자 생성 )
@Data
public class BoardEntity {
	@Id	// 기본키임을 알림
	@GeneratedValue(strategy=GenerationType.AUTO)	// 기본키 생성 전력, GenerationType = 데이터베이스에서 제공하는 기본키 생성 전략을 따름
	private int boardIdx;
	
	@Column(nullable=false)	// false = NOT NULL
	private String title;
	
	@Column(nullable=false)
	private String contents;
	
	@Column(nullable=false)
	private int hitCnt = 0;
	
	@Column(nullable=false)
	private String creatorId;
	
	@Column(nullable=false)
	private LocalDateTime createdDatetime = LocalDateTime.now();
	
	private String updaterId;
	
	private LocalDateTime updatedDatetime;
	
	// OneToMany는 1:N 관계를 표현하는 JPA 어노테이션.
	// 하나의 게시글은 기본적으로 첨부파일이 없거나 1개 이상의 첨부파일을 가질 수 있다.
	// 이 경우 게시글 하나에 여러개의 첨부파일을 가지는 1:N관계가 됨.
	// JoinColumn은 릴레이션 관계가 있는 테이블의 컬럼을 지정
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="board_idx")
	private Collection<BoardFileEntity> fileList;
}
