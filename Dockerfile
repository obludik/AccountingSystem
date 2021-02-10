FROM openjdk:13-slim
COPY target/*.original /app/app.jar
COPY native-libs/*.so /app/native-libs/
COPY native-libs/*.dll /app/native-libs/
COPY target/libs/*.jar /app/libs/
COPY run.sh /app/run.sh
RUN chmod +x /app/run.sh
RUN apt-get update \
  && apt-get -y install curl \
  && apt-get -y install dos2unix \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*
RUN dos2unix /app/run.sh
ENTRYPOINT ["/app/run.sh"]

