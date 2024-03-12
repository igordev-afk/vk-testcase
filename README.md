# VK INTERNSHIP 2024 TESTCASE
## Краткое описание
В ходе проекта был реализован Proxy REST API для перенаправления запросов на https://jsonplaceholder.typicode.com/

Учтены все требования технического задания, а именно:
- Стэк технологий: Java 17, Spring, PostgreSQL
- [Реализованы все основные обработчики](#rest-api)
- [Реализован inmemory-cache](#caching)
- [Реализованна базовая авторизация](#authorization)
- [Проработанная ролевая модель доступа](#authorization)
- [Реализовано ведение аудита действий](#auditing)
- Для простоты запуска приложения использован сборщик Gradle
- [Использована база данных для хранения данных пользователей и журнала аудита](#data-storage)
- Написаны юнит тесты, покрывающие основной функционал REST API
- [Реализована конечная точка для запросов по websocket](#websocket)

**Подробнее о реализации ниже**

## REST API
Реализованы 3 основных обработчика:
- `/api/posts/**`
- `/api/users/**`
- `/api/albums/**`

В каждом из обработчиков присутствуют все основные HTTP методы (**GET**, **POST**, **PUT**, **PATCH**, **DELETE**). <br>
В качестве HTTP клиента использована библиотека `apache httpclient`

Для создания ваших собственных обработчиков с основными HTTP методами 
достаточно сконфигурировать объект обработчика в классе конфигурации `HttpClientConfiguration`:
```` java
@Bean
public HttpClient<AlbumDTO> albumDTOHttpClient(@Value("${spring.api.albums.url}") String URL,
                                                 ObjectMapper objectMapper) {
    return new HttpClient<>(URL, objectMapper, AlbumDTO.class);
}
````

В качестве типа укажите DTO, с которым вы работаете.

Также реализован дополнительный обработчик для работы с сервисными пользователями:
- `**POST /users**` (создание нового системного пользователя, дефолтная роль устанавливается согласно полю в конфигурации `spring.security.defaultRole`)
- `PATCH /admin/users/{id}` (добавление роли для пользователя, доступен для пользователей с ролью ROLE_ADMIN)

## Validation
Реализована валидация входящих данных с помощью Spring Validation и Jakarta Validation

## Caching
Для кэширования данных используется Spring Cache, для установки политики кэширования используется библиотека `Caffeine`

По стандарту политика такая: если данные в кэшэ не использовались `spring.cache.expires-in-minutes` минут, то данные удаляются из кэша

## Authorization
При реализации базовой авторизации было принято решение использовать более гибкую модель доступа:
- у каждого пользователя есть роль
- у каждой роли могут быть свойственные только ей привелегии, а также все привилегии ролей, которые находятся ниже уровнем по дереву иерархии
- доступ к методам обработчиков можно настраивать как по ролям, так и по привилегиям

Дерево иерархии ролей настраивается механизмами Spring Security:
``` java
@Bean
public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    String hierarchy =
                    "ROLE_ADMIN > ROLE_USERS_EDITOR\n" +
                    "ROLE_ADMIN > ROLE_POSTS_EDITOR\n" +
                    "ROLE_ADMIN > ROLE_ALBUMS_EDITOR\n" +
                    "ROLE_USERS_EDITOR > ROLE_USERS_VIEWER\n" +
                    "ROLE_POSTS_EDITOR > ROLE_POSTS_VIEWER\n" +
                    "ROLE_ALBUMS_EDITOR > ROLE_ALBUMS_VIEWER\n" +
                    "ROLE_CLOWN > ROLE_USERS_VIEWER\n" +
                    "ROLE_CLOWN > ROLE_POSTS_VIEWER\n" +
                    "ROLE_CLOWN > ROLE_ALBUMS_VIEWER";
    roleHierarchy.setHierarchy(hierarchy);
    return roleHierarchy;
}
```

На boot этапе приложения (если `spring.security.setupRequired` установлен на true) будут 
созданы все основные роли, привилегии. Все роли получат как свои свойственные привилегии, 
так и все привилегии, которые находятся у ролей уровнем ниже.

Рекомендуется запускать приложение с данной настройкой единожды, чтобы избежать лишних select запросов на этапе boot.

В моем REST API реализовано 8 ролей
- `ADMIN` (доступ ко всем обработчикам)
- `USERS_EDITOR` (доступ к POST, PUT, PATCH, DELETE методам /api/users/**)
- `USERS_VIEWER` (доступ к GET методам /api/users/**)
- `POSTS_EDITOR` (доступ к POST, PUT, PATCH, DELETE методам /api/posts/**)
- `POSTS_VIEWER` (доступ к GET методам /api/posts/**)
- `ALBUMS_EDITOR` (доступ к POST, PUT, PATCH, DELETE методам /api/albums/**)
- `ALBUMS_VIEWER` (доступ к GET методам /api/albums/**)
- `CLOWN` (доступ ко всем GET методам всех обработчиков, реализована для демонстрации гибкости модели доступа)
- `WEBSOCKET_USER` (доступ к конечной точке /ws)

![img_2](https://github.com/igordev-afk/vk-testcase/assets/66678952/f25ed7ec-6722-4c5b-b59e-1fcd4c65e092)

## Auditing
При каждой попытке пользователя достучаться до определенного метода обработчика, создается новая запись в таблице журналирования.
Сохраняются следующие данные:
- `метод обработчика`, к которому была попытка получить доступ
- `ip`
- `HTTP метод`
- `статус` (GRANTED, либо DENIED)
- `время попытки`
- `имя пользователя` (в случае, если пользователь пытается получить доступ без базовой авторизации, 
используется дефолтный Spring Security username `anonymousUser`)

![img_3](https://github.com/igordev-afk/vk-testcase/assets/66678952/6a1f34af-81df-4d60-9336-13d33bb6a88a)

## Data storage
Для хранения данных о пользователях и сохранения записей при журналировании используется PostgreSQL

Демонстрация отношения таблиц:

![img_4](https://github.com/igordev-afk/vk-testcase/assets/66678952/6c7fd3bf-82d9-4897-afb2-2732092d2399)

## Websocket
Реализована конечная точка `/ws` для запросов по websocket. 
Доступ к конечной точке разрешен только для пользователей с привелегией `WEBSOCKET_PRIVILEGE`. 
Так же, как и с обычными обработчиками, при любой попытке получить доступ к конечной точке создается новая запись в таблице журналирования.

При установке соединения, пользователь может отправлять сообщения через конечную точку приложения на эхо сервер https://websocket.org/tools/websocket-echo-server/ и получать ответ.
