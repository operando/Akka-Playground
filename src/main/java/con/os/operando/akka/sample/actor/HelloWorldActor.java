package con.os.operando.akka.sample.actor;

import akka.actor.ActorSystem;
import akka.actor.UntypedAbstractActor;

public class HelloWorldActor extends UntypedAbstractActor {

    @Override
    public void onReceive(Object o) throws Throwable {
        System.out.println("Hello World");
        System.out.printf("Sender %s\n", getSender()); // 送信元を取得する

        // Contextで色々できるっぽい
        // 自分が所属してるActorSystemの参照
        ActorSystem actorSystem = getContext().getSystem();

        // Actorは新しいActorを生成したり、別のActorにメッセージを送ったりもできる
//        ActorRef actorRef = actorSystem.actorOf(Props.create(HelloWorldActor.class));
//        actorRef.tell("Hello", ActorRef.noSender());
    }
}
