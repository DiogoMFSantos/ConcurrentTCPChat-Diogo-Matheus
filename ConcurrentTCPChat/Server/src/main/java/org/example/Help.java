package org.example;



public class Help  implements FtpCommand {

    private ClientHandler clientHandler;

    public Help(ClientHandler clientHandler) {

        this.clientHandler = clientHandler;
    }




    @Override
    public boolean run(){

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(">>> AVAILABLE COMMANDS:\n");
        stringBuilder.append("-------------------------------------------------------------------\n");

        for (Commands commands : Commands.values()) {

            stringBuilder.append(">>> /")
                    .append(commands.name().toLowerCase())
                    .append(" -> ")
                    .append(commands.getDescription())
                    .append("\n");
        }

        stringBuilder.append("\n");

        clientHandler.send(stringBuilder.toString());
        return true;
    }

}
