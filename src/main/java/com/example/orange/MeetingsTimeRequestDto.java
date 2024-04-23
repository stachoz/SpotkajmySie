package com.example.orange;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class MeetingsTimeRequestDto {
    @JsonProperty("meeting_duration")
    @NotNull
    private LocalTime meetingDuration;
    @JsonProperty("first_calendar")
    @Valid
    @NotNull
    private UserCalendarDto firstCalendar;
    @JsonProperty("second_calendar")
    @Valid
    @NotNull
    private UserCalendarDto secondCalendar;
}
