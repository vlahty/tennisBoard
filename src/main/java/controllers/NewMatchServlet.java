package controllers;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Match;
import models.additional.MatchScore;
import services.OngoingMatchesService;

import java.io.IOException;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("newMatch.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String playerName1 = req.getParameter("player1");
        String playerName2 = req.getParameter("player2");

        //TODO: Добавить проверку на уникальность playerNames

        OngoingMatchesService oms = (OngoingMatchesService) req.getServletContext()
                .getAttribute("ongoingMatchesService");

        MatchScore ms = oms.generateNewMatchScore(playerName1, playerName2);

        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + ms.getMatchId());
    }

}




