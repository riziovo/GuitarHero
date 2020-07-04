package guitarsynth;

public interface BoundedQueue<T> extends Iterable<T> {
    int capacity();
    int size();
    void add(T x);
    T remove();
    T get();
    default boolean isEmpty()
    {
        return this.size() < 1;
    }
    default boolean isFull()
    {
        return this.size() == this.capacity();
    }
}
