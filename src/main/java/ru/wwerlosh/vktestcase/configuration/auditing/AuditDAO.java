package ru.wwerlosh.vktestcase.configuration.auditing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditDAO extends JpaRepository<Audit, Long> {
}
