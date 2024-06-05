package com.hana.ddok.products.repository;

import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.domain.ProductsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findAllByType(ProductsType type);
    Optional<Products> findByType(ProductsType type);
}
