package edu.coursera.concurrent.guardedblock;

public class Main {
    public static void main(String[] args) {
        DropBox dropBox = new DropBox();
        Producer producer = new Producer(dropBox);
        Consumer consumer = new Consumer(dropBox);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();
    }
}
