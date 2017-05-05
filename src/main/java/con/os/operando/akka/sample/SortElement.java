package con.os.operando.akka.sample;

import java.io.Serializable;
import java.util.List;

public class SortElement implements Serializable {
    private final List<Integer> list;

    public SortElement(List<Integer> list) {
        this.list = list;
    }

    public List<Integer> getList() {
        return list;
    }
}