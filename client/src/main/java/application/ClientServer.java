package application;

import exceptions.ApplicationException;
import exceptions.ApplicationRuntimeException;
import server.SerializeParser;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientServer {
    private final ByteBuffer buffer = ByteBuffer.allocate(65536);
    private final DatagramChannel server;
    private SocketAddress address;

    public ClientServer(SocketAddress address) throws ApplicationException {
        this.address = address;
        try {
            server = DatagramChannel.open().bind(null);
            server.configureBlocking(false);
            server.socket().setSoTimeout(10000);
        } catch (IOException e) {
            throw new ApplicationException("couldn't open a channel");
        }
    }

    public Serializable receive() throws ApplicationException {
        try {
            address = server.receive(buffer);
            if (address == null) {
                return null;
            }
            return SerializeParser.toSerializable(buffer.array());
        } catch (IOException e) {
            throw new ApplicationException("server couldn't receive");
        } catch (ClassNotFoundException e) {
            throw new ApplicationException("Got invalid object");
        } finally {
            buffer.clear();
        }
    }

    public void send(Serializable ser) throws ApplicationException {
        if (address == null) {
            throw new ApplicationRuntimeException("Attempt to send without address");
        }
        try {
            server.send(ByteBuffer.wrap(SerializeParser.fromSerializable(ser)), address);
            address = null;
        } catch (IOException e) {
            throw new ApplicationException("server couldn't send");
        }
    }
}
