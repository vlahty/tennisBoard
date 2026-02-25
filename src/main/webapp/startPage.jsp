<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Главная страница</title>
</head>
<body>
<h1>Главная страница</h1>

<h2>Навигация</h2>
<ul>
    <li><a href="/new-match">Новый матч</a></li>
    <li><a href="/matches">Завершенные матчи</a></li>
</ul>

<hr>

<h2>Быстрый переход</h2>
<form action="/new-match" method="get">
    <button type="submit">Начать новый матч</button>
</form>

<br>

<form action="/matches" method="get">
    <button type="submit">Посмотреть завершенные матчи</button>
</form>

<hr>

<p>Теннисный табло - приложение для отслеживания теннисных матчей</p>
</body>
</html>