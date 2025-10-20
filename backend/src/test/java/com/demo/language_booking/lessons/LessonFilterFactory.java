package com.demo.language_booking.lessons;

import com.demo.language_booking.common.Country;
import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class LessonFilterFactory {
	public static Stream<Arguments> lessonFilterProvider() {
		List<LessonFilter> lessonFilterList = validLessonFilters().toList();
		List<Set<String>> lessonNames = validLessonNameCases().toList();
		return IntStream.range(0, lessonFilterList
						.size())
				.mapToObj(i -> Arguments.of(lessonFilterList.get(i), lessonNames.get(i)));
	}

	private static Stream<Set<String>> validLessonNameCases() {
		return Stream.of(
				Set.of("Vocabulary", "Listening"),
				Set.of("German", "Interview Prep"),
				Set.of("Interview Prep", "Listening"),
				Set.of("German", "Interview Prep", "Listening"),
				Set.of("Listening", "German", "Vocabulary"),
				Set.of("Listening", "German")
		);
	}

	private static Stream<LessonFilter> validLessonFilters() {
		return Stream.of(
				LessonFilter.builder()
						.tutorId(1L)
						.build(),
				LessonFilter.builder()
						.country(Country.DE)
						.build(),
				LessonFilter.builder()
						.lessonCategory(Set.of(LessonCategory.INTERVIEW_PREPARATION, LessonCategory.LISTENING))
						.build(),
				LessonFilter.builder()
						.minPrice(BigDecimal.valueOf(10.0))
						.build(),
				LessonFilter.builder()
						.maxPrice(BigDecimal.valueOf(25.75))
						.build(),
				LessonFilter.builder()
						.minPrice(BigDecimal.valueOf(10.0))
						.maxPrice(BigDecimal.valueOf(25.75))
						.build()
		);
	}
}
