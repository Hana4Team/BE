package com.hana.ddok.spend.repository;

import com.hana.ddok.spend.domain.Spend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpendRepository extends JpaRepository<Spend, Long> {
}
