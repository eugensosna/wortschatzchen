package ua.sosna.wortschatz.wortschatztchen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatztchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatztchen.domain.TimestampRow;

public interface TimestampRowRepo extends JpaRepository<TimestampRow, Long>{
	List<TimestampRow> findAllBySubtitleFile(SubtitleFile subtitleFile);

	

}
