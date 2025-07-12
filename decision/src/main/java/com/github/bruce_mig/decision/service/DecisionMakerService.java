package com.github.bruce_mig.decision.service;

import com.github.bruce_mig.decision.domain.Decision;

import java.time.LocalDate;

public interface DecisionMakerService {
    Decision decide(Integer ssn, LocalDate birthDate);
}
