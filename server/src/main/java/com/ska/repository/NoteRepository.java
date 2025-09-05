package com.ska.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ska.model.syncable.note.Note;


@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByUuid(UUID uuid);

}
