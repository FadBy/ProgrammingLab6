package server;

public class ClientQuestion implements ClientRequest {
    public final String value;
    public final String fieldName;

    public ClientQuestion(String value, String fieldName) {
        this.value = value;
        this.fieldName = fieldName;

    }
}
