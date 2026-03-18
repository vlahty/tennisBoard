package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Match;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repositories.MatchRepository;
import java.io.IOException;
import java.util.List;

@WebServlet("/matches")
public class CompletedMatchesController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Match> matches;
        long totalMatches;
        int pageSize = 5;
        int pageNumber = 1;
        String pageParam = req.getParameter("page");
        String filterName = req.getParameter("filter_by_player_name");
        SessionFactory sessionFactory = (SessionFactory)
                getServletContext().getAttribute("sessionFactory");
        Session session = sessionFactory.getCurrentSession();

        if (pageParam != null && !pageParam.isEmpty()) {
            pageNumber = Integer.parseInt(pageParam);
            if (pageNumber < 1) pageNumber = 1;
        }

        Transaction transaction = session.beginTransaction();

        MatchRepository matchRepository = new MatchRepository(sessionFactory);
        if (filterName != null && !filterName.trim().isEmpty()) {
            matches = matchRepository.findAllByName(filterName, pageNumber, pageSize);
            totalMatches = matchRepository.totalPagesForNamedMatches(filterName);
        } else {
            matches = matchRepository.findAll(pageNumber, pageSize, "id");
            totalMatches = matchRepository.totalPagesForAllMatches();
        }

        for (Match match : matches) {
            Hibernate.initialize(match.getPlayer1());
            Hibernate.initialize(match.getPlayer2());
            Hibernate.initialize(match.getWinner());
        }
        transaction.commit();

        int totalPages = (int) Math.ceil((double) totalMatches / pageSize);

        req.setAttribute("matches", matches);
        req.setAttribute("currentPage", pageNumber);
        req.setAttribute("filterName", filterName);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/WEB-INF/views/completedMatches.jsp").forward(req, resp);
    }
}

