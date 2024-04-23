package com.example.orange;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

@Service
public class CalendarService {

    public List<TimeInterval> getMeetingTimes(MeetingsTimeRequestDto dto){
        List<TimeInterval> freeTime1 = findFreeTimeIntervals(dto.getFirstCalendar());
        List<TimeInterval> freeTime2 = findFreeTimeIntervals(dto.getSecondCalendar());
        Duration meetingDuration = Duration.between(LocalTime.MIN, dto.getMeetingDuration());
        return findTimeIntervalsForMeeting(freeTime1, freeTime2, meetingDuration);
    }

    private List<TimeInterval> findTimeIntervalsForMeeting(List<TimeInterval> freeTime1, List<TimeInterval> freeTime2, Duration meetingDuration) {
        List<TimeInterval> meetings = new ArrayList<>();
        for (TimeInterval ft1 : freeTime1) {
            for (TimeInterval ft2 : freeTime2) {
                findCommonPartOfIntervals(ft1, ft2)
                        .filter(timeInterval -> Duration.between(timeInterval.getStart(), timeInterval.getEnd()).compareTo(meetingDuration) >= 0)
                        .ifPresent(meetings::add);
            }
        }
        return meetings;
    }

    private List<TimeInterval> findFreeTimeIntervals(UserCalendarDto userCalendarDto){
        List<TimeInterval> freeIntervals = new ArrayList<>();
        TimeInterval workingHours = userCalendarDto.getWorkingHours();
        LocalTime lastIntervalEnd = workingHours.getStart();
        LocalTime workEnd = workingHours.getEnd();
        for (TimeInterval plannedMeeting : userCalendarDto.getPlannedMeeting()) {
            LocalTime meetingStart = plannedMeeting.getStart();
            if(lastIntervalEnd.isBefore(meetingStart)) {
                freeIntervals.add(new TimeInterval(lastIntervalEnd, meetingStart));
            }
            lastIntervalEnd = plannedMeeting.getEnd();
        }
        if(lastIntervalEnd.isBefore(workEnd)){
            freeIntervals.add(new TimeInterval(lastIntervalEnd, workEnd));
        }
        return freeIntervals;
    }

    private Optional<TimeInterval> findCommonPartOfIntervals(TimeInterval t1, TimeInterval t2) {
        LocalTime t1Start = t1.getStart();
        LocalTime t2Start = t2.getStart();
        LocalTime t1End = t1.getEnd();
        LocalTime t2End = t2.getEnd();

        LocalTime firstEnd = t1End.isBefore(t2End) ? t1End : t2End;
        LocalTime lastStart = t1Start.isBefore(t2Start) ? t2Start : t1Start;

        if(lastStart.isAfter(firstEnd)) return Optional.empty();
        return Optional.of( new TimeInterval(lastStart, firstEnd));
    }
}