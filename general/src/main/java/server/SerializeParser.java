package server;

import exceptions.ApplicationException;

import java.io.*;

public class SerializeParser {
    public static Serializable toSerializable(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        try (ObjectInputStream input = new ObjectInputStream(byteStream)){
            return (Serializable) input.readObject();
        }
    }

    public static byte[] fromSerializable(Serializable ser) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
            objectStream.writeObject(ser);
            return outputStream.toByteArray();
        }
    }
}
