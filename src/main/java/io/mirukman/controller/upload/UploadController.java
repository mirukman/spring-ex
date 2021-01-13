package io.mirukman.controller.upload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import io.mirukman.domain.upload.AttachFileDto;
import io.mirukman.service.upload.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/upload")
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final UploadService service;

    @PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<AttachFileDto>> uploadAjaxPost(MultipartFile[] uploadFile) {
        return new ResponseEntity<>(service.save(uploadFile), HttpStatus.OK);
    }

    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> displayThumbnail(String fileName) {

        File file = new File(service.getUploadRootPath() + "/" + fileName);   

        try {
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            return new ResponseEntity<>(service.getThumbnail(file), header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {
        log.info("fileName: " + fileName);
        Resource resource = new FileSystemResource(service.getUploadRootPath() + "/" + fileName);
        log.info("resource: " + resource);

        String resourceName = resource.getFilename();
        String resourceNameUuidRemoved = resourceName.substring(resourceName.indexOf("_") + 1);

        HttpHeaders header = new HttpHeaders();

        try {
            String downloadName = null;
            if(userAgent.contains("Trident") || userAgent.contains("Edge")) {
                downloadName = URLEncoder.encode(resourceNameUuidRemoved, "UTF-8");
            } else {
                downloadName = new String(resourceNameUuidRemoved.getBytes("UTF-8"), "ISO-8859-1");
            }

            header.add("Content-Disposition", "attachment; filename=" + downloadName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }

    @PostMapping("/deleteFile")
    @ResponseBody
    public ResponseEntity<String> deleteFile(String fileName, String type) {

        log.info("fileName: " + fileName);
        log.info("type: " + type);

        File file;
        try {
            
            file = new File(service.getUploadRootPath() + "/" + URLDecoder.decode(fileName, "UTF-8"));
            file.delete();
            if (type.equals("image")) {
                //image파일의 경우 view에서 전달받은것은 thumbnail path
                String originalFilePath = file.getAbsolutePath().replaceFirst("s_", "");
                log.info("thumbnail file path: " + originalFilePath);
                file = new File(originalFilePath);
                file.delete();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }
}
