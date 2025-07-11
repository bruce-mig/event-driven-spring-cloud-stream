package com.github.bruce_mig.decision.repository;

import com.github.bruce_mig.decision.domain.Decision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisionRepository extends JpaRepository<Decision, Long> {
}
