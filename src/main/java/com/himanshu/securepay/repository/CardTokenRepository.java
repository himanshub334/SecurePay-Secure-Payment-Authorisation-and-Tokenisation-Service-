package com.himanshu.securepay.repository;
import com.himanshu.securepay.entity.CardToken; import org.springframework.data.jpa.repository.JpaRepository;
public interface CardTokenRepository extends JpaRepository<CardToken,String>{}
