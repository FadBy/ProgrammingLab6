package commander;

import application.ClientServer;
import exceptions.ApplicationException;
import exceptions.IncorrectCommandException;
import org.jetbrains.annotations.NotNull;
import server.CommandPackage;
import server.Response;
import data.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ServerCommand implements Command {
    private final ClientServer server;
    public final String name;
    public final int argsNumber;
    public final String envObject;
    private final ObjectBuilder builder;


    public ServerCommand(@NotNull String name, int argsNumber, @NotNull String envObject, @NotNull ClientServer server, @NotNull ObjectBuilder builder) {
        this.builder = builder;
        this.name = name;
        this.argsNumber = argsNumber;
        this.envObject = envObject;
        this.server = server;
    }

    @Override
    public String execute(@NotNull List<String> args, @NotNull Input input, @NotNull Output output) throws IncorrectCommandException {
        if (args.size() != argsNumber + 1) {
            throw new IncorrectCommandException("Wrong number of arguments: expected: " + argsNumber + ", actual: " + args.size());
        }
        try {
            Map<String, Map<String, String>> object = null;
            if (!envObject.isEmpty()) {
                object = builder.buildObject(envObject, input, output);
            }
            CommandPackage commandPackage = new CommandPackage(args, object);
            server.send(commandPackage);
            Response response = (Response) server.receive();
            while (response == null) {
                response = (Response) server.receive();
            }
            return response.text;
        } catch (IOException | ApplicationException e) {
            return e.getMessage();
        }
    }
}
