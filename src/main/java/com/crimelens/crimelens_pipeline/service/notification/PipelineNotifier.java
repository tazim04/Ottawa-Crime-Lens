package com.crimelens.crimelens_pipeline.service.notification;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile({"prod", "dev"})
public class PipelineNotifier {

  private final SNSMessageBuilder messageBuilder;
  private final SNSPublisher publisher;

  public void running(String runId) {
    publisher.publish(messageBuilder.buildRunningMessage(runId));
  }

  public void success(
      String runId, Duration durationSeconds, int fetched, int inserted, int duplicates) {
    publisher.publish(
        messageBuilder.buildSuccessMessage(runId, durationSeconds, fetched, inserted, duplicates));
  }

  public void failure(String runId, Exception e) {
    publisher.publish(messageBuilder.buildFailureMessage(runId, e.getMessage()));
  }
}
