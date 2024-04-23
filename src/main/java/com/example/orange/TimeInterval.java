package com.example.orange;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeInterval{
    @JsonFormat(pattern = "HH:mm")
    private LocalTime start;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime end;

    @Override
    public String toString() {
        return "[\"" + start + "\",\"" + end + "\"]";
    }
}
