package com.crimelens.crimelens_pipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrimeLensPipelineApplication {

  public static void main(String[] args) {
    var context = SpringApplication.run(CrimeLensPipelineApplication.class, args);
    var exitCode = SpringApplication.exit(context);
    System.exit(exitCode);
  }
}
