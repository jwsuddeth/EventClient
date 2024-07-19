package edu.kcc.response;


import edu.kcc.entity.Event;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetEventResponse implements Serializable {
    int statusCode;
    String errorMessage;
    private List<Event> events;
}
