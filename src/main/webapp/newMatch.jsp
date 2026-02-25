<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Новый матч</title>
</head>
<body>
<h1>Новый теннисный матч</h1>

<form action="/new-match" method="post">
    <label for="player1">Имя игрока 1:</label>
    <input type="text" id="player1" name="player1" required>
    <br><br>

    <label for="player2">Имя игрока 2:</label>
    <input type="text" id="player2" name="player2" required>
    <br><br>

    <button type="submit">Начать матч</button>
</form>

<br>
<a href="startPage.jsp">Вернуться на главную</a>
</body>
</html>