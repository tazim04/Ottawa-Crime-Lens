FROM eclipse-temurin:21-jre

# Create non-root user
RUN useradd -r -u 1001 appuser

WORKDIR /app

# copy jar
COPY target/crimelens-pipeline-0.0.1-SNAPSHOT.jar app.jar

# JVM options optimized for containers & batch jobs
ENV JAVA_OPTS="\
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75 \
  -XX:+ExitOnOutOfMemoryError \
  -Djava.security.egd=file:/dev/./urandom \
"

# Drop root
USER appuser

ENTRYPOINT ["java","-jar","/app/app.jar"]
