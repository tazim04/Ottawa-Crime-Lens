package com.crimelens.crimelens_pipeline.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.IngestionResult;
import com.crimelens.crimelens_pipeline.mapper.CrimeRecordMapper;
import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import com.crimelens.crimelens_pipeline.repository.CrimeRecordBatchRepository;
import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import com.crimelens.crimelens_pipeline.util.DummyFeatureFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrimeIngestionServiceTest {

  private static final int PAGE_SIZE = 2000;

  CrimeIngestionService service;
  CrimeRecordBatchRepository batchRepository = mock(CrimeRecordBatchRepository.class);
  OttawaCrimeApiClient client = mock(OttawaCrimeApiClient.class);
  CrimeRecordMapper mapper = mock(CrimeRecordMapper.class);

  @BeforeEach
  void setup() {
    service = new CrimeIngestionService(client, batchRepository, mapper);
  }

  @Test
  void ingestsAndPersistsRecords() {
    // Arrange
    FeatureDTO feature = DummyFeatureFactory.createDummyFeature();
    CrimeRecord record = new CrimeRecord();

    when(client.fetchCrimeData(0, PAGE_SIZE)).thenReturn(List.of(feature));
    when(client.fetchCrimeData(PAGE_SIZE, PAGE_SIZE)).thenReturn(List.of());

    when(mapper.toEntity(feature)).thenReturn(record);
    when(batchRepository.insertBatchIgnore(anyList())).thenReturn(1);

    // Act
    IngestionResult result = service.run();

    // Assert
    verify(client, times(2)).fetchCrimeData(anyInt(), eq(PAGE_SIZE));
    verify(batchRepository).insertBatchIgnore(anyList());

    assertEquals(1, result.fetched());
    assertEquals(1, result.inserted());
    assertEquals(0, result.duplicates());
  }
}
