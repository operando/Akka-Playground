package con.os.operando.akka.sample.actor;

import akka.actor.UntypedAbstractActor;

public class StoppedActor extends UntypedAbstractActor {
    private int count;

    @Override
    public void onReceive(Object o) throws Throwable {
        count++;
        System.out.println(count);
        Thread.sleep(1000);
    }
}
