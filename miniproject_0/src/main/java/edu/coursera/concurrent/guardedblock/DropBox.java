package edu.coursera.concurrent.guardedblock;

import java.util.logging.Logger;

public class DropBox {
    private final Logger logger = Logger.getLogger("DropBox Logger");
    // message for:
    //  - Producer to put information to
    //  - Consumer to read information from
    private String sharedMessage;

    // Flat to tell dropBox status
    private boolean boxEmpty;

    // Default initialized instance
    //  - have empty but non-null message
    //  - have the empty status flag true
    public DropBox() {
        sharedMessage = "";
        boxEmpty = true;
        logger.info("dropbox instance initialized");
    }

    // method that producer to call
    public synchronized void put(String messageFromProducer) {
        logger.info("-put()- producer starting call put: " + messageFromProducer);

        while (!boxEmpty) {
            // when the boxEmpty is false
            // meaning the DropBox object is NOT ready for producer to put a message in
            // Thus call the wait() => get producer thread release the intrinsic lock
            // Nothing is performed in catch block, due to the fact interruption exception might be from other source => not always the source to change boxEmpty status
            logger.info("-put()- boxEmpty is false");
            try {
                logger.info("-put()- producer thread to wait");
                wait();
            } catch (InterruptedException e) {
                logger.info("-put()- producer thread get interruption exception");
            }
        }

        // when code is executed to here
        // then it means the boxEmpty is changed to true
        // plus the change is made by the Consumer
        // that consumer has taken the message so the box is ready to be used by the producer
        logger.info("-put()- boxEmpty value is now: " + String.valueOf(boxEmpty));

        // ideally the put/store message and
        // toggle status should happen at the same time
        this.sharedMessage = messageFromProducer;
        logger.info("message: " + messageFromProducer + " is done by producer");
        boxEmpty = false;
        logger.info("-put()- boxEmpty set to false");


        // now producer has done all
        // and should notify consumer thread that boxEmpty status is changed to false ==> meaning there is something for consumer to consume
        logger.info("-put()- producer thread call notifyAll()");
        notifyAll();
    }


    // method that consumer to call
    public synchronized String take() {
        logger.info("-take()- consumer starting call take");
        while (boxEmpty) {
            // when the boxEmpty is false
            // meaning the DropBox object is ready for consumer to take a message
            // Thus call the wait() => get consumer thread release the intrinsic lock (of the dropbox object ==> used for sharing and transferring message in between)
            // Nothing is performed in catch block, due to the fact interruption exception might be from other source => not always the source to change boxEmpty status
            logger.info("-take()- boxEmpty is true");
            try {
                logger.info("-take()- consumer thread to wait");
                wait();
            } catch (InterruptedException e) {
                logger.info("-take()- consumer thread get interruption exception");
            }
        }

        //when code is executed to here
        //      then it means the boxEmpty is changed to false
        //      plus the change is made by the Producer
        //          that Producer has stored the message
        //next is for Consumer to take it
        logger.info("-take()- boxEmpty is false");

        String returnedMessage = sharedMessage;
        logger.info("-take() - consumer thread got message: " + returnedMessage);
        //Toggle status to true
        //      Thus when producer thread resumed runnable state <== by notify or notifyAll
        //           then producer can properly work
        // Note the notifyAll shall be called after status updated
        boxEmpty = true;
        logger.info("-take() - consumer thread marks boxEmpty as true");
        // notify other threads
        logger.info("-take() - consumer thread notifyAll");
        notifyAll();


        // this is the last statement of consumer method call - take()
        // If this is not a synchronised method, notify first might cause issue => the returned sharedMessage might be updated already..
        // thus we make a new local variable rather than using the shared
        logger.info("-take() - consumer thread return the message");
        return returnedMessage;
    }
}