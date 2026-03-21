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
import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {

    private MatchScore matchScore;
    private OngoingMatchesService ongoingMatchesService;
    private CalculatingScoreService calculatingScoreService;
    private UUID uuid;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        uuid = UUID.fromString(req.getParameter("uuid"));
        ongoingMatchesService = (OngoingMatchesService) req.getServletContext()
                .getAttribute("ongoingMatchesService");
        if (ongoingMatchesService.getOngoingMatchScore(uuid).isPresent())
            matchScore = ongoingMatchesService.getOngoingMatchScore(uuid).get();
        calculatingScoreService = new CalculatingScoreService(matchScore);

        req.setAttribute("matchScore", matchScore);
        req.setAttribute("display", calculatingScoreService.getScoreDisplay());
        req.getRequestDispatcher("/WEB-INF/views/matchScore.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionFactory sessionFactory = (SessionFactory)
                getServletContext().getAttribute("sessionFactory");
        Session session = sessionFactory.getCurrentSession();
        MatchRepository matchRepository = new MatchRepository(sessionFactory);

        if (ongoingMatchesService.getOngoingMatchScore(uuid).isEmpty()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Uuid is not found or match is over");
            return; //TODO: Обработать ошибку
        }

        String pointWinner = req.getParameter("action");
        if ("player1".equals(pointWinner)) {
            calculatingScoreService.pointWon(1);
        } else if ("player2".equals(pointWinner)) {
            calculatingScoreService.pointWon(2);
        } else {
            System.out.println("Unknown player name: " + pointWinner);
        }

        Transaction transaction = session.beginTransaction();

        if (matchScore.isMatchFinished()) {
            Match match = MatchScoreToMatchMapper.getMatch(req, matchScore);

            ongoingMatchesService.deleteFromOngoingMatches(matchScore.getMatchId());

            matchRepository.save(match);

            transaction.commit();

            resp.sendRedirect(req.getContextPath() + "/matches");
            return;
        }
        transaction.commit();

        req.setAttribute("matchScore", matchScore);
        req.setAttribute("display", calculatingScoreService.getScoreDisplay());
        req.getRequestDispatcher("/WEB-INF/views/matchScore.jsp").forward(req, resp);
    }
}