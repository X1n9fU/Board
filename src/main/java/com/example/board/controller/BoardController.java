package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.CommentDTO;
import com.example.board.repository.CommentRepository;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board") //묶어서 처리
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    @GetMapping("/save") //원래는 /board/save
    public String saveForm(){
        return "save";
    }

    @PostMapping("/save") //post로 변수를 전달받을때
    //GetMapping을 사용하게 되면 오류 발생, 같은 경로를 사용
    public String save(@ModelAttribute BoardDTO boardDTO){
                        //save.html에서의 input name과 BoardDTO의 변수 이름이 같은 것을 모두 가져와준다.
        System.out.println("boardDTO" + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model){
        //DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList" , boardDTOList);
        return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
//                            경로상에 있는 데이터 가져올 때는 PathVariale
                        @PageableDefault(page =1) Pageable pageable){
//        해당 게시글의 조회수를 하나 올리고
//        게시글 데이터를 가져와서 detail.html에 출력
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        //댓글 목록 가져오기
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);

        model.addAttribute("board" , boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "detail";
    }


    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate",boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model){
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "detail";
//        return "redirect:/board/" + boardDTO.getId();
        //목록으로 돌아가는 것 근데 수정했을때 조회수의 영향을 받는다.
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/";
    }

    // /board/paging?page=1
    @GetMapping("/paging")                              //pageable은 page=1의 정보를 가져와준다.
    public String paging(@PageableDefault(page=1) Pageable pageable, Model model){
    //    pageable.getPageNumber();     //page=1이라는 값이 없으면 default로 page=1로 생각하겠다.
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3; //페이지 번호 갯수
        // page 갯수 20개
        // 현재 사용자가 3페이지 -> 1 2 3
        // 현재 사용자가 7페이지 -> 7 8 9
        // 보여지는 페이지 갯수 3개
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();
        // 총 페이지 갯수 8개 -> 7 8

        model.addAttribute("boardList",boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage",endPage);
        return "paging";

    }

}
