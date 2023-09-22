FROM amazoncorretto:19.0.2-alpine
WORKDIR /app
COPY target/schoolie-image.jar schoolie-image.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","schoolie-image.jar"]

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz