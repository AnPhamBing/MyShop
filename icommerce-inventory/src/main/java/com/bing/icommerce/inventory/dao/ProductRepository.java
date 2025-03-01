package com.bing.icommerce.inventory.dao;

import com.bing.icommerce.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
