package com.blue.girl.server.dao;

import com.blue.girl.server.entity.FileRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRecordDao extends JpaRepository<FileRecordEntity,Integer> {
    FileRecordEntity findById(int id);
    FileRecordEntity findByFileName(String fileName);
}
