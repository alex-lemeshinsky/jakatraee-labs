# Forum Project

## Лабораторна робота №7

У проєкті реалізовано **Container Managed Transactions (CMT)** на рівні бізнес-логіки через EJB.

- `TopicServiceImpl` керує бізнес-операціями над темами.
- `PostModerationServiceImpl` бере участь у тій самій бізнес-операції та працює в тому самому транзакційному контексті.
- Для демонстрації transaction propagation додано операцію **транзакційного закриття теми**:
  вона змінює запис у `topics` і масово оновлює записи в `posts` в межах однієї транзакції.

### Що демонструє лабораторна

1. Успішний сценарій:
- тема отримує `closed = true`;
- усі її дописи отримують службову позначку про закриття;
- транзакція фіксується, зміни зберігаються в БД.

2. Сценарій rollback:
- `TopicServiceImpl` оновлює тему;
- `PostModerationServiceImpl` виконує масове оновлення дописів і далі кидає `TopicClosureException`;
- контейнер відкочує всю транзакцію;
- у БД не зберігаються ні зміна теми, ні зміни дописів.

### REST-демонстрація

Базовий шлях API: `http://localhost:8080/api`

- Успішне закриття теми:

```http
POST /api/topics/1/close
Content-Type: application/json

{
  "simulateFailure": false
}
```

- Демонстрація rollback:

```http
POST /api/topics/1/close
Content-Type: application/json

{
  "simulateFailure": true
}
```

Після другого сценарію тема має лишитися відкритою, а тексти її дописів не повинні змінитися.

### UI-демонстрація

1. Увійти під адміністратором.
2. Відкрити редагування відкритої теми.
3. У блоці "Демонстрація транзакційності" натиснути:
- без прапорця симуляції помилки для commit;
- з прапорцем симуляції помилки для rollback.
4. Після редіректу на список тем перевірити flash-повідомлення та стан теми.

## Swagger / OpenAPI

When the app is running locally on GlassFish (`http://localhost:8080`):

- OpenAPI JSON: `http://localhost:8080/api/openapi.json`
- OpenAPI YAML: `http://localhost:8080/api/openapi.yaml`
