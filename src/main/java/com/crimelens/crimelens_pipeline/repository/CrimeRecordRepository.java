package com.crimelens.crimelens_pipeline.repository;

import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrimeRecordRepository extends JpaRepository<CrimeRecord, Long> {}
