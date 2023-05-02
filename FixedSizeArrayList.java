import java.util.ArrayList;

public class FixedSizeArrayList<T> extends ArrayList<T> {
    private int maxSize;

    public FixedSizeArrayList(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T element) {
        if (size() == maxSize) {
            return false;
        }
        return super.add(element);
    }
}

