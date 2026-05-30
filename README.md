# Онлайн платформа мини-игр

Учебный проект: клиент-серверное веб-приложение на Java 25 + Tomcat 11 + PostgreSQL 18.

## Стек
- Java 25 / Jakarta Servlet 6 / JSP + JSTL
- PostgreSQL 18 (JDBC + HikariCP)
- BCrypt (jbcrypt) для хранения паролей
- Maven (war), Docker Compose
- JUnit 5 для логики игр

## Запуск

```bash
docker compose up --build
```

Открыть: <http://localhost:9999/>

Учётка администратора: **admin / admin123**

## Игры
- **Слоты** — 3 барабана, 5 символов; выплаты: 7×50, ⭐×20, 🔔×10, 🍋×5, 🍒×3
- **Рулетка** — европейская (0–36); ставки на число (×36), цвет, чёт/нечёт (×2)
- **Блэкджек** — классические правила, дилер до 17, BJ 3:2; действия Hit/Stand/Double

## Структура
```
src/main/java/com/example/minigames/
  controller/  — сервлеты
  service/     — бизнес-логика (auth, баланс, транзакции игр)
  dao/         — JDBC-доступ
  model/       — сущности
  game/        — движки игр (SecureRandom)
  security/    — фильтр аутентификации/ролей
  util/        — DataSource
src/main/resources/db/migration/V1__init.sql
src/main/webapp/WEB-INF/jsp/  — представления
```

## Тесты
```bash
mvn test
```
