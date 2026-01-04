package com.crimelens.crimelens_pipeline.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@RequiredArgsConstructor
@Profile({"prod", "dev"})
public class SNSPublisher {

  private final SnsClient snsClient;

  @Value("${aws.sns.topic-arn}")
  private String topicArn;

  public void publish(String message) {
    snsClient.publish(r -> r.topicArn(topicArn).message(message));
  }
}
