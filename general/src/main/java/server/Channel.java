package server;

import exceptions.ApplicationException;
import exceptions.ApplicationRuntimeException;
import server.SerializeParser;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class Channel {
    private final ByteBuffer buffer = ByteBuffer.allocate(65536);
    private final DatagramChannel server;
    private SocketAddress address;

    public Channel(SocketAddress address) throws ApplicationException {
        try {
            server = DatagramChannel.open().bind(address);
        } catch (IOException e) {
            throw new ApplicationException("couldn't open a channel");
        }
    }

    public void setAddress(SocketAddress address) {
        this.address = address;
    }

    public Serializable receive() throws ApplicationException {
        try {
            address = server.receive(buffer);
            if (checkEmptiness(buffer.array())) {
                return null;
            }
            return SerializeParser.toSerializable(buffer.array());
        } catch (IOException e) {
            throw new ApplicationException("server couldn't receive");
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

    private static boolean checkEmptiness(byte[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public void setBlocking(boolean blocking) throws ApplicationException {
        try {
            server.configureBlocking(blocking);
        } catch (IOException e) {
            throw new ApplicationException("server couldn't configureBlocking");
        }
    }

    public SocketAddress getAddress() {
        return address;
    }
}
