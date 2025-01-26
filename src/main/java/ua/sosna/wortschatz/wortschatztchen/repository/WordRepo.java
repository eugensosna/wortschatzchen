package ua.sosna.wortschatz.wortschatztchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatztchen.domain.Word;

public interface WordRepo extends JpaRepository<Word, Long> {

}
