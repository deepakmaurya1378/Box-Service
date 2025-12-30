package com.tiffinservice.boxservice.vendor.repository;

import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.common.enums.DocumentVerificationStatus;
import com.tiffinservice.boxservice.common.enums.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    boolean existsByEmailOrPhone(String email, String phone);
    Optional<Vendor> findByEmail(String email);
    Optional<Vendor> findByEmailOrPhone(String email, String phone);
    List<Vendor> findByStatus(VendorStatus status);
    List<Vendor> findByDocumentVerificationStatus(DocumentVerificationStatus DocumentVerificationStatus);
    List<Vendor> findByCityAndIsActiveTrue(String city);
}
