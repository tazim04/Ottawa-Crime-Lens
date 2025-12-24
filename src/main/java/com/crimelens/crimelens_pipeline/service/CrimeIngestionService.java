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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrimeIngestionService {

  private static final int PAGE_SIZE = 1000;

  private final OttawaCrimeApiClient apiClient;
  private final CrimeRecordRepository repository;
  private final CrimeRecordMapper mapper;

  public void ingest() {
    LocalDateTime lastRepDate =
        Optional.ofNullable(repository.findLatestReportedDate())
            .orElse(LocalDateTime.of(1970, 1, 1, 0, 0));
    int offset = 0;

    while (true) {
      List<FeatureDTO> features = apiClient.fetchCrimeData(offset, PAGE_SIZE, lastRepDate);

      if (features.isEmpty()) {

        break;
      }

      // Convert features from DTO -> Entity
      List<CrimeRecord> records = new ArrayList<>(features.size());
      for (FeatureDTO f : features) {
        records.add(mapper.toEntity(f));
      }

      // Store in DB
      repository.saveAll(records);
      offset += PAGE_SIZE;
    }
  }
}
