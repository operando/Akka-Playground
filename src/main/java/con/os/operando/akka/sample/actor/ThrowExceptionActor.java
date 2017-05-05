package con.os.operando.akka.sample.actor;

import akka.actor.UntypedAbstractActor;

import java.util.Optional;

public class ThrowExceptionActor extends UntypedAbstractActor {

    public ThrowExceptionActor() {
        System.out.println("new ThrowExceptionActor " + this);
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        throw new Exception();
    }

    @Override
    public void preStart() throws Exception {
        // actorOfで生成された時に実行される
        super.preStart();
        System.out.println("preStart " + this);
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        // SupervisorStrategyで再起動する前に実行される
        // 例外を送出したActor上で実行される
        super.preRestart(reason, message);
        System.out.println("preRestart " + this);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        // SupervisorStrategyで再起動する後に実行される
        super.postRestart(reason);
        System.out.println("postRestart " + this);
    }

    @Override
    public void postStop() throws Exception {
        // Actorが停止した時に実行される
        super.postStop();
        System.out.println("postStop " + this);
    }
}