package edu.kcc.entity;


import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event implements Serializable {
    private int id;
    private String title;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String Description;
}
