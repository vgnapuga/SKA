package com.ska.model.syncable.note;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.ska.model.BaseModel;
import com.ska.model.syncable.note.converter.*;
import com.ska.model.user.User;
import com.ska.vo.encrypted.note.*;
import com.ska.constant.note.*;


@Entity
@Table(name = "notes")
public class Note extends BaseModel {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "fk_note_author"))
    private User user;

    @Column(name = "title", nullable = false, length = NoteTitleConstants.Format.MAX_ENCRYPTED_DATA_SIZE)
    @Convert(converter = NoteTitleConverter.class)
    private EncryptedNoteTitle title;

    @Column(name = "encrypted_content", nullable = false, length = NoteContentConstants.Format.MAX_ENCRYPTED_DATA_SIZE)
    @Convert(converter = NoteContentConverter.class)
    private EncryptedNoteContent encryptedContent;


    public Note() {}

    public Note(final User user, final EncryptedNoteTitle title, final EncryptedNoteContent encryptedContent) {
        this.user = user;
        this.title = title;
        this.encryptedContent = encryptedContent;
    }

    public final void changeTitle(final EncryptedNoteTitle newTitle, final boolean isUniqueForUser) {
        this.title = newTitle;
    }

    public final void changeContent(final EncryptedNoteContent newEncryptedContent) {
        this.encryptedContent = newEncryptedContent;
    }

    public final User getUser() {
        return this.user;
    }

    public final EncryptedNoteTitle getTitle() {
        return this.title;
    }

    public final EncryptedNoteContent getContent() {
        return this.encryptedContent;
    }

    @Override
    public final String toString() {
        return String.format(
                "Note{id=%d, authorId=%d, title=%s, content=***}",
                this.id, this.user.getId(), this.title.toString()
        );
    }
    
}
