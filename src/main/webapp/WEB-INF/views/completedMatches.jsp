<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Сыгранные матчи</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }

        .container {
            position: relative;
            max-width: 1000px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #333;
            text-align: center;
        }

        .filter-form {
            margin: 20px 0;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 5px;
        }

        .filter-form input[type="text"] {
            padding: 8px;
            width: 300px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .filter-form button {
            padding: 8px 20px;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .filter-form button:hover {
            background: #45a049;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:hover {
            background-color: #f5f5f5;
        }

        .pagination {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin: 20px 0;
        }

        .pagination a, .pagination span {
            padding: 8px 16px;
            text-decoration: none;
            border: 1px solid #ddd;
            color: #333;
            border-radius: 4px;
        }

        .pagination a:hover {
            background-color: #ddd;
        }

        .pagination .current {
            background-color: #4CAF50;
            color: white;
            border: 1px solid #4CAF50;
        }

        .no-matches {
            text-align: center;
            padding: 40px;
            color: #666;
        }

        .home-link {
            position: absolute;
            bottom: 40px;
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
    <h1>Сыгранные матчи</h1>

    <!-- Форма фильтрации -->
    <div class="filter-form">
        <form action="${pageContext.request.contextPath}/matches" method="get">
            <label for="playerName">Поиск по имени игрока:</label>
            <input type="text" id="playerName" name="filter_by_player_name"
                   value="${filterName}" placeholder="Введите имя игрока">
            <button type="submit">Искать</button>
            <c:if test="${not empty filterName}">
                <a href="${pageContext.request.contextPath}/matches" style="margin-left: 10px;">Сбросить</a>
            </c:if>
        </form>
    </div>

    <!-- Список матчей -->
    <c:choose>
        <c:when test="${empty matches}">
            <div class="no-matches">
                <h3>Матчи не найдены</h3>
                <p>Попробуйте изменить параметры поиска</p>
            </div>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>Игрок 1</th>
                    <th>Игрок 2</th>
                    <th>Победитель</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="match" items="${matches}">
                    <tr>
                        <td>${match.player1.name}</td>
                        <td>${match.player2.name}</td>
                        <td>${match.winner.name}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <!-- Пагинация -->
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/matches?page=${currentPage - 1}&filter_by_player_name=${filterName}">←</a>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/matches?page=${i}&filter_by_player_name=${filterName}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/matches?page=${currentPage + 1}&filter_by_player_name=${filterName}">→</a>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>

    <a href="${pageContext.request.contextPath}/" class="home-link">
        Вернуться на главную
    </a>

</div>

</body>
</html>