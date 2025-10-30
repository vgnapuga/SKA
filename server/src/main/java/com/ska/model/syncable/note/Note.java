package com.ska.model.syncable.note;


import com.ska.model.syncable.BaseSyncableModel;
import com.ska.model.syncable.note.converter.NoteContentConverter;
import com.ska.model.syncable.note.converter.NoteTitleConverter;
import com.ska.model.syncable.note.vo.NoteContent;
import com.ska.model.syncable.note.vo.NoteTitle;
import com.ska.model.user.User;
import com.ska.util.constant.NoteConstants;
import com.ska.util.constant.UserConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "notes")
public class Note extends BaseSyncableModel {

    @Column(name = "title", nullable = false, length = NoteConstants.Title.MAX_ENCRYPTED_DATA_SIZE)
    @Convert(converter = NoteTitleConverter.class)
    private NoteTitle title;

    @Column(name = "encrypted_content", nullable = false, length = NoteConstants.Content.MAX_ENCRYPTED_DATA_SIZE)
    @Convert(converter = NoteContentConverter.class)
    private NoteContent encryptedContent;

    protected Note() {
    }

    public Note(final User owner, NoteTitle encryptedTitle, NoteContent encryptedContent) {
        this.owner = java.util.Objects.requireNonNull(owner, UserConstants.NULL_MESSAGE);
        this.title = java.util.Objects.requireNonNull(encryptedTitle, NoteConstants.Title.NULL_MESSAGE);
        this.encryptedContent = java.util.Objects.requireNonNull(encryptedContent, NoteConstants.Content.NULL_MESSAGE);
    }

    public final void changeTitle(NoteTitle newEncryptedTitle) {
        this.title = java.util.Objects.requireNonNull(newEncryptedTitle, NoteConstants.Title.NULL_MESSAGE);
    }

    public final void changeContent(NoteContent newEncryptedContent) {
        this.encryptedContent = java.util.Objects.requireNonNull(
                newEncryptedContent,
                NoteConstants.Content.NULL_MESSAGE);
    }

    public NoteTitle getTitle() {
        return this.title;
    }

    public NoteContent getContent() {
        return this.encryptedContent;
    }

    @Override
    public final String toString() {
        return String.format("Note{id=%s, ownerId=%d, title=***, content=***}", this.id, this.owner.getId());
    }

}
