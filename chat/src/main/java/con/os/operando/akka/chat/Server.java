package con.os.operando.akka.chat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import con.os.operando.akka.chat.actor.ChatServerActor;

public class Server {

    public static void main(String... s) {
        Config config = ConfigFactory.load("server.conf");
        ActorSystem actorSystem = ActorSystem.apply("ChatServer", config);
        ActorRef serverActor = actorSystem.actorOf(ChatServerActor.props(), "server");

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}