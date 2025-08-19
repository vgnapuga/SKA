package com.ska.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ska.model.note.Note;


@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {}
