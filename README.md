# Spring Boot приложение Filmorate

## ER-диаграмма

![Диаграмма](/src/main/resources/schema.svg)

# Описание базы данных

База данных состоит из нескольких таблиц, которые связаны между собой через внешние ключи. Основные таблицы:

## Таблицы
### 1. `users`
Хранит информацию о пользователях:
- `id` - уникальный идентификатор пользователя (первичный ключ).
- `email` - электронная почта пользователя.
- `login` - логин пользователя.
- `name` - имя пользователя (может быть пустым).
- `birthday` - дата рождения пользователя.

### 2. `friedship`
Хранит информацию о дружбе между пользователями:
- `user_id1` и `user_id2` - идентификаторы пользователей, которые являются друзьями.

### 3. `film`
Хранит информацию о фильмах:
- `id` - уникальный идентификатор фильма (первичный ключ).
- `name` - название фильма.
- `description` - описание фильма.
- `releaseDate` - дата выхода фильма.
- `duration` - продолжительность фильма в минутах.
- `mpa_id` - идентификатор рейтинга MPA (внешний ключ к таблице `mpa_rating`).

### 4. `genre`
Хранит информацию о жанрах фильмов:
- `id` - уникальный идентификатор жанра (первичный ключ).
- `name` - название жанра.

### 5. `film_genre`
Связывает фильмы с жанрами:
- `film_id` - идентификатор фильма (внешний ключ к таблице `film`).
- `genre_id` - идентификатор жанра (внешний ключ к таблице `genre`).

### 6. `mpa_rating`
Хранит информацию о рейтингах MPA:
- `mpa_rating_id` - уникальный идентификатор рейтинга (первичный ключ).
- `mpa_name` - название рейтинга (например, "G", "PG", "PG-13" и т.д.).

### 7. `like`
Хранит информацию о лайках, которые пользователи ставят фильмам:
- `film_id` - идентификатор фильма (внешний ключ к таблице `film`).
- `user_id` - идентификатор пользователя (внешний ключ к таблице `users`).
## Примеры SQL запросов

### Добавить нового пользователя
```sql
INSERT INTO users (email, login, name, birthday)
VALUES ('email@email.com', 'login', 'Name', '2000-01-01');
```

### Добавить фильм
```sql
INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('name', 'description film', '2000-01-01', 100, 3);
```

### Выбор всех фильмов определенного жанра (например, "Комедия"):
```sql
SELECT f.id, f.name, f.description, f.releaseDate, f.duration
FROM films f
JOIN film_genres fg ON f.id = fg.film_id
JOIN genres g ON fg.genre_id = g.id
WHERE g.name = 'Комедия';
```

### Добавление лайка от пользователя к фильму:
```sql
INSERT INTO likes (film_id, user_id)
VALUES (1, 1);
```
### Получение топ-5 рейтинговых фильмов
```sql
SELECT f.id, f.name, f.description, f.releaseDate, f.duration, COUNT(l.user_id) AS likes_count
FROM films f
LEFT JOIN likes l ON f.id = l.film_id
GROUP BY f.id
ORDER BY likes_count DESC
LIMIT 5;
```