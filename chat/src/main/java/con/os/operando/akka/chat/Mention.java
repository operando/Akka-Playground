package con.os.operando.akka.chat;

import java.io.Serializable;

public class Mention implements Serializable {
    private final String contents;
    private final String toUser;

    public Mention(String contents, String toUser) {
        this.contents = contents;
        this.toUser = toUser;
    }

    public String getContents() {
        return contents;
    }

    public String getToUser() {
        return toUser;
    }
}