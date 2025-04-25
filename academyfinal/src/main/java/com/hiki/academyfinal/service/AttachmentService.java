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

    // 🔸 1. DB insert + 파일 저장 통합 메서드 (선택 사용)
    public AttachmentDto save(MultipartFile attach) throws IOException {
        if (attach.isEmpty()) return null;

        // 저장 위치 생성
        File dir = fileuploadProperties.getRootDir();
        dir.mkdirs();

        // 1. DB 저장 (attachment_no는 시퀀스나 insert 후 get 방식으로)
        AttachmentDto dto = new AttachmentDto();
        dto.setAttachmentName(attach.getOriginalFilename());
        dto.setAttachmentType(attach.getContentType());
        dto.setAttachmentSize(attach.getSize());
        AttachmentDto savedDto = attachmentDao.insert(dto);

        // 2. 물리 파일 저장
        File target = new File(dir, String.valueOf(savedDto.getAttachmentNo()));
        attach.transferTo(target);

        return savedDto;
    }

    // 🔸 2. DB insert는 따로 하고, 파일만 저장하는 메서드
    public void save(MultipartFile file, int attachmentNo) throws IOException {
        File dir = fileuploadProperties.getRootDir();
        dir.mkdirs();

        File target = new File(dir, String.valueOf(attachmentNo));
        file.transferTo(target);
    }
}