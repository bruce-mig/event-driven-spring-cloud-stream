package com.github.bruce_mig.decision.domain;

import com.github.bruce_mig.decision.domain.enumerated.State;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Decision {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private State state;
    private SSN ssn;

    private Decision(State state, SSN ssn) {
        this.state = state;
        this.ssn = ssn;
    }

    public static Decision decide(SSN ssn, LocalDate birthDate){
        Period creditLimitDuration = Period.ofYears(40);
        var maximumCustomerYears = 70;

        LocalDate maximumAllowedAge = LocalDate.now().plus(creditLimitDuration);

        long customerYearsInTheEndOfCredit = ChronoUnit.YEARS.between(birthDate, maximumAllowedAge);
        if (customerYearsInTheEndOfCredit > maximumCustomerYears){
            return new Decision(State.REJECTED, ssn);
        } else if (ssn.getSsn()  % 2 == 0) {
            return new Decision(State.APPROVED, ssn);
        }
        return new Decision(State.REJECTED, ssn);
    }


}
