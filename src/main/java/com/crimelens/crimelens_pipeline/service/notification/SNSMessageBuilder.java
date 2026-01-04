package com.crimelens.crimelens_pipeline.service.notification;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SNSMessageBuilder {

  private static final DateTimeFormatter TIME_FMT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z").withZone(ZoneId.systemDefault());

  @Value("${ENVIRONMENT:UNKNOWN}")
  private String environment;

  public String buildRunningMessage(String runId) {
    return """
🚀 CrimeLens Pipeline Started

Environment: %s
Run ID: %s
Start Time: %s

Status: RUNNING
"""
        .formatted(environment, runId, TIME_FMT.format(Instant.now()));
  }

  public String buildSuccessMessage(
      String runId, Duration durationSeconds, int fetched, int inserted, int duplicates) {
    return """
🚔 CrimeLens Pipeline Completed ✅

Environment: %s
Run ID: %s
Duration: %ds

Summary:
• Records fetched: %d
• Records inserted: %d
• Duplicates skipped: %d

Status: SUCCESS
"""
        .formatted(environment, runId, durationSeconds.toSeconds(), fetched, inserted, duplicates);
  }

  public String buildFailureMessage(String runId, String errorMessage) {
    String safeMessage = errorMessage != null ? errorMessage : "No error message provided";

    return """
🚨 CrimeLens Pipeline Failed ❌

Environment: %s
Run ID: %s

Error:
• Message: %s

Status: FAILURE
"""
        .formatted(environment, runId, safeMessage);
  }
}
