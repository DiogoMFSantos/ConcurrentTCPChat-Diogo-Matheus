package org.example;

public enum Commands {

    LS("List all Connected Clients"),
    WHISPER("Send a Message to a Specific User"),
    NAME("Change User Name"),
    HELP("List All Available Commands"),
    BYE("Stop Connection"),
    DISCONNECT("Stop Connection"),
    QUIT("Stop Connection"),
    EXIT("Stop Connection");


    private String description;

    Commands(String description){
        this.description=description;
    }

    public String getDescription() {
        return description;
    }
}
