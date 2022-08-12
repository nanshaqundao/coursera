package edu.coursera.concurrent;

public class Demo {
    public static void main(String[] args) {
        SieveActor app = new SieveActor();
        System.out.println(app.countPrimes(50));
    }
}
