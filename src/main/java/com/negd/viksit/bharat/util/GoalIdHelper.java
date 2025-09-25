package com.negd.viksit.bharat.util;

import com.negd.viksit.bharat.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoalIdHelper {

    private static GoalRepository goalRepository;

    @Autowired
    public GoalIdHelper(GoalRepository goalRepository) {
        GoalIdHelper.goalRepository = goalRepository;
    }

    public static synchronized long getNextGoalNumber() {
        return goalRepository.getMaxGoalNumber() + 1;
    }
}

