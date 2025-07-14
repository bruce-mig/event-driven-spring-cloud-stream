package com.github.bruce_mig.customer.repository;

import com.github.bruce_mig.customer.domain.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

    List<OutboxMessage> findTop10BySentOrderByIdAsc(boolean sent);
}
