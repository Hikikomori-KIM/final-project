package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.AttachmentDto;

@Repository
public class AttachmentDao {
    @Autowired
    private SqlSession sqlSession;
    
    public long sequence() {
        return sqlSession.selectOne("attachment.sequence");
    }
    
    public void insert(AttachmentDto attachmentDto) {
        sqlSession.insert("attachment.add", attachmentDto);
    }

    public AttachmentDto selectOne(int attachmentNo) {
        return sqlSession.selectOne("attachment.find", attachmentNo);
    }
    public void delete(long attachmentNo) {
        sqlSession.delete("attachment.delete", attachmentNo);
    }

}

