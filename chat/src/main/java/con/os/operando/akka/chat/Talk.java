package con.os.operando.akka.chat;

import java.io.Serializable;

public class Talk implements Serializable {
    private final String contents;

    public Talk(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }
}