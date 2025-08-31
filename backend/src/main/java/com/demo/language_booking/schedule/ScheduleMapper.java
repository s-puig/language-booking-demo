package com.demo.language_booking.schedule;

import com.demo.language_booking.schedule.dto.Schedule;
import com.demo.language_booking.schedule.dto.ScheduleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Locale;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ScheduleMapper {
    ScheduleMapper SCHEDULE_MAPPER = Mappers.getMapper(ScheduleMapper.class);

    RegularSchedule toRegularSchedule(ScheduleRequest schedule);
    Schedule toSchedule(RegularSchedule schedule);

    default BitSet map(boolean[][] weekHours) {
        BitSet bitSet = new BitSet(7*24);

        IntStream.range(0, 7 * 24)
                .filter(i -> weekHours[i / 24][i % 24]) // true slots only
                .forEach(bitSet::set);

        return bitSet;
    }

    default byte[] map(BitSet bitSet) {
        if (bitSet == null) throw new IllegalArgumentException("availableHours cannot be null");
        return bitSet.toByteArray();
    }

    default ZoneId map(String timezone) {
        return ZoneId.of(timezone);
    }

    default String map(ZoneId timezone) {
        return timezone.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}
