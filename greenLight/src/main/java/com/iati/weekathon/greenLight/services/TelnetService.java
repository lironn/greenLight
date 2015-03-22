package com.iati.weekathon.greenLight.services;

import org.apache.commons.net.telnet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 22/03/15
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TelnetService {

    private final static Logger log = LoggerFactory.getLogger(TelnetService.class);
    private static final String ON = "/on ";
    private static final String OFF = "/off ";
    private static final String START_OF_TELNET_COMMAND = "NPS>";

    @Value("${nps1.ip}")
    private String trafficLightIp;

    @Value("${nps1.port}")
    private int port;

    private PrintStream nps1OutputStream;

    private InputStream nps1InputStream;

    private TelnetClient telnetClient1;

    @PostConstruct
    private void init() throws IOException, InvalidTelnetOptionException {

        telnetClient1 = new TelnetClient();
        telnetClient1.addOptionHandler(new TerminalTypeOptionHandler("VT100", false, false, true, false));
        telnetClient1.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));
        telnetClient1.addOptionHandler(new EchoOptionHandler(true, false, true, false));
        telnetClient1.connect(trafficLightIp, port);
        nps1OutputStream = new PrintStream(telnetClient1.getOutputStream());
        nps1InputStream = telnetClient1.getInputStream();
        String sessionStartText =readUntil(START_OF_TELNET_COMMAND);
        log.info("Started telnetClient1:"+sessionStartText);

    }

    public void sendOnCommand(int lightId) {
        String command = ON + lightId;
        sendCommand(command);

    }

    public void sendOffCommand(int lightId) {
        String command = OFF + lightId;
        sendCommand(command);

    }

    private void sendCommand(String command) {
        log.info("Sending command " + command);
        nps1OutputStream.println(command);
        nps1OutputStream.flush();
        String commandResult = readUntil(START_OF_TELNET_COMMAND);
        log.info("Command Result: \n" + commandResult);
    }

    public String readUntil(String pattern) {
        log.info("Reading from telnet until pattern '"+pattern+"'");
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuilder sb = new StringBuilder();
            char ch = (char) nps1InputStream.read();
            while (true) {
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) nps1InputStream.read();
            }
        } catch (Exception e) {
            log.error("Failed to read telnet inputStream", e);
        }
        log.error("Failed to find pattern '" + pattern + "' from telnet inputStream");
        return null;
    }

}