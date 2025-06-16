package com.pepsico.hcmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pepsico.hcmservice.model.HcmRecord;

public interface HcmRepository extends JpaRepository<HcmRecord, Long>{

}
