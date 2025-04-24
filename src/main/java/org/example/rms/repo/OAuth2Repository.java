package org.example.rms.repo;

import org.example.rms.entity.OAuth2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2Repository extends JpaRepository<OAuth2, Long> {
    Optional<OAuth2> findByProviderUserIdAndProviderName(String providerUserId, String providerName);

    Optional<OAuth2> findByCode(String code);
}
