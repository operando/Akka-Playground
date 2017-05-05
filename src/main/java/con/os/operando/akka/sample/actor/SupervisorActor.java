package con.os.operando.akka.sample.actor;

import akka.actor.*;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

// SupervisorStrategy
// 親にあたるActorが子Actorが例外を送出した時に子Actorをどのように扱うかを決める
// 扱い方は以下
// restart - 新しいActorインスタントを生成して再起動する
// resume - インスタントはそのままでメッセージの処理を諦める
// stop - Actorを停止する。停止したActorはそれ以上メッセージを受け付けることができない
// escalate - さらに上位のActorのSupervisorStrategyに委ねる
public class SupervisorActor extends UntypedAbstractActor {

    private final ActorRef exceptionActor;

    public SupervisorActor() {
        exceptionActor = getContext().actorOf(Props.create(ThrowExceptionActor.class));
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        exceptionActor.tell("gamugamu", getSender());
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    // OneForOneStrategy - 例外を送出した子Actorのみにコールバックを適用する

    // AllForOneStrategy - 親Actorに対して複数の子Actorがぶら下がってる場合にどの子Actorが例外を送出した場合にも全ての子Actorに対してコールバックを適用する
    // 要は１つの子Actorで例外が起きたら他の子にも同じ処理を適用するって感じかな
    private static SupervisorStrategy strategy =
            new OneForOneStrategy(10, // 一定時間内に何度まで救うかの回数
                    Duration.create(1, TimeUnit.SECONDS), // 一定時間を表す数値
                    new Function<Throwable, SupervisorStrategy.Directive>() {
                        @Override
                        public SupervisorStrategy.Directive apply(Throwable throwable) throws Exception {
                            // 子Actorが例外を送出した時に呼ばれる
                            if (throwable instanceof Exception) {
                                System.out.println("諦めんなよ！！");
                                return SupervisorStrategy.restart();
                            }
                            return SupervisorStrategy.stop();
                        }
                    });
}