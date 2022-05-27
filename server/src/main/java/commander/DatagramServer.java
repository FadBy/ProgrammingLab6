package commander;

import exceptions.ApplicationException;
import exceptions.ApplicationRuntimeException;
import org.jetbrains.annotations.NotNull;
import server.SerializeParser;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class DatagramServer {
    private final DatagramSocket socket;
    InetAddress receiveHost;
    int receivePort = -1;

    public DatagramServer(int port) throws IOException {
        socket = new DatagramSocket(port);
    }

    public void send(@NotNull Serializable toSend) throws IOException {
        if (receivePort == -1 || receiveHost == null) {
            throw new ApplicationRuntimeException("Attempt to send without address");
        }
        byte[] bytes = SerializeParser.fromSerializable(toSend);
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, receiveHost, receivePort);
        socket.send(dp);
    }

    public Serializable receive() throws IOException, ClassNotFoundException {
        byte[] data = new byte[65536];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        receiveHost = packet.getAddress();
        receivePort = packet.getPort();
        return SerializeParser.toSerializable(data);
    }

    public void disconnect() {
        socket.close();
    }


}
