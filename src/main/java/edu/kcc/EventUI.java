package edu.kcc;

import edu.kcc.entity.Event;
import edu.kcc.request.GetEventRequest;
import edu.kcc.response.GetEventResponse;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Properties;


public class EventUI extends JFrame {

    private int _port;
    private JTextArea _txtArea;

    public EventUI() {

        try {
            loadConfig();
        }
        catch(IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }

        setSize(600, 500);
        setTitle("Event Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container mainPanel = getContentPane();
        mainPanel.setLayout(new BorderLayout());

        _txtArea = new JTextArea();
        mainPanel.add(_txtArea, BorderLayout.CENTER);

        // create a panel with Label, textbox and button
        JPanel topPanel = new JPanel();
        JLabel label = new JLabel("Date:");

        JTextField txtDate = new JTextField(15);

        JButton submit = new JButton("Search");
        submit.addActionListener(evt -> {
            loadEvents(txtDate.getText().trim());
        });

        topPanel.add(label);
        topPanel.add(txtDate);
        topPanel.add(submit);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        setVisible(true);
    }

    private void loadConfig() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("config/application.config"));
        String portProperty = properties.getProperty("edu.kcc.Main.port", "9876");
        _port = Integer.parseInt(portProperty);
    }


    private void loadEvents(String dateText){

        try(var s = new Socket("localhost", _port);){

            var outStream = new ObjectOutputStream(s.getOutputStream());

            // send the request
            if (dateText != null && !dateText.isEmpty()) {

                LocalDate dt = LocalDate.parse(dateText);

                // get events for a date
                GetEventRequest req = new GetEventRequest("GetByDate", null, dt);
                outStream.writeObject(req);
            }
            else {
                // get all events
                GetEventRequest req = new GetEventRequest("GetAll", null, null);
                outStream.writeObject(req);
            }

            StringBuilder bld = new StringBuilder();

            // read the response
            var inStream = new ObjectInputStream(s.getInputStream());
            GetEventResponse resp = (GetEventResponse) inStream.readObject();

            if (resp.getStatusCode() == 200){
                System.out.println("Successfully got all events!");
                resp.getEvents().forEach(evt -> { bld.append(evt.toString() + "\n"); });
            }
            else{
                bld.append("Failed to get events -> " + resp.getErrorMessage());
            }

            // append the results into the text area
            _txtArea.setText(bld.toString());

            // send a disconnect event
            GetEventRequest disconnectReq = new GetEventRequest("Disconnect", null, null);
            outStream.writeObject(disconnectReq);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }


    public static void main(String [] args){
        SwingUtilities.invokeLater( () ->
                new EventUI() );
    }
}