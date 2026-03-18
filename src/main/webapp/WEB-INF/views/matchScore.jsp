<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Таблица матча</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
        }

        table {
            border-collapse: collapse;
            width: 700px;
            background-color: white;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        th, td {
            border: 2px solid #333;
            padding: 15px;
            text-align: center;
        }

        th {
            background-color: #4CAF50;
            color: white;
            font-size: 18px;
            height: auto;
        }

        .player-name {
            text-align: left;
            font-weight: bold;
            width: 300px;
        }

        .score-column {
            width: 133px;
        }

        .container {
            display: flex;
            gap: 50px; /* расстояние между кнопками и таблицей */
        }

        .buttons-container {
            display: flex;
            flex-direction: column;
            margin-top: 50px;
            gap: 5px; /* убраны отступы между кнопками */
        }

        .player-button {
            width: 133px;           /* ширина как у второго столбца */
            height: 53px;           /* высота под размер ячейки */
            padding: 0;
            font-size: 16px;
            border: 2px solid #333;
            border-radius: 0;
            cursor: pointer;
            transition: all 0.2s;
            box-sizing: border-box;
        }

        .player1-btn {
            background-color: #4CAF50;
            color: white;
            font-weight: bold;
        }

        .player2-btn {
            background-color: #2196F3;
            color: white;
            font-weight: bold;
        }

        /* Убираем стандартные отступы у форм */
        form {
            margin: 0;
            padding: 0;
            gap: 5px;
        }

    </style>
</head>
<body>
<div class="container">
    <div class="buttons-container">
        <form action="${pageContext.request.contextPath}/match-score?uuid=${requestScope.matchScore.matchId}" method="post">
            <input type="hidden" name="action" value="player1">
            <button type="submit" class="player-button player1-btn">Очко игроку 1</button>
        </form>

        <form action="${pageContext.request.contextPath}/match-score?uuid=${requestScope.matchScore.matchId}" method="post">
            <input type="hidden" name="action" value="player2">
            <button type="submit" class="player-button player2-btn">Очко игроку 2</button>
        </form>
    </div>

    <!-- Таблица (без изменений) -->
    <table>
        <thead>
        <tr>
            <th>ИГРОКИ</th>
            <th>ГЕЙМ</th>
            <th>СЕТ</th>
            <th>СЧЕТ</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="player-name1">${requestScope.matchScore.player1Name}</td>
            <td class="score-points1">${requestScope.display.player1Points}</td>
            <td class="score-games1">${requestScope.display.player1Games}</td>
            <td class="score-sets1">${requestScope.display.player1Sets}</td>
        </tr>
        <tr>
            <td class="player-name2">${requestScope.matchScore.player2Name}</td>
            <td class="score-points2">${requestScope.display.player2Points}</td>
            <td class="score-games2">${requestScope.display.player2Games}</td>
            <td class="score-sets2">${requestScope.display.player2Sets}</td>
        </tr>

        </tbody>
    </table>
</div>

</body>
</html>