package con.os.operando.akka.sample.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import con.os.operando.akka.sample.SortElement;

import java.util.*;

public class QuickSortActor extends UntypedAbstractActor {

    private ActorRef parent;
    private ActorRef childLeft;
    private ActorRef childRight;
    private List<Integer> childLeftList = Collections.emptyList();
    private List<Integer> childRightList = Collections.emptyList();

    @Override
    public void onReceive(Object message) throws Throwable {
        if (!(message instanceof SortElement)) {
            return;
        }

        System.out.printf("Sender %s\n", getSender());

        ActorRef actorRef = getSender();

        // sortし終わったListが子Actorから返ってくるので、左右どちらのActorなのか判定して結果を保持する
        if (Objects.equals(actorRef, childLeft)) {
            childLeftList = ((SortElement) message).getList();
        } else if (Objects.equals(actorRef, childRight)) {
            childRightList = ((SortElement) message).getList();
        } else {
            parent = actorRef;

            List<Integer> list = ((SortElement) message).getList();
            int listSize = list.size();
            ActorRef self = getSelf();

            // sortする必要がない場合
            if (listSize == 1) {
                // 戻る先は自分の親にあたるActor
                getSender().tell(message, self);
                return;
            }

            int axis = pivot(list);
            ActorContext context = getContext();

            // 自分の子にあたるアクターを生成してる
            // 2分割したリストを送る
            // 再帰をアクターで代替してる
            // 子のアクターもまたソートが終わるまで子アクターを作って処理させる
            childLeft = context.actorOf(Props.create(QuickSortActor.class));
            childLeft.tell(new SortElement(new ArrayList<>(list.subList(0, axis))), self);

            childRight = context.actorOf(Props.create(QuickSortActor.class));
            childRight.tell(new SortElement(new ArrayList<>(list.subList(axis, listSize))), self);
        }

        // 左右の子Actorから分割したリストが戻ってきているか判定
        if (!childLeftList.isEmpty() && !childRightList.isEmpty()) {
            childLeftList.addAll(childRightList);

            // Akkaでは最上位のActorの親は"akka://default/user"というアドレスを持つ
            // sortを開始したActorを判定するために利用してる
            // akka://default/userじゃなくてakka://default/deadLettersじゃないとダメだった
            if (Objects.equals(parent.path().toString(), "akka://default/deadLetters")) {
                System.out.println(Arrays.toString(childLeftList.toArray()));
            } else {
                // 親がAkkaが提供する特殊なActorでなければ、自分は分割されたリストを受け取ったということになる
                // 親Actorにsort済みのリストを送り返す
                parent.tell(new SortElement(childLeftList), getSelf());
            }
        }
    }

    private int pivot(List<Integer> list) {
        int l = 0;
        int r = list.size() - 1;
        int pivot = list.get((l + r) / 2);
        while (l <= r) {
            while (list.get(l) < pivot) {
                l++;
            }
            while (list.get(r) > pivot) {
                r--;
            }
            if (l <= r) {
                Collections.swap(list, l, r);
                l++;
                r--;
            }
        }
        return l;
    }
}
