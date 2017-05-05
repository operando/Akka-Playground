package con.os.operando.akka.chat;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import con.os.operando.akka.chat.actor.MemberActor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

    public static void main(String... s) {
        String userName;
        if (s.length != 0) {
            userName = s[0];
        } else {
            userName = "GuestUser" + (int) Math.random() * 10;
        }

        Config config = ConfigFactory.load("client.conf");
        ActorSystem actorSystem = ActorSystem.apply("ChatClient", config);

        ActorSelection server = actorSystem.actorSelection("akka.tcp://ChatServer@127.0.0.1:2552/user/server");
        ActorRef client = actorSystem.actorOf(MemberActor.props(server), userName);

        client.tell(new Join(), client);
        client.tell(new Talk("Hello"), client);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = br.readLine()) != null) {
                client.tell(new Talk(line), client);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
