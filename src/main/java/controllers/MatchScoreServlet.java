package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.MatchScoreToMatchMapper;
import models.Match;
import models.additional.MatchScore;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repositories.MatchRepository;
import services.CalculatingScoreService;
import services.OngoingMatchesService;
import utils.HibernateRunner;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    private MatchScore matchScore;
    private OngoingMatchesService ongoingMatchesService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        ongoingMatchesService = (OngoingMatchesService) req.getServletContext()
                .getAttribute("ongoingMatchesService");

        matchScore = ongoingMatchesService.getOngoingMatchScore(
                UUID.fromString(
                        req.getParameter("uuid")));

        req.setAttribute("matchScore", matchScore);

        req.getRequestDispatcher("matchScore.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CalculatingScoreService calculatingScoreService;

        calculatingScoreService = new CalculatingScoreService(matchScore);

        try (SessionFactory sessionFactory = HibernateRunner.buildSessionFactory()) {
            Session currentSession = sessionFactory.getCurrentSession();
            MatchRepository matchRepository = new MatchRepository(sessionFactory);

            Transaction transaction = currentSession.beginTransaction();

            String pointWinner = req.getParameter("action");
            if ("player1".equals(pointWinner)) {
                calculatingScoreService.pointWon(1);
            } else if ("player2".equals(pointWinner)) {
                calculatingScoreService.pointWon(2);
            } else {
                System.out.println("Unknown player name: " + pointWinner);
            }

            if (matchScore.isMatchFinished()){
                Match match = MatchScoreToMatchMapper.getMatch(matchScore);

                ongoingMatchesService.deleteFromOngoingMatches(matchScore.getMatchId());

                System.out.println("Проверка");

                matchRepository.save(match);
                transaction.commit();

                resp.sendRedirect(req.getContextPath()+"/matches");
                return;
            }

            transaction.commit();
        }

        req.setAttribute("matchScore", matchScore);

        req.getRequestDispatcher("matchScore.jsp").forward(req, resp);
    }
}
