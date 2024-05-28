package com.hana.ddok.products.repository;

import com.hana.ddok.products.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findByType(Integer type);
}
