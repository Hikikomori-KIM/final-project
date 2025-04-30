package com.hiki.academyfinal.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.configuration.FileuploadProperties;
import com.hiki.academyfinal.dao.AttachmentDao;
import com.hiki.academyfinal.dto.AttachmentDto;

@Service
public class AttachmentService {

    @Autowired
    private FileuploadProperties fileuploadProperties;

    @Autowired
    private AttachmentDao attachmentDao;

    // 🔥 파일 저장 + DB 등록
    public AttachmentDto save(MultipartFile attach) throws IOException {
        if (attach.isEmpty()) return null;

        // 1. upload 디렉토리 생성 (upload 폴더만 생성, 개별 파일 mkdirs() 절대 안함)
        File dir = fileuploadProperties.getRootDir();
        dir.mkdirs(); // upload 폴더 없으면 생성

        // 2. DB에 attachment 정보 저장
        AttachmentDto dto = new AttachmentDto();
        dto.setAttachmentName(attach.getOriginalFilename());
        dto.setAttachmentType(attach.getContentType());
        dto.setAttachmentSize(attach.getSize());
        attachmentDao.insert(dto); // attachment_no 생성

        // 3. 파일 저장 (폴더 ❌ 파일 ✔️)
        File target = new File(dir, String.valueOf(dto.getAttachmentNo())); // upload/{attachmentNo}
        attach.transferTo(target); // 파일로 복사

        // 4. 저장된 정보 리턴
        return dto;
    }
 // 🔥 추가
    public AttachmentDto save(MultipartFile attach, int productsNo) throws IOException {
        return save(attach); // 그냥 1개짜리 메서드 재사용
    }
}
