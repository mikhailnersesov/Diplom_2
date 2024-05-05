<h1 align="center">Welcome to task 2 of the graduation project in yandex automation course 👋</h1>

> Author: Mikhail Nersesov
>
> GitHub link: https://github.com/mikhailnersesov/Diplom_2

## ✨ Technologies in the project

> SDK: Amazon Corretto 11.0.20
>
> JUnit: 4.13.2
>
> Mockito: 3.8.0
>
> Jacoco plugin: 0.8.7

## 🚀 Usage

To run the tests:

```sh
mvn clean test
```
To get an Allure report:

```sh
mvn allure:serve 
```

## 🚀 Tasks
> + Создай отдельный репозиторий для тестов API.
> + Создай Maven-проект.
> + Подключи JUnit 4, RestAssured и Allure.
> + Напиши тесты.
> - Сделай отчёт в Allure.
> - Для каждой ручки тесты лежат в отдельном классе.
> - Тесты запускаются и проходят.
> - Написаны все тесты, указанные в задании.
> + В тестах проверяется тело и код ответа.
> + Все тесты независимы.
> + Для всех шагов автотестов должна быть использована аннотация @Step.
    Нужные тестовые данные создаются перед тестом и удаляются после того, как он выполнится.
    Сделан Allure-отчёт. Отчёт добавлен в пул-реквест. 
> + Тесты в test/java.
> + В файле pom.xml нет ничего лишнего.
> + Создание пользователя:
> + создать уникального пользователя;
> + создать пользователя, который уже зарегистрирован;
> + создать пользователя и не заполнить одно из обязательных полей.
> + Логин пользователя:
> + логин под существующим пользователем,
> + логин с неверным логином и паролем.
> + Изменение данных пользователя:
> + с авторизацией,
> + без авторизации,
> + Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
> *    Создание заказа:
> + с авторизацией,
> + без авторизации,
> + с ингредиентами,
> + без ингредиентов,
> + с неверным хешем ингредиентов.
> + Получение заказов конкретного пользователя:
> + авторизованный пользователь,
> + неавторизованный пользователь.




