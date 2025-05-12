package com.hiki.academyfinal.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.configuration.FileuploadProperties;
import com.hiki.academyfinal.dao.AttachmentDao;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private FileuploadProperties fileuploadProperties;

    // 🔥 파일 저장만 담당 (DB insert는 ProductService에서 실행)
    public void save(MultipartFile attach, long attachmentNo) throws IOException {
        if (attach.isEmpty()) return;

        File dir = fileuploadProperties.getRootDir();
        dir.mkdirs();

        File target = new File(dir, String.valueOf(attachmentNo));
        attach.transferTo(target);
    }

    public void remove(long attachmentNo) {
        // 1. 파일 삭제
        File dir = fileuploadProperties.getRootDir();
        File target = new File(dir, String.valueOf(attachmentNo));
        if (target.exists()) target.delete();

        // 2. DB에서 삭제
        attachmentDao.delete(attachmentNo);
    }

}
