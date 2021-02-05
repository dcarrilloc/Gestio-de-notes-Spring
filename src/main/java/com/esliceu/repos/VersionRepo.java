package com.esliceu.repos;

import com.esliceu.entities.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepo extends JpaRepository<Version, Long> {

}
