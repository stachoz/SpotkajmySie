package com.example.orange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {

    private static CalendarService calendarService;

    @BeforeEach
    void init() {
        calendarService = new CalendarService();
    }

    @Test
    public void basicFuncTest1(){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse("00:30"));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("19:55")),
                List.of(
                        new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("10:30")),
                        new TimeInterval(LocalTime.parse("12:00"), LocalTime.parse("13:00")),
                        new TimeInterval(LocalTime.parse("16:00"), LocalTime.parse("18:00"))
                )
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("18:30")),
                List.of(
                        new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("11:30")),
                        new TimeInterval(LocalTime.parse("12:30"), LocalTime.parse("14:30")),
                        new TimeInterval(LocalTime.parse("14:30"), LocalTime.parse("15:00")),
                        new TimeInterval(LocalTime.parse("16:00"), LocalTime.parse("17:00"))
                )
        ));
        List<TimeInterval> expected = List.of(
                new TimeInterval(LocalTime.parse("11:30"), LocalTime.parse("12:00")),
                new TimeInterval(LocalTime.parse("15:00"), LocalTime.parse("16:00")),
                new TimeInterval(LocalTime.parse("18:00"), LocalTime.parse("18:30"))
        );
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"00:30", "00:15", "00:10", "00:05"})
    public void basicFuncTest2(String meetingDuration){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse(meetingDuration));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("19:55")),
                List.of(
                        new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("10:30")),
                        new TimeInterval(LocalTime.parse("16:00"), LocalTime.parse("18:00"))
                )
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("18:30")),
                List.of(
                        new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("11:30")),
                        new TimeInterval(LocalTime.parse("12:30"), LocalTime.parse("14:30")),
                        new TimeInterval(LocalTime.parse("14:30"), LocalTime.parse("15:00")),
                        new TimeInterval(LocalTime.parse("16:00"), LocalTime.parse("17:00"))
                )
        ));
        List<TimeInterval> expected = List.of(
                new TimeInterval(LocalTime.parse("11:30"), LocalTime.parse("12:30")),
                new TimeInterval(LocalTime.parse("15:00"), LocalTime.parse("16:00")),
                new TimeInterval(LocalTime.parse("18:00"), LocalTime.parse("18:30"))
        );
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void basicFuncTest3(){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse("00:30"));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("19:55")),
                List.of(
                        new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("10:00")),
                        new TimeInterval(LocalTime.parse("12:00"), LocalTime.parse("14:00")),
                        new TimeInterval(LocalTime.parse("16:00"), LocalTime.parse("19:55"))
                )
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("19:55")),
                List.of(
                        new TimeInterval(LocalTime.parse("10:30"), LocalTime.parse("11:30")),
                        new TimeInterval(LocalTime.parse("13:00"), LocalTime.parse("15:00")),
                        new TimeInterval(LocalTime.parse("15:30"), LocalTime.parse("19:55"))
                )
        ));
        List<TimeInterval> expected = List.of(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("10:30")),
                new TimeInterval(LocalTime.parse("11:30"), LocalTime.parse("12:00")),
                new TimeInterval(LocalTime.parse("15:00"), LocalTime.parse("15:30"))
        );
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        assertThat(result).isEqualTo(expected);
    }


    @ParameterizedTest
    @ValueSource(strings = {"01:01", "02:00", "12:00"})
    public void meetingDurationLongerThenFreeTime(String meetingDuration){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse(meetingDuration));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("19:55")),
                List.of(
                        new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("10:30")),
                        new TimeInterval(LocalTime.parse("16:00"), LocalTime.parse("18:00"))
                )
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("18:30")),
                List.of(
                        new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("11:30")),
                        new TimeInterval(LocalTime.parse("12:30"), LocalTime.parse("14:30")),
                        new TimeInterval(LocalTime.parse("14:30"), LocalTime.parse("15:00")),
                        new TimeInterval(LocalTime.parse("16:00"), LocalTime.parse("17:00"))
                )
        ));
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        int resultSize = result.size();
        assertThat(resultSize).isEqualTo(0);
    }


    @ParameterizedTest
    @ValueSource(strings = {"13:00", "14:00"})
    public void shouldReturnEmptyList_SeparableWorkingHours(String workStart){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse("00:30"));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("13:00")),
                List.of()
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse(workStart), LocalTime.parse("18:00")),
                List.of()
        ));
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        int resultSize = result.size();
        assertThat(resultSize).isEqualTo(0);
    }

    @Test
    public void shouldReturnEmptyList_NoFreeTime(){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse("00:30"));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("13:00")),
                List.of(
                        new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("13:00"))
                )
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("18:00")),
                List.of(
                        new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("18:00"))
                )
        ));
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        int resultSize = result.size();
        assertThat(resultSize).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"00:05", "00:01", "00:40", "04:00"})
    public void noPlannedMeetings_theSameWorkingHours(String meetingDuration){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse(meetingDuration));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("13:00")),
                List.of()
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("13:00")),
                List.of()
        ));
        List<TimeInterval> expected = List.of(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("13:00"))
        );
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void noPlannedMeetings_DifferentWorkingHours1(){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse("00:30"));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("13:00")),
                List.of()
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("14:00")),
                List.of()
        ));
        List<TimeInterval> expected = List.of(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("13:00"))
        );
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void noPlannedMeetings_DifferentWorkingHours2(){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse("00:30"));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("14:00")),
                List.of()
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("09:00"), LocalTime.parse("13:00")),
                List.of()
        ));
        List<TimeInterval> expected = List.of(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("13:00"))
        );
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void noPlannedMeetings_DifferentWorkingHours3(){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse("00:30"));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("14:00")),
                List.of()
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("11:00"), LocalTime.parse("13:00")),
                List.of()
        ));
        List<TimeInterval> expected = List.of(
                new TimeInterval(LocalTime.parse("11:00"), LocalTime.parse("13:00"))
        );
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void theSamePlannedMeetings(){
        MeetingsTimeRequestDto dto = new MeetingsTimeRequestDto();
        dto.setMeetingDuration(LocalTime.parse("00:30"));
        dto.setFirstCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("18:00")),
                List.of(
                        new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("10:05")),
                        new TimeInterval(LocalTime.parse("11:00"), LocalTime.parse("11:05")),
                        new TimeInterval(LocalTime.parse("12:00"), LocalTime.parse("12:03"))
                )
        ));
        dto.setSecondCalendar(new UserCalendarDto(
                new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("18:00")),
                List.of(
                        new TimeInterval(LocalTime.parse("10:00"), LocalTime.parse("10:05")),
                        new TimeInterval(LocalTime.parse("11:00"), LocalTime.parse("11:05")),
                        new TimeInterval(LocalTime.parse("12:00"), LocalTime.parse("12:03"))
                )
        ));
        List<TimeInterval> expected = List.of(
                new TimeInterval(LocalTime.parse("10:05"), LocalTime.parse("11:00")),
                new TimeInterval(LocalTime.parse("11:05"), LocalTime.parse("12:00")),
                new TimeInterval(LocalTime.parse("12:03"), LocalTime.parse("18:00"))
        );
        List<TimeInterval> result = calendarService.getMeetingTimes(dto);
        assertThat(result).isEqualTo(expected);
    }
}