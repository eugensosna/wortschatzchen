package ua.sosna.wortschatz.wortschatztchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatztchen.domain.File;

public interface FileRepo  extends JpaRepository<File, Long>{

}
