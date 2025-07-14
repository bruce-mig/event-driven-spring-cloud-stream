package com.github.bruce_mig.decision.integration;

import com.github.bruce_mig.decision.domain.Decision;
import com.github.bruce_mig.decision.enumerated.State;
import com.github.bruce_mig.decision.repository.DecisionRepository;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.Optional;

@SpringBootTest
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.github.bruce-mig:customer:+:stubs"
)
@Slf4j
public class HandleCustomerCreatedEventIT {

    @Autowired
    private StubFinder stubFinder;

    @Autowired
    private DecisionRepository decisionRepository;


    @Test
    void handleEvent(){
        this.stubFinder.trigger("shouldPublishCustomerCreated");

        Awaitility.await().untilAsserted(()-> {
            Optional<Decision> optionalDecision = this.decisionRepository.findAll().stream().findAny();
            Assertions.assertTrue(optionalDecision.isPresent());

            Decision decision = optionalDecision.get();

            Assertions.assertNotNull(decision.getId());
            Assertions.assertNotNull(decision.getState());
            Assertions.assertNotNull(decision.getSsn());

            Assertions.assertEquals(888888888, decision.getSsn().getSsn());
            Assertions.assertEquals(State.REJECTED, decision.getState());
        });

    }
}
