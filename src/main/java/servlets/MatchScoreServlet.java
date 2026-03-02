package servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.secondary.Score;
import services.OngoingMatchesService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        String uuid = req.getParameter("uuid");


        OngoingMatchesService ongoingMatchesService =
                (OngoingMatchesService) servletContext.getAttribute("ongoingMatchesService");

        Score ongoingMatchScore =
                ongoingMatchesService.getOngoingMatchScore(UUID.fromString(uuid));

        req.setAttribute("score", ongoingMatchScore.getScore());
        req.setAttribute("score1", 1);

        System.out.println(ongoingMatchesService.getOngoingMatchDTO(UUID.fromString(uuid)).getId());

        req.getRequestDispatcher("matchScore.jsp").forward(req, resp);
    }
}
