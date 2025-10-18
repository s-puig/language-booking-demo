package com.demo.language_booking.lessons;

import com.demo.language_booking.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "lessons")
public class Lesson {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "lesson_id")
	private long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User tutor;

	@Column(name = "lesson_name", nullable = false, length = 32)
	private String name;

	@Column(name = "description", length = 256)
	private String description;

	@Column(name = "price", precision = 10, scale = 2, nullable = false)
	private BigDecimal price;

	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = LessonCategory.class)
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
