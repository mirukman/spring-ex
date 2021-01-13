package io.mirukman.service.board;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.mirukman.domain.attach.BoardAttachVo;
import io.mirukman.domain.board.BoardVo;
import io.mirukman.domain.board.Criteria;
import io.mirukman.mapper.AttachMapper;
import io.mirukman.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
 public class BoardServiceImpl implements BoardService {
    
	private final BoardMapper boardMapper;
	private final AttachMapper attachMapper;

	@Override
	@Transactional
	public void register(BoardVo boardVo) {
		log.info("register boardVo: " + boardVo);
		boardMapper.insertSelectKey(boardVo);
		if (boardVo.getAttachList() != null && boardVo.getAttachList().size() > 0) {
			boardVo.getAttachList().forEach(attach -> {
				attach.setBno(boardVo.getBno());
				attachMapper.insert(attach);
			});
		}
	}

    @Override
	public List<BoardVo> getList(Criteria criteria) {
		log.info("get board list(criteria: " + criteria + ")");
		List<BoardVo> list = boardMapper.getList(criteria);
		return list;
	}
	
	@Override
	public int getTotalCount() {
		log.info("getTotalCount()");
		return boardMapper.getTotalCount();
	}
    
	@Override
	public BoardVo get(long bno) {
		log.info("get boardVo(bno=" + bno + ")");
        return boardMapper.read(bno);
	}

	@Override
	public boolean modify(BoardVo boardVo) {
		log.info("modify boardVo: " + boardVo);

		boolean modifyResult = boardMapper.update(boardVo) == 1;
	
		if (modifyResult && boardVo.getAttachList() != null && boardVo.getAttachList().size() > 0) {
			attachMapper.deleteAll(boardVo.getBno());
			boardVo.getAttachList().forEach(attach -> {
				attach.setBno(boardVo.getBno());
				attachMapper.insert(attach);
			});
		}

		return modifyResult;
	}

	@Override
	public boolean remove(long bno) {
		log.info("remove boardVo(bno=" + bno + ")");
		attachMapper.deleteAll(bno);
		return boardMapper.delete(bno) == 1;
	}
	
	@Override
	public List<BoardAttachVo> getAttachList(Long bno) {
		log.info("get Attach list by bno" + bno);
		return attachMapper.findByBno(bno);
	}

	@Override
	public void deleteFiles(List<BoardAttachVo> attachList) {
		if (attachList == null || attachList.size() <= 0) {
			log.info("attach list is null");
			return;
		}

		log.info("delete attach files...");
		log.info(attachList.toString());

		attachList.forEach(attach -> {
			Path path = Paths.get("/mnt/c/Users/jchg9/workspace/upload/" + attach.getUploadPath() + "/"
					+ attach.getUuid() + "_" + attach.getFileName());

			try {
				Files.deleteIfExists(path);
				if (Files.probeContentType(path).startsWith("image")) {
					Path thumbnailPath = Paths
							.get(path.toString().replaceFirst(attach.getUuid(), "s_" + attach.getUuid()));
					Files.delete(thumbnailPath);
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		});
	}
    
}
