package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , String> {
    //search
    Page<Product> findByTitleContaining(String title,Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);
    Page<Product> findByLiveFalse(Pageable pageable);
}
