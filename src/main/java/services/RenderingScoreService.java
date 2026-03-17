package services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Match;
import models.additional.MatchScore;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.HibernateRunner;
import java.io.IOException;
import java.util.UUID;

public class RenderingScoreService {

    CalculatingScoreService calculatingScoreService;

    public RenderingScoreService(CalculatingScoreService calculatingScoreService) {
        this.calculatingScoreService = calculatingScoreService;
    }

    public void check(){


    }

    
}
