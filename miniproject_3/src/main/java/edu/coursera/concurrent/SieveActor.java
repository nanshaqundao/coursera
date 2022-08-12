package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 * <p>
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {

    /**
     * {@inheritDoc}
     * <p>
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        if (limit <= 1) return 0;
        if (limit == 2) return 1;

        SieveActorActor actor = new SieveActorActor(2);
        finish(() -> {
            for (int i = 3; i <= limit; i += 2) {
                actor.send(i);
            }
            actor.send(0);
        });

        int countOfActors = 1;
        SieveActorActor countPointer = actor.getNext();
        while (countPointer != null) {
            countOfActors = countOfActors + 1;
            countPointer = countPointer.getNext();
        }
        return countOfActors;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {

        private Integer actorPrime;
        private SieveActorActor next;

        public SieveActorActor(Integer actorPrime) {
            System.out.println("Actor " + actorPrime + " created");
            this.actorPrime = actorPrime;
            next = null;
        }

        public SieveActorActor getNext() {
            return next;
        }

        /**
         * Process a single message sent to this actor.
         * <p>
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            final Integer candidate = (Integer) msg;
            // for any non-positive input, either return or ask the next actor to process
            if (candidate <= 0) {
                if (next != null) {
                    next.send(msg);
                } else {
                    return;
                }
            }
            // we only need to check if the current value process
            boolean isCandidatePrime = isLocalPrime(candidate);
            if (isCandidatePrime) {
                System.out.println("Actor " + actorPrime + " ----- processing candidate: " + candidate);

                if (next == null) {
                    next = new SieveActorActor(candidate);
//                    System.out.println("prime number: " + candidate);
                } else {
                    next.send(candidate);
                }
            }


        }

        private boolean isLocalPrime(Integer candidate) {
            return candidate % actorPrime != 0;
        }
    }
}
