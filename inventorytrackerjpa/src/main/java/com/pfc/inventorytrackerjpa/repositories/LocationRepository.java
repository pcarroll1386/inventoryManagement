package com.pfc.inventorytrackerjpa.repositories;

import com.pfc.inventorytrackerjpa.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{

	@Query(value = "SELECT DISTINCT ul.location_id " +
			"FROM user_location ul " +
			"WHERE ul.user_id = :userId " +
			"UNION " +
			"SELECT DISTINCT ulr.location_id " +
			"FROM user_location_role ulr " +
			"JOIN \"role\" r ON r.id = ulr.role_id " +
			"WHERE ulr.user_id = :userId " +
			"AND r.scope = 'LOCATION' " +
			"AND r.\"role\" IN ('LOCATION_VIEWER', 'LOCATION_USER', 'LOCATION_ADMIN') " +
			"UNION " +
			"SELECT l.id " +
			"FROM location l " +
			"JOIN \"user\" u ON u.id = :userId " +
			"JOIN \"role\" app_r ON app_r.id = u.app_role_id " +
			"WHERE app_r.scope = 'APP' " +
			"AND app_r.\"role\" = 'ROLE_ADMIN'", nativeQuery = true)
	List<Long> findReadableLocationIdsByUserId(@Param("userId") Long userId);
}
