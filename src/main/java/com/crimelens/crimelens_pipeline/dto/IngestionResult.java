package com.crimelens.crimelens_pipeline.dto;

public record IngestionResult(int fetched, int inserted, int duplicates) {}
