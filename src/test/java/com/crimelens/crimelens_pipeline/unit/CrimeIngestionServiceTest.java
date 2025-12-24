package com.crimelens.crimelens_pipeline.unit;

import static org.mockito.Mockito.*;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.OttawaCrimeApiResponseDTO;
import com.crimelens.crimelens_pipeline.mapper.CrimeRecordMapper;
import com.crimelens.crimelens_pipeline.repository.CrimeRecordRepository;
import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import com.crimelens.crimelens_pipeline.util.DummyFeatureFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrimeIngestionServiceTest {

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
    when(client.fetchCrimeData()).thenReturn(mockResponse());

    service.ingest();

    verify(repository).saveAll(any());
  }

  private OttawaCrimeApiResponseDTO mockResponse() {
    FeatureDTO feature = DummyFeatureFactory.createDummyFeature();

    OttawaCrimeApiResponseDTO response = new OttawaCrimeApiResponseDTO();
    response.setFeatures(List.of(feature));

    return response;
  }
}
