package edu.coursera.concurrent.guardedblock;

import java.util.Random;
import java.util.logging.Logger;

public class Consumer implements Runnable {
    private final Logger logger = Logger.getLogger("Consumer Logger");
    private DropBox dropBox;

    public Consumer(DropBox dropBox){
        this.dropBox = dropBox;
    }

    public void run() {
        logger.info("Consumer thread starts to run...");
        for(String message = dropBox.take();!message.equals("Done");message = dropBox.take()){
            logger.info("Message received: " + message);
            try{
                logger.info("consumer sleeps random time");
                Thread.sleep(new Random().nextInt(5000));
            }catch (InterruptedException e){
                logger.info("consumer thread interruption");
            }
        }
    }
}