package tippjatek.model;

@FunctionalInterface
public interface UIResultListener<T> {
    void notify(T result);
}
