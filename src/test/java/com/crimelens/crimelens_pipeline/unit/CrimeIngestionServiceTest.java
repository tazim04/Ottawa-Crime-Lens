package com.crimelens.crimelens_pipeline.unit;

import static org.mockito.Mockito.*;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.mapper.CrimeRecordMapper;
import com.crimelens.crimelens_pipeline.repository.CrimeRecordRepository;
import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import com.crimelens.crimelens_pipeline.util.DummyFeatureFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrimeIngestionServiceTest {

  private static final int PAGE_SIZE = 2000;

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
    when(client.fetchCrimeData(0, PAGE_SIZE)).thenReturn(List.of(feature));

    // Second page is empty -> ingestion stops
    when(client.fetchCrimeData(PAGE_SIZE, PAGE_SIZE)).thenReturn(List.of());

    service.run();

    verify(repository).saveAll(any());
    verify(client, times(2)).fetchCrimeData(anyInt(), eq(PAGE_SIZE));
  }
}
