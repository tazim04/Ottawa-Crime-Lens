package com.crimelens.crimelens_pipeline.service;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.mapper.CrimeRecordMapper;
import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import com.crimelens.crimelens_pipeline.repository.CrimeRecordRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrimeIngestionService {

  private static final int PAGE_SIZE = 2000;

  private final OttawaCrimeApiClient apiClient;
  private final CrimeRecordRepository repository;
  private final CrimeRecordMapper mapper;

  public void run() {
    log.info("Ingestion in progress.........");
    LocalDateTime lastRepDate =
        Optional.ofNullable(repository.findLatestReportedDate())
            .orElse(LocalDateTime.of(1970, 1, 1, 0, 0));
    log.info("Last reported date: {}", lastRepDate);
    int offset = 0;

    int totalInserted = 0;
    while (true) {
      List<FeatureDTO> features = apiClient.fetchCrimeData(offset, PAGE_SIZE, lastRepDate);

      if (features.isEmpty()) {
        log.info("No more records returned. Ingestion complete! Total inserted={}", totalInserted);
        break;
      }

      // Convert features from DTO -> Entity
      List<CrimeRecord> records = new ArrayList<>(features.size());
      for (FeatureDTO f : features) {
        records.add(mapper.toEntity(f));
      }

      // Store in DB
      repository.saveAll(records);
      totalInserted += records.size();
      log.info("Inserted {} records so far (offset={})", totalInserted, offset);
      offset += PAGE_SIZE;
    }
  }
}
