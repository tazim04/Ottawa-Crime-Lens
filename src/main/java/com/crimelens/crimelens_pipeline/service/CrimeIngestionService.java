package com.crimelens.crimelens_pipeline.service;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.IngestionResult;
import com.crimelens.crimelens_pipeline.mapper.CrimeRecordMapper;
import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import com.crimelens.crimelens_pipeline.repository.CrimeRecordRepository;
import java.util.ArrayList;
import java.util.List;
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

  public IngestionResult run() {
    log.info("Ingestion in progress.........");
    int offset = 0;

    int totalFetched = 0;
    int totalInserted = 0;

    while (true) {
      List<FeatureDTO> features = apiClient.fetchCrimeData(offset, PAGE_SIZE);

      if (features.isEmpty()) {
        log.info(
            "No more records returned. Ingestion complete! fetched={}, inserted={}",
            totalFetched,
            totalInserted);
        break;
      }

      totalFetched += features.size();

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

    int duplicates = totalFetched - totalInserted;

    return new IngestionResult(totalFetched, totalInserted, duplicates);
  }
}
