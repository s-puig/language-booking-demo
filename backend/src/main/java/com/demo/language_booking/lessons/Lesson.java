package com.demo.language_booking.lessons;

import com.demo.language_booking.users.User;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a language lesson entity in the system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "lessons")
@SQLDelete(sql = "UPDATE lessons SET deleted_at = now() WHERE lesson_id=?")
@SQLRestriction("deleted_at IS null")
public class Lesson {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "lesson_id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User tutor;

	@Column(name = "lesson_name", nullable = false, length = 32)
	private String name;

	@Column(name = "description", length = 256)
	private String description;

	@Column(name = "price", precision = 10, scale = 2, nullable = false)
	private BigDecimal price;

	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = LessonCategory.class)
	@Fetch(FetchMode.JOIN)
	@CollectionTable(name = "lesson_categories", joinColumns = @JoinColumn(name = "lesson_id"))
	@Column(name = "category", nullable = false)
	private Set<LessonCategory> lessonCategories;

	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
}
