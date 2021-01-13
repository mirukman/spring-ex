package io.mirukman.controller.board;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.mirukman.domain.attach.BoardAttachVo;
import io.mirukman.domain.board.BoardVo;
import io.mirukman.domain.board.Criteria;
import io.mirukman.domain.board.PageDto;
import io.mirukman.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService service;

    @GetMapping("/list")
    public void list(Criteria criteria, Model model) {
        model.addAttribute("list", service.getList(criteria));
        model.addAttribute("pageDto", new PageDto(criteria, service.getTotalCount()));
    }

    @GetMapping("/get")
    public void get(@RequestParam long bno, @ModelAttribute Criteria criteria, Model model) {
        model.addAttribute("boardVo", service.get(bno));
        model.addAttribute("criteria", criteria);
    }

    @GetMapping("/modify")
    public void modify(@RequestParam long bno, @ModelAttribute Criteria criteria, Model model) {
        model.addAttribute("boardVo", service.get(bno));
        model.addAttribute("criteria", criteria);
    }
    
    @GetMapping("/register")
    public void register() {
        
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public String register(BoardVo boardVo, RedirectAttributes rttr) {

        log.info("register: " + boardVo.toString());

        if (boardVo.getAttachList() != null) {
            boardVo.getAttachList().forEach(attach -> {
                log.info(attach.toString());
            });
        }

        service.register(boardVo);
        rttr.addFlashAttribute("result", boardVo.getBno());

        return "redirect:/board/list";
    }

    @PreAuthorize("principal.username == #boardVo.writer")
    @PostMapping("/modify")
    public String modify(BoardVo boardVo, @ModelAttribute Criteria criteria, RedirectAttributes rttr) {

        if (service.modify(boardVo)) {
            rttr.addFlashAttribute("result", boardVo.getBno());
        }

        rttr.addFlashAttribute("criteria", criteria);

        return "redirect:/board/list";
    }
    
    @PreAuthorize("principal.username == #writer")
    @PostMapping("/remove")
    public String remove(@RequestParam long bno, @ModelAttribute Criteria criteria, RedirectAttributes rttr) {
        List<BoardAttachVo> attachList = service.getAttachList(bno);
        if (service.remove(bno)) {
            service.deleteFiles(attachList);
            rttr.addFlashAttribute("result", "success");
        } else {
            rttr.addFlashAttribute("result", "fail");
        }

        rttr.addFlashAttribute("criteria", criteria);

        return "redirect:/board/list";
    }

    @ResponseBody
    @GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoardAttachVo>> getAttachList(@RequestParam("bno") Long bno) {
        log.info("getAttachList(). request param bno = " + bno);
        return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
    }
}
