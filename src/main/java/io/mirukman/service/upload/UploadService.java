package io.mirukman.service.upload;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.mirukman.domain.upload.AttachFileDto;

public interface UploadService {
    
    public List<AttachFileDto> save(MultipartFile[] files);

    public byte[] getThumbnail(File file) throws IOException;

    public String getUploadRootPath();
}
