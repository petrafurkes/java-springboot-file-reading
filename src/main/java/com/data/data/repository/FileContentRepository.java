package com.data.data.repository;

import com.data.data.model.FileContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileContentRepository extends JpaRepository<FileContent, Long> {
}
