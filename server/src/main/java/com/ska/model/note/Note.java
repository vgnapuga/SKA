package com.ska.model.note;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.ska.model.BaseModel;
import com.ska.model.user.User;
import com.ska.vo.note.*;
import com.ska.constant.note.*;
import com.ska.model.note.converter.*;


@Entity
@Table(name = "notes")
public class Note extends BaseModel {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "fk_note_author"))
    private User user;

    @Column(name = "title", nullable = false, length = TitleConstants.Numeric.MAX_LENGTH)
    @Convert(converter = TitleConverter.class)
    private Title title;

    @Column(name = "content", nullable = false, length = ContentConstants.Numeric.MAX_LENGTH)
    @Convert(converter = ContentConverter.class)
    private Content content;


    public Note() {}

    public Note(final Title title, final Content content) {
        this.title = title;
        this.content = content;
    }


    public final User getUser() {
        return this.user;
    }

    public final Title getTitle() {
        return this.title;
    }

    public final Content getContent() {
        return this.content;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Note))
            return false;

        Note note = (Note) obj;
        return java.util.Objects.equals(note.id, this.id);
    }

    @Override
    public final int hashCode() {
        return java.util.Objects.hash(this.id);
    }

    @Override
    public final String toString() {
        return String.format(
                "Note{id=%d, authorId=%d, title=%s, content=%s}",
                this.id, this.user.getId(), this.title.toString(), this.content.toString()
        );
    }
    
}
