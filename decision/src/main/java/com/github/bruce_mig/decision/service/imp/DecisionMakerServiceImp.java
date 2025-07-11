package com.github.bruce_mig.decision.service.imp;

import com.github.bruce_mig.decision.domain.Decision;
import com.github.bruce_mig.decision.domain.SSN;
import com.github.bruce_mig.decision.repository.DecisionRepository;
import com.github.bruce_mig.decision.service.DecisionMakerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class DecisionMakerServiceImp implements DecisionMakerService {

    private final DecisionRepository decisionRepository;

    public DecisionMakerServiceImp(DecisionRepository decisionRepository) {
        this.decisionRepository = decisionRepository;
    }

    @Override
    public void decide(Integer ssn, LocalDate birthDate) {
        Decision decision = Decision.decide(SSN.create(ssn), birthDate);
        Decision decisionCreated = decisionRepository.save(decision);
        log.info("The decision is: {}", decisionCreated);
    }
}
