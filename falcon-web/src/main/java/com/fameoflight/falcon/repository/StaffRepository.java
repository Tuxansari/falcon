package com.fameoflight.falcon.repository;

import com.fameoflight.falcon.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    Optional<Staff> findByStaffId(Long staffId);
}
