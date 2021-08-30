package com.data.data.repository;

import com.data.data.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileData, Long> {

    Optional<FileData> findById(String cvId);

    List<FileData> findAll();


}
