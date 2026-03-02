
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>match-score</title>
</head>
<body>
    <p>session Score: ${pageScope.score}</p>
    <p>session Score: ${requestScope.score}</p>
    <p>session Score: ${sessionScope.score}</p>
    <p>app Score: ${applicationScope.score}</p>
    <p>Score: ${score}</p>
    <p>Score1: ${score1}</p>

</body>
</html>
