FROM eclipse-temurin:25-jdk-jammy

WORKDIR /app

# Copiar arquivos do Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Garantir permissão de execução do mvnw
RUN chmod +x mvnw

# Baixar dependências (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código-fonte e resources
COPY src ./src

# Build do JAR
RUN ./mvnw clean package -DskipTests

# Expor porta do Spring Boot
EXPOSE 8080

# Rodar a aplicação
CMD ["sh", "-c", "java -jar target/*.jar"]

