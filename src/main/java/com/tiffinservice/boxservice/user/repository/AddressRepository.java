package com.tiffinservice.boxservice.user.repository;

import com.tiffinservice.boxservice.common.enums.AddressType;
import com.tiffinservice.boxservice.user.entity.Address;
import com.tiffinservice.boxservice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    Optional<Address> findByUser_IdAndAddressType(
            Long userId,
            AddressType addressType
    );

    boolean existsByUser_IdAndAddressType(
            Long userId,
            AddressType addressType
    );
}
