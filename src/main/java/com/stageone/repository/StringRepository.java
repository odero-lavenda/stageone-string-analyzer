package com.stageone.repository;

import com.stageone.entity.StringEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StringRepository extends JpaRepository<StringEntity, String>, JpaSpecificationExecutor<StringEntity> {

    @Query("SELECT s FROM StringEntity s WHERE s.value = :value")
    Optional<StringEntity> findByValue(@Param("value") String value);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM StringEntity s WHERE s.value = :value")
    boolean existsByValue(@Param("value") String value);

}

