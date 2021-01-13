package io.mirukman.service.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import io.mirukman.domain.upload.AttachFileDto;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

	@Override
    public List<AttachFileDto> save(MultipartFile[] files) {
        String uploadPath = "/mnt/c/Users/jchg9/workspace/upload";

        String folderPath = getFolderPath();
        File uploadFolder = new File(uploadPath, folderPath);
        log.info("upload folder: " + uploadFolder);

        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        List<AttachFileDto> list = new ArrayList<>();
        for (MultipartFile file : files) {
            log.info("file name: " + file.getOriginalFilename());
            log.info("input tag name: " + file.getName());
            log.info("file size: " + file.getSize() / 1024 / 1024 + "MB");

            UUID uuid = UUID.randomUUID();
            String uploadName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(uploadFolder, uploadName);

            boolean isImage = isImageFile(saveFile);
            try {
                file.transferTo(saveFile);
                if (isImage) {
                    FileOutputStream thumbnail = new FileOutputStream(new File(uploadFolder, "s_" + uploadName));
                    Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 100, 100);
                    thumbnail.close();
                }

                list.add(new AttachFileDto(file.getOriginalFilename(), folderPath, uuid.toString(), isImage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }
    
    @Override
    public byte[] getThumbnail(File file) throws IOException {

        byte[] thumbnail = null;
        try {
            thumbnail = FileCopyUtils.copyToByteArray(file);
        } catch (IOException e) {
            log.error("thumbnail path: " + file.getAbsolutePath());
            throw new IOException("thumbnail load failure");
        }

        return thumbnail;
    }

    @Override
    public String getUploadRootPath() {
        return "/mnt/c/Users/jchg9/workspace/upload";
    }
    
    private String getFolderPath() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateString = format.format(date);

        return dateString.replace("-", File.separator);
    }
    
    public boolean isImageFile(File file) {
        try {
            String contentType = Files.probeContentType(file.toPath());
            return contentType.startsWith("image");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
