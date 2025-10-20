package com.demo.language_booking.users;

import com.demo.language_booking.common.Country;
import com.demo.language_booking.lessons.Lesson;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
	// Lessons are never deleted, instead they are soft-deleted. CascadeType.REMOVE is only for testing purposes.
	@OneToMany(mappedBy = "tutor", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	//@Immutable
			Set<Lesson> lessons;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;
	@Size(min = 5, max = 32, message = "Username must be between 5 and 32 characters long")
	@NotBlank(message = "Username cannot be empty")
	@Column(name = "username", nullable = false, unique = true, length = 32)
	private String username;
	@Size(min = 5, max = 64, message = "Email must be at most 64 characters long")
	@Column(name = "email", nullable = false, unique = true, length = 64)
	private String email;
	@Column(name = "password", nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	@Column(name = "country_code", length = 2, nullable = false)
	private Country countryCode;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<UserLanguageLevel> spokenLanguages = new HashSet<>();
	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	@ColumnDefault("'STUDENT'")
	private Role role = Role.STUDENT;
	@Column(name = "created_at", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public enum Role {
		STUDENT,
		TEACHER,
		ADMIN
	}
}
