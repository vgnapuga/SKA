package com.ska.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ska.model.note.Note;
import com.ska.vo.note.Title;



@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    boolean existsByTitle(Title title);
    Note getByTitle(Title title);

}
