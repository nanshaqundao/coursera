package demo;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class Demo {
    public static void main(String[] args) {
        ConcurrentLinkedQueue<Integer> integerConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();

        IntStream.range(0, 10000).forEach(e -> {
            System.out.println(e);
            integerConcurrentLinkedQueue.add(e);
        });
    }
}
