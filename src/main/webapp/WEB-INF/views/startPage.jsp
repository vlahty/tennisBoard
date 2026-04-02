<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Главная страница</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .container {
            max-width: 600px;
            width: 100%;
            margin: 20px;
            background: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 2em;
        }

        h2 {
            color: #4CAF50;
            margin: 30px 0 20px;
            font-size: 1.5em;
        }

        ul {
            list-style: none;
            padding: 0;
            margin: 0 0 30px;
        }

        li {
            margin: 15px 0;
        }

        a {
            display: inline-block;
            padding: 12px 30px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-size: 16px;
            transition: background-color 0.3s;
            width: 200px;
        }

        a:hover {
            background-color: #45a049;
        }

        p {
            color: #666;
            line-height: 1.6;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Главная страница</h1>

    <h2>Навигация</h2>
    <ul>
        <li><a href="${pageContext.request.contextPath}/new-match">Новый матч</a></li>
        <li><a href="${pageContext.request.contextPath}/matches">Завершенные матчи</a></li>
    </ul>

    <p>Теннисное табло - приложение для отслеживания теннисных матчей</p>
</div>
</body>
</html>