package con.os.operando.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import con.os.operando.akka.sample.actor.HelloWorldActor;
import con.os.operando.akka.sample.actor.QuickSortActor;
import con.os.operando.akka.sample.actor.StoppedActor;
import con.os.operando.akka.sample.actor.SupervisorActor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String... s) {
        ActorSystem actorSystem = ActorSystem.apply();

        // ActorRef = アクターへの参照?
        ActorRef actorRef = actorSystem.actorOf(Props.create(HelloWorldActor.class));

        // messageを送信する
        // 第二引数は送信元を意味する.受信したアクターからそれを使って返信するときとかに使うらしい
        // ActorRef.noSenderはそのままで送信元がないことを意味する
        actorRef.tell("Hello", ActorRef.noSender());

        List<Integer> list = randomIntegerList(100);
        System.out.println(list);

        ActorRef actorRef2 = actorSystem.actorOf(Props.create(QuickSortActor.class));
//        actorRef2.tell(new SortElement(list), ActorRef.noSender());
//
//        try {
//            while (true) {
//                Thread.sleep(1000);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // askを使えば同期的に処理することもできる
        // askを使う場合Actorが受信したメッセージに対して必ず返信を返すような実装にしないとダメ
//        Future<Object> f = Patterns.ask(actorRef2, new SortElement(list), 1000);
//
//        try {
//            Object result = Await.result(f, Duration.create(1000, TimeUnit.MILLISECONDS));
//            if (result instanceof SortElement) {
//                System.out.println(Arrays.toString(((SortElement) result).getList().toArray()));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        actorSystem.actorOf(Props.create(SupervisorActor.class))
                .tell("", ActorRef.noSender());

        ActorRef stoppedActor = actorSystem.actorOf(Props.create(StoppedActor.class));

        // Actorがメッセージ処理中に次のメッセージをつけとるとActor側のキューに詰まれる
        IntStream.range(0, 10)
                .forEach(value -> stoppedActor.tell("test", ActorRef.noSender()));

        // ActorSystem#stopで停止された場合、今処理してるメッセージは処理し、その後のメッセージじゃすべて無視される
        actorSystem.stop(stoppedActor);
    }

    private static List<Integer> randomIntegerList(int size) {
        return IntStream.range(0, size)
                .mapToObj(operand -> (int) (Math.random() * size))
                .collect(Collectors.toList());
    }
}