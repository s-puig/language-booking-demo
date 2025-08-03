package com.demo.language_booking.users;

import com.demo.language_booking.common.CEFRLevel;
import com.demo.language_booking.common.Language;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@IdClass(UserLanguageId.class)
@Table(name = "user_language_level")
public class UserLanguageLevel {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private CEFRLevel level;
}

@Data
class   UserLanguageId implements Serializable {
    private Long user;
    private Language language;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLanguageId that)) return false;
        return Objects.equals(user, that.user) && language == that.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, language);
    }
}
