package ua.sosna.wortschatz.wortschatzchen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.domain.TimestampRow;

public interface TimestampRowRepo extends JpaRepository<TimestampRow, Long>{
	List<TimestampRow> findAllBySubtitleFile(SubtitleFile subtitleFile);
	Page<TimestampRow> findAllBySubtitleFile(SubtitleFile subtitleFile, Pageable pageable);
	
    Optional<TimestampRow> findByUuid(UUID uuid);

	// List<TimestampRow> findAllBySubtitleFilePagination(SubtitleFile
	// subtitleFile);

	

}
