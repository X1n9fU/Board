package com.example.board.repository;

import com.example.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
                                                    //이 부분만 잘 설정해주면 다 상속받아 사용 가능
    // update board_table set board_hits=board_bits+1 where id = ?
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    //Entity 기준의 Query문 - b(약어) = BoardEntity , 마지막 id가 밑 param(id)와 일치해야함
    void updateHits(@Param("id") Long id);
}

