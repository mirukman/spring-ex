package io.mirukman.domain.upload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttachFileDto {
    
    private String fileName;
    private String uploadPath;
    private String uuid;
    private boolean image;
}
