package com.example.orange;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/meeting-time")
    @ResponseStatus(HttpStatus.OK)
    public List<TimeInterval> countPossibleTimeMeeting(@Valid @RequestBody MeetingsTimeRequestDto dto){
        return calendarService.getMeetingTimes(dto);
    }
}
