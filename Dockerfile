FROM gradle:8.7-jdk17-alpine as builder

WORKDIR /app
COPY ./ ./
RUN ./gradlew clean build --no-daemon
# 빌더 이미지에서 애플리케이션 빌드
#COPY ./build/libs/*.jar /app.jar

# APP
FROM openjdk:17.0-slim
WORKDIR /app
# 빌더 이미지에서 jar 파일만 복사
COPY --from=builder /app/build/libs/Ddok-0.0.1-SNAPSHOT.jar .

EXPOSE 8080
# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT ["java", "-jar", "Ddok-0.0.1-SNAPSHOT.jar"]