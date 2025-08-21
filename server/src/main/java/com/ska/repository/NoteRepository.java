package com.ska.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ska.model.syncable.note.Note;
import com.ska.model.user.User;
import com.ska.vo.encryptable.note.EncryptedNoteTitle;



@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByUserAndTitle(User user, EncryptedNoteTitle title);

}
