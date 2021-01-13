package io.mirukman.controller.reply;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mirukman.domain.board.Criteria;
import io.mirukman.domain.reply.ReplyPageDto;
import io.mirukman.domain.reply.ReplyVo;
import io.mirukman.service.reply.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
@Slf4j
public class ReplyController {
    
    private final ReplyService service;

    @GetMapping(value = "/pages/{bno}/{page}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ReplyPageDto> getList(@PathVariable("bno") Long bno, @PathVariable("page") int page) {

        if (page == -1) {
            int totalCount = service.getTotalCount(bno);
            page = (int) (totalCount / 10.0f); 
        }

        Criteria criteria = new Criteria(page);

        return new ResponseEntity<ReplyPageDto>(service.getList(bno, criteria), HttpStatus.OK);
    }
    
    @GetMapping(value = "/{rno}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ReplyVo> get(@PathVariable("rno") Long rno) {
        return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/new", consumes = "application/json", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> create(@RequestBody ReplyVo replyVo) {

        log.info("replyVo: " + replyVo);

        int count = service.register(replyVo);

        if (count == 1) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("principal.username == #replyVo.replyer")
    @PutMapping(value = "/{rno}", consumes = "application/json", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> modify(@RequestBody ReplyVo replyVo, @PathVariable("rno") Long rno) {
        
        log.info("replyvo: " + replyVo);

        replyVo.setRno(rno);

        int count = service.modify(replyVo);
    
        if (count == 1) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("principal.username == #replyVo.replyer")
    @DeleteMapping(value = "/{rno}")
    public ResponseEntity<String> remove(@RequestBody ReplyVo replyVo, @PathVariable("rno") Long rno) {

        int count = service.remove(rno);
    
        if (count == 1) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
