# transfer-system

Учебная система денежных переводов: ledger с двойной записью, оркестратор переводов с сагой, уведомления через Kafka.

> Проект учебный: микросервисы выбраны для отработки межсервисного взаимодействия, а не из-за нагрузки. Подробнее – [ADR-001](docs/adr/0001-microservices.md).

## Документация

- [Design doc](docs/design-doc.md)
- [Архитектурные решения (ADR)](docs/adr/)

## Запуск

```bash
docker compose up -d 
mvn verify             # сборка и тесты
```

## Стек

Java 21, Spring Boot 4, Maven, PostgreSQL, Hibernate (EntityManager, свои запросы), Liquibase, JUnit 5, Testcontainers. Дальше: gRPC, Kafka, Kotlin, Grafana.
