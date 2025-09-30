package com.negd.viksit.bharat.util;

import com.negd.viksit.bharat.repository.GoalRepository;
import com.negd.viksit.bharat.repository.ReformMilestoneRepository;
import com.negd.viksit.bharat.repository.RegulatoryReformRepository;
import org.springframework.stereotype.Component;

@Component
public class IdGenHelper {

    private static GoalRepository goalRepository;
    private static RegulatoryReformRepository regulatoryReformRepository;
    private static ReformMilestoneRepository reformMilestoneRepository;

    public IdGenHelper(GoalRepository goalRepository, RegulatoryReformRepository regulatoryReformRepository, ReformMilestoneRepository reformMilestoneRepository) {
        this.goalRepository = goalRepository;
        this.regulatoryReformRepository = regulatoryReformRepository;
        this.reformMilestoneRepository = reformMilestoneRepository;
    }

    public static synchronized long getNextGoalNumber() {
        return goalRepository.countAllIncludingDeleted() + 1;
    }

    public static synchronized long getNextReformNumber() {
        return regulatoryReformRepository.countAllIncludingDeleted() + 1;
    }

    public static synchronized long getNextMileStoneNumber() {
        return reformMilestoneRepository.countAllIncludingDeleted() + 1;
    }
}

