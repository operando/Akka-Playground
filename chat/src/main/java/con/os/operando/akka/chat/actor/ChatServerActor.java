package con.os.operando.akka.chat.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import con.os.operando.akka.chat.Join;
import con.os.operando.akka.chat.Leave;
import con.os.operando.akka.chat.Mention;
import con.os.operando.akka.chat.Talk;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChatServerActor extends UntypedAbstractActor {

    private Set<ActorRef> participants = new HashSet<>();

    public static Props props() {
        return Props.create(ChatServerActor.class);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        System.out.println(getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Join) {
            // 入ってきた人を追加して管理する
            participants.add(getSender());
        } else if (message instanceof Talk) {
            // broadcast message to all participants
            participants.parallelStream()
                    .filter(p -> Objects.equals(p, getSender())) // メッセージを送ってきたActorには送らなくていいのでfilterする
                    .forEach(p -> p.tell(message, getSender()));
        } else if (message instanceof Leave) {
            participants.remove(getSender());
        } else if (message instanceof Mention) {
            final String toUser = ((Mention) message).getToUser();
            participants.parallelStream()
                    .filter(p -> Objects.equals(p.path().name(), toUser))
                    .forEach(p -> p.tell(message, getSender()));
        }
    }
}