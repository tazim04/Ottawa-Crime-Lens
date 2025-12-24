package com.crimelens.crimelens_pipeline.service;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.OttawaCrimeApiResponseDTO;
import com.crimelens.crimelens_pipeline.mapper.CrimeRecordMapper;
import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import com.crimelens.crimelens_pipeline.repository.CrimeRecordRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrimeIngestionService {

  private final OttawaCrimeApiClient apiClient;
  private final CrimeRecordRepository repository;
  private final CrimeRecordMapper mapper;

  public void ingest() {

    OttawaCrimeApiResponseDTO response = apiClient.fetchCrimeData();

    if (response == null || response.getFeatures() == null) {
      return;
    }

    // Ingest crime records in chunks of 1000 since the Ottawa Police dataset includes 200k+ records
    List<CrimeRecord> chunk = new ArrayList<>();
    for (FeatureDTO f : response.getFeatures()) {
      CrimeRecord record = mapper.toEntity(f);
      chunk.add(mapper.toEntity(f));

      if (chunk.size() == 1000) {
        repository.saveAll(chunk);
        chunk.clear();
      }
    }

    if (!chunk.isEmpty()) {
      repository.saveAll(chunk);
    }
  }
}
