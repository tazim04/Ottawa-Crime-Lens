package com.crimelens.crimelens_pipeline.repository;

import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CrimeRecordRepository extends JpaRepository<CrimeRecord, Long> {

  // get the date of the latest reported crime
  @Query("select max(c.reportedDate) from CrimeRecord c")
  LocalDateTime findLatestReportedDate();
}
