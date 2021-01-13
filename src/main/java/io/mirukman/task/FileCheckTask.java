package io.mirukman.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.mirukman.domain.attach.BoardAttachVo;
import io.mirukman.mapper.AttachMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileCheckTask {
    
    private final AttachMapper attachMapper;

    private final static String ROOT_DIR = "/mnt/c/Users/jchg9/workspace/upload";

    @Scheduled(cron = "0 0 2 * * *")
    public void checkFiles() {
        log.warn("File Check Task run............................................");
        log.warn("now: " + LocalDateTime.now());

        List<BoardAttachVo> fileList = attachMapper.getOldFiles();

        List<Path> fileListPaths = fileList.stream()
                .map(attach -> Paths.get(ROOT_DIR, attach.getUploadPath(), attach.getUuid() + "_" + attach.getFileName()))
                .collect(Collectors.toList());
            
        fileList.stream()
                .filter(attach -> attach.isFileType())
                .map(attach -> Paths.get(ROOT_DIR, attach.getUploadPath(), "s_" + attach.getUuid() + "_" + attach.getFileName()))
                .forEach(path -> {
                    fileListPaths.add(path);
                });

        File targetDir = Paths.get(ROOT_DIR, getFolderYesterDay()).toFile();

        File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);

        for (File file : removeFiles) {
            log.warn("remove file: " + file.getAbsolutePath());
            file.delete(); 
        }
    }

    private String getFolderYesterDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate yesterday = LocalDate.now().minusDays(1);

        String yesterDayFolder = formatter.format(yesterday);
        log.warn("yesterday folder: " + yesterDayFolder);

        return yesterDayFolder;
    }
}
