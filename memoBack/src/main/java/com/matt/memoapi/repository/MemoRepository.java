package com.matt.memoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matt.memoapi.domain.Memo;

//Spring : repository = interface
//JPARepository<Classe/Objet, type primary clé>
@Repository
public interface MemoRepository extends JpaRepository<Memo , Long> {
    
}
