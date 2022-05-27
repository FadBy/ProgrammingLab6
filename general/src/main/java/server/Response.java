package server;

import java.io.Serializable;

public class Response implements Serializable {
    public String text;

    public Response(String text) {
        this.text = text;
    }
}
