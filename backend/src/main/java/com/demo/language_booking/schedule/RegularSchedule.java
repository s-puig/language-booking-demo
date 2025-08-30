package com.demo.language_booking.schedule;


import com.demo.language_booking.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.BitSet;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "regular_schedule")
public class RegularSchedule {
    @Id
    @OneToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;

    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, length = 64)
    private String timezone;

    @Column(nullable = false, columnDefinition = "BINARY(21)")
    BitSet availableTime;
}
