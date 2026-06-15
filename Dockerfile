FROM eclipse-temurin:21-jdk

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package

CMD ["sh", "-c", "java -jar target/*.jar"]
