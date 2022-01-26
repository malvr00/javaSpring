package board.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;

// CrudRepository 리포지터리에서 사용할 도메인 클래스와 도메인의 id 타입을 파라미터로 받는다.
// JPA Entitu = domain
public interface JpaBoardRepository extends CrudRepository<BoardEntity, Integer>{
	List<BoardEntity> findAllByOrderByBoardIdxDesc();
	
	// FROM 절에 데이터베이스 이름이 아니라 Entity 이름을 사용함.
	// 일반 쿼리와 JPQL 차이점
	@Query("SELECT file FROM BoardFileEntity file WHERE board_idx = :boardIdx AND idx = :idx")
	BoardFileEntity findBoardFile(@Param("boardIdx") int boardIdx, @Param("idx") int idx);
	
	// 위와 아래 같음.
	
	//@Query("SELECT file FROM BoardFileEntity file WHERE board_idx = ?1 AND idx = ?2")
	// ?1, ?2에 각각 boardIdx, idx 할당함.
	//BoardFileEntity findBoardFile(int boardIdx, int idx);
}