package ua.sosna.wortschatz.wortschatzchen.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatzchen.domain.FileEntity;

public interface FileRepo  extends JpaRepository<FileEntity, Long>{

	Optional<FileEntity> findByUuid(UUID idToUpdate);

}
