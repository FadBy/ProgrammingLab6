package server;

import exceptions.ApplicationException;

import java.io.*;

public class SerializeParser {
    public static Serializable toSerializable(byte[] bytes) throws ApplicationException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        try (ObjectInputStream input = new ObjectInputStream(byteStream)){
            return (Serializable) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new ApplicationException(e.getMessage() + ": couldn't serialize");
        }
    }

    public static byte[] fromSerializable(Serializable ser) throws ApplicationException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)){
            objectStream.writeObject(ser);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ApplicationException(e.getMessage() + ": couldn't serialize");
        }
    }
}
