package edu.kcc;

import edu.kcc.entity.Event;
import edu.kcc.request.GetEventRequest;
import edu.kcc.response.GetEventResponse;

import java.io.*;

import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Properties properties = new Properties();
        properties.load(new FileInputStream("config/application.config"));
        String portProperty = properties.getProperty("edu.kcc.Main.port", "9876");
        int port = Integer.parseInt(portProperty);

        try(var s = new Socket("localhost", port);){

            var outStream = new ObjectOutputStream(s.getOutputStream());

            // create an event
            Event event = new Event(0, "Cubs game", LocalDate.of(2024, 7, 18), LocalTime.of(19, 0, 0), "Cubs vs Brewers");

            GetEventRequest req = new GetEventRequest("Create", event, null);
            outStream.writeObject(req);

            var inStream = new ObjectInputStream(s.getInputStream());
            GetEventResponse resp = (GetEventResponse) inStream.readObject();

            if (resp.getStatusCode() == 200){
                System.out.print("Success! -> " + resp.getEvents().getFirst().toString());
            }
            else{
                System.out.println("Failed to create event: -> " + resp.getErrorMessage());
            }


            // get all events
            GetEventRequest req2 = new GetEventRequest("GetAll", null, null);
            outStream.writeObject(req2);
            GetEventResponse resp2 = (GetEventResponse) inStream.readObject();

            if (resp2.getStatusCode() == 200){
                System.out.println("Successfully got all events!");
                resp2.getEvents().forEach(System.out::println);
            }
            else{
                System.out.println("Failed to get All events -> " + resp2.getErrorMessage());
            }


            // get events for a date
            GetEventRequest req3 = new GetEventRequest("GetByDate", null, LocalDate.of(2024, 7, 18));
            outStream.writeObject(req3);
            GetEventResponse resp3 = (GetEventResponse) inStream.readObject();

            if (resp3.getStatusCode() == 200){
                System.out.println("Successfully got all events!");
                resp3.getEvents().forEach(System.out::println);
            }
            else{
                System.out.println("Failed to get All events -> " + resp3.getErrorMessage());
            }

            // send a disconnect event
            GetEventRequest req4 = new GetEventRequest("Disconnect", null, null);
            outStream.writeObject(req4);
        }
    }
}