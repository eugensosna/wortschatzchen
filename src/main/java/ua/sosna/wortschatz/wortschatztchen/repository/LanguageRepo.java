package ua.sosna.wortschatz.wortschatztchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatztchen.domain.Language;

public interface LanguageRepo extends JpaRepository<Language, Long> {

}
