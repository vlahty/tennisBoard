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
import utils.HibernateRunner;

import java.io.IOException;
import java.util.List;

@WebServlet("/matches")
public class CompletedMatchesServlet extends HttpServlet {

    private final int PAGE_SIZE = 2; // Количество матчей на странице
    private MatchRepository matchRepository;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (SessionFactory sessionFactory = HibernateRunner.buildSessionFactory()) {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();
            matchRepository = new MatchRepository(sessionFactory);

            // Получаем параметры из запроса
            String pageParam = req.getParameter("page");
            String filterName = req.getParameter("filter_by_player_name");

            int pageNumber = 1;
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    pageNumber = Integer.parseInt(pageParam);
                    if (pageNumber < 1) pageNumber = 1;
                } catch (NumberFormatException e) {
                    pageNumber = 1;
                }
            }

            List<Match> matches;
            long totalMatches;
            if (filterName != null && !filterName.trim().isEmpty()) {
                matches = matchRepository.findAllByName(filterName, pageNumber, PAGE_SIZE);
                totalMatches = matchRepository.totalPagesForNamedMatches(filterName);
            } else {
                matches = matchRepository.findAll(pageNumber, PAGE_SIZE, "id");
                totalMatches = matchRepository.totalPagesForAllMatches();
            }

            int totalPages = (int) Math.ceil((double) totalMatches / PAGE_SIZE);


            for (Match match : matches){
                Hibernate.initialize(match.getPlayer1());
                Hibernate.initialize(match.getPlayer2());
                Hibernate.initialize(match.getWinner());
            }

            req.setAttribute("matches", matches);
            req.setAttribute("currentPage", pageNumber);
            req.setAttribute("filterName", filterName);
            req.setAttribute("totalPages", totalPages);

            transaction.commit();

            req.getRequestDispatcher("/completedMatches.jsp").forward(req, resp);
        }

    }

}
