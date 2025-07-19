package ua.sosna.wortschatz.wortschatzchen.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatzchen.domain.FileEntity;
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;

public interface SubtitleFileRepo extends JpaRepository<SubtitleFile, Long> {
    Optional<SubtitleFile> findByUuid(UUID uuid);

    Optional<SubtitleFile> findByFile(FileEntity item);

}
