package com.demo.language_booking.lessons;

import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonUpdateRequest;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.UserService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Default implementation of the LessonService interface.
 * Provides implementations for managing lesson entities including creation, retrieval, updating, and deletion.
 */
@RequiredArgsConstructor
@Validated
@Service
public class DefaultLessonService implements LessonService {
	private final LessonRepository lessonRepository;
	private final LessonMapper lessonMapper;
	private final UserService userService;

	/**
	 * Creates a Specification for filtering Lesson entities based on the provided filter criteria.
	 *
	 * @param filter the LessonFilter containing the criteria to filter lessons by
	 * @return a Specification that can be used to query lessons matching the specified criteria
	 */
	public static Specification<Lesson> withFilter(LessonFilter filter) {
		return (root, query, cb) -> {
			Set<Predicate> predicates = new HashSet<>();

			if (filter.getTutorId() != null) {
				predicates.add(cb.equal(root.get("tutor")
						.get("id"), filter.getTutorId()));
			}

			if (filter.getCountry() != null) {
				predicates.add(cb.equal(root.get("tutor")
						.get("countryCode"), filter.getCountry()));
			}

			if (filter.getLessonCategory() != null && !filter.getLessonCategory()
					.isEmpty()) {
				Join<Lesson, LessonCategory> categoryJoin = root.joinSet("lessonCategories", JoinType.INNER);
				predicates.add(categoryJoin.in(filter.getLessonCategory()));
				assert query != null;
				query.distinct(true);
			}

			if (filter.getMinPrice() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
			}

			if (filter.getMaxPrice() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	@Override
	public Optional<Lesson> findById(long id) {
		return lessonRepository.findById(id);
	}

	@NonNull
	@Override
	public List<Lesson> findAll() {
		return lessonRepository.findAll();
	}

	@NonNull
	@Override
	public List<Lesson> findAll(@Valid @NotNull LessonFilter filter) {
		return lessonRepository.findAll(withFilter(filter));
	}

	@Transactional
	@NonNull
	@Override
	public Lesson create(long userId, @Valid @NotNull LessonCreateRequest lessonCreateRequest) {
		Lesson lesson = lessonMapper.toLesson(lessonCreateRequest);
		User user = userService.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
		lesson.setTutor(user);
		return lessonRepository.save(lesson);
	}

	@NonNull
	@Transactional
	@Override
	public Lesson update(long id, @Valid @NotNull LessonUpdateRequest lessonUpdateRequest) {
		Lesson lesson = lessonRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
		lesson.setName(lessonUpdateRequest.getName());
		lesson.setDescription(lessonUpdateRequest.getDescription());
		lesson.setLessonCategories(new HashSet<>(lessonUpdateRequest.getLessonCategories()));
		lesson.setPrice(lessonUpdateRequest.getPrice());

		return lessonRepository.save(lesson);
	}

	@Transactional
	@Override
	public void delete(long id) {
		if (!lessonRepository.existsById(id)) {
			throw new ResourceNotFoundException("Lesson not found with id: " + id);
		}
		lessonRepository.deleteById(id);
	}

	@Transactional
	@NonNull
	@Override
	public Lesson save(Lesson lesson) {
		return lessonRepository.save(lesson);
	}
}
