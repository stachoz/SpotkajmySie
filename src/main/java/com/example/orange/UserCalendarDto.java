package com.example.orange;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserCalendarDto {
    @JsonProperty("working_hours")
    @NotNull
    private TimeInterval workingHours;
    @JsonProperty("planned_meeting")
    @NotNull
    private List<TimeInterval> plannedMeeting;
}