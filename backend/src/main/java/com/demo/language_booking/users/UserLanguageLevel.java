package com.demo.language_booking.users;

import com.demo.language_booking.common.CEFRLevel;
import com.demo.language_booking.common.Language;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@Table(name = "user_language_level")
public class UserLanguageLevel {
    @EmbeddedId
    private UserLanguageId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private CEFRLevel level;

    public Language getLanguage() {
        return id != null ? id.getLanguage() : null;
    }

    public void setLanguage(Language language) {
        if (id == null) {
            setId(new UserLanguageId());
        }
        id.setLanguage(language);
    }
}

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
class UserLanguageId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;
}
