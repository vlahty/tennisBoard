<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Новый матч</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            position: relative;
            min-height: 300px;
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        label {
            font-weight: bold;
            margin-bottom: 5px;
            color: #555;
        }
        input[type="text"] {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            width: 100%;
            box-sizing: border-box;
        }
        input[type="text"]:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 5px rgba(76,175,80,0.3);
        }
        button[type="submit"] {
            padding: 12px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        button[type="submit"]:hover:not(:disabled) {
            background-color: #45a049;
        }
        button[type="submit"]:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
        }
        #playerError {
            color: #f44336;
            margin: 10px 0;
            padding: 10px;
            background-color: #ffebee;
            border-radius: 4px;
            text-align: center;
        }
        .home-link {
            position: absolute;
            bottom: 20px;
            right: 20px;
            text-decoration: none;
            color: #4CAF50;
            font-weight: bold;
            padding: 8px 15px;
            background-color: #f0f0f0;
            border-radius: 4px;
            transition: background-color 0.3s;
        }
        .home-link:hover {
            background-color: #e0e0e0;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Новый теннисный матч</h1>

    <form action="${pageContext.request.contextPath}/new-match" method="post">
        <label for="player1">Имя игрока 1:</label>
        <input type="text" id="player1" name="player1" required oninput="checkPlayers()">
        <br>

        <label for="player2">Имя игрока 2:</label>
        <input type="text" id="player2" name="player2" required oninput="checkPlayers()">
        <br>

        <div id="playerError" style="display: none;">Игрок не может играть сам с собой!</div>

        <button type="submit" id="submitBtn">Начать матч</button>
    </form>

    <a href="${pageContext.request.contextPath}/" class="home-link">
        Вернуться на главную
    </a>
    <br>
</div>

<script>
    function checkPlayers() {
        const player1 = document.getElementById('player1').value.trim();
        const player2 = document.getElementById('player2').value.trim();
        const errorDiv = document.getElementById('playerError');
        const submitBtn = document.getElementById('submitBtn');

        if (player1 && player2 && player1.toLowerCase() === player2.toLowerCase()) {
            errorDiv.style.display = 'block';
            submitBtn.disabled = true;
        } else {
            errorDiv.style.display = 'none';
            submitBtn.disabled = false;
        }
    }
</script>
</body>
</html>