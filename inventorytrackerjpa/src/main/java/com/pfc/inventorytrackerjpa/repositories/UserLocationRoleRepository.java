package com.pfc.inventorytrackerjpa.repositories;

import com.pfc.inventorytrackerjpa.entities.UserLocationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLocationRoleRepository extends JpaRepository<UserLocationRole, Long> {

    List<UserLocationRole> findAllByUserId(long userId);

    List<UserLocationRole> findAllByLocationId(long locationId);

    boolean existsByUserIdAndLocationIdAndRoleId(long userId, long locationId, long roleId);

    long countByUserIdAndLocationId(long userId, long locationId);
}