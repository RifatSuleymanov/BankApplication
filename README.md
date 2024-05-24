**Сервис для “банковских” операций. В нашей системе есть пользователи (клиенты), у каждого клиента есть строго один “банковский аккаунт”, в котором изначально лежит какая-то сумма. Деньги можно переводить между клиентами. На средства также начисляются проценты.**

Стек:
1. _Java 17_
2. _Spring Boot 3_
3. _База данных PostgreSQL_
4. _Maven_
5. _REST API_
6. _Spring Security_
7. _Логирование @Slf4j_

База данных PosgreSQL

![image](https://github.com/RifatSuleymanov/BankApplication/assets/117975440/3f0c393a-d3ef-41c1-9d2d-5732ad9ba59a)

Функционал:    
1. _В системе есть пользователи, у каждого пользователя есть строго один “банковский аккаунт”. У пользователя также есть телефон и email. Телефон и или email должен быть минимум один._
2. _На “банковском счету” должна быть какая-то изначальная сумма. Также у пользователя должна быть указана дата рождения и ФИО._
3. _Для простоты будем считать что в системе нет ролей, только обычные клиенты.
        Пусть будет служебный апи (с открытым доступом), через который можно заводить новых пользователей в системе, указав логин, пароль, изначальную сумму, телефон и email (логин, телефон и email не должны быть заняты)._
4. _Баланс счета клиента не может уходит в минус ни при каких обстоятельствах._
5. _Пользователь может добавить/сменить свои номер телефона и/или email, если они еще не заняты другими пользователями._
6. _Пользователь может удалить свои телефон и/или email. При этом нельзя удалить последний._
7. _Остальные данные пользователь не может менять._
8. _Сделал АПИ поиска. Искать можно любого клиента. Должна быть фильтрация и пагинация/сортировка._


     Фильтры:
           a)	Если передана дата рождения, то фильтр записей, где дата рождения больше чем переданный в запросе.
           b)	Если передан телефон, то фильтр по 100% сходству.
           c)	Если передано ФИО, то фильтр по like форматом ‘{text-from-request-param}%’
           d)	Если передан email, то фильтр по 100% сходству.

9. _Доступ к АПИ аутентифицирован (кроме служебного апи для создания новых клиентов)._
10. _Раз в минуту баланс каждого клиента увеличиваются на 5% но не более 207% от начального депозита._


      Например:
            Было: 100, стало: 105.
            Было: 105, стало:110.25.

11. _Реализовал функционал перевода денег с одного счета на другой. Со счета аутентифицированного пользователя, на счёт другого пользователя._
12. _Сделал все необходимые валидации и потокобезопасной._


