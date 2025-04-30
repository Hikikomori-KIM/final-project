package com.hiki.academyfinal.restcontroller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hiki.academyfinal.dao.AttachmentDao;
import com.hiki.academyfinal.dto.AttachmentDto;
import com.hiki.academyfinal.configuration.FileuploadProperties;

@CrossOrigin
@RestController
@RequestMapping("/api/attachment")
public class AttachmentRestController {

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    private FileuploadProperties fileuploadProperties;

    @GetMapping("/{attachmentNo}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable int attachmentNo) throws IOException {
        // 1. 첨부파일 정보 조회
        AttachmentDto attachmentDto = attachmentDao.selectOne(attachmentNo);
        if (attachmentDto == null) {
            return ResponseEntity.notFound().build();
        }

        // 2. 파일 실제 경로 설정
        File target = new File(fileuploadProperties.getRootDir(), String.valueOf(attachmentNo));
        if (!target.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 3. 파일 읽기
        byte[] data = Files.readAllBytes(target.toPath());
        ByteArrayResource resource = new ByteArrayResource(data);

        // 4. 파일 반환
        return ResponseEntity.ok()
                .contentLength(target.length())
                .header(HttpHeaders.CONTENT_TYPE, attachmentDto.getAttachmentType())
                .body(resource);
    }
}
