package com.hiki.academyfinal.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.configuration.FileuploadProperties;

@Service
public class AttachmentService {

    @Autowired
    private FileuploadProperties fileuploadProperties;

    // 🔥 파일 저장만 담당 (DB insert는 ProductService에서 실행)
    public void save(MultipartFile attach, int attachmentNo) throws IOException {
        if (attach.isEmpty()) return;

        File dir = fileuploadProperties.getRootDir();
        dir.mkdirs();

        File target = new File(dir, String.valueOf(attachmentNo));
        attach.transferTo(target);
    }
}
