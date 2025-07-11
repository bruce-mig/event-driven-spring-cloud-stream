package com.github.bruce_mig.decision.service;

import java.time.LocalDate;

public interface DecisionMakerService {
    void decide(Integer ssn, LocalDate birthDate);
}
