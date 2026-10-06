package com.jeeniv.billboard.repository;


import com.jeeniv.billboard.model.Billboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillboardRepository extends JpaRepository<Billboard, Long> {
    List<Billboard> findByAreaIgnoreCase(String area);

    @Query("SELECT DISTINCT b.area FROM Billboard b ORDER BY b.area ASC")
    List<String> findDistinctAreas();
}