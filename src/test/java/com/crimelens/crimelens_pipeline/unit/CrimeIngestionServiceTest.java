package com.crimelens.crimelens_pipeline.unit;

import static org.mockito.Mockito.*;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.mapper.CrimeRecordMapper;
import com.crimelens.crimelens_pipeline.repository.CrimeRecordRepository;
import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import com.crimelens.crimelens_pipeline.util.DummyFeatureFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrimeIngestionServiceTest {

  private static final int PAGE_SIZE = 1000;
  private static final LocalDateTime DATE_DEFAULT = LocalDateTime.of(1970, 1, 1, 0, 0);

  CrimeIngestionService service;
  CrimeRecordRepository repository = mock(CrimeRecordRepository.class);
  OttawaCrimeApiClient client = mock(OttawaCrimeApiClient.class);
  CrimeRecordMapper mapper = mock(CrimeRecordMapper.class);

  @BeforeEach
  void setup() {
    service = new CrimeIngestionService(client, repository, mapper);
  }

  @Test
  void ingestsAndPersistsRecords() {
    // Dummy data
    FeatureDTO feature = DummyFeatureFactory.createDummyFeature();

    // First page returns data
    when(client.fetchCrimeData(0, PAGE_SIZE, DATE_DEFAULT)).thenReturn(List.of(feature));

    // Second page is empty -> ingestion stops
    when(client.fetchCrimeData(PAGE_SIZE, PAGE_SIZE, DATE_DEFAULT)).thenReturn(List.of());

    service.ingest();

    verify(repository).saveAll(any());
    verify(client, times(2)).fetchCrimeData(anyInt(), eq(PAGE_SIZE), eq(DATE_DEFAULT));
  }
}
