package edu.kcc.request;

import edu.kcc.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEventRequest implements Serializable {
    private String command;
    Event createEventData;
    private LocalDate date;
}
