package con.os.operando.akka.chat.actor;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import con.os.operando.akka.chat.Join;
import con.os.operando.akka.chat.Leave;
import con.os.operando.akka.chat.Mention;
import con.os.operando.akka.chat.Talk;

import java.util.Objects;

public class MemberActor extends UntypedAbstractActor {

    private final ActorSelection server;

    public static Props props(ActorSelection server) {
        return Props.create(MemberActor.class, server);
    }

    public MemberActor(ActorSelection server) {
        this.server = server;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Join) {
            server.tell(message, getSelf());
        } else if (message instanceof Leave) {
            server.tell(message, getSelf());
        } else if (message instanceof Talk) {
            if (Objects.equals(getSender(), getSelf())) {
                server.tell(message, getSelf());
            }
            System.out.println(
                    String.format("%s %s",
                            getSender().path().name(),
                            ((Talk) message).getContents()));
        } else if (message instanceof Mention) {
            if (Objects.equals(getSender(), getSelf())) {
                server.tell(message, getSelf());
            }
            System.out.println(
                    String.format("%s %s",
                            getSender().path().name(),
                            ((Mention) message).getContents()));
        }
    }
}