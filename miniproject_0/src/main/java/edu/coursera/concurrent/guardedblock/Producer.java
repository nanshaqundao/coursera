package edu.coursera.concurrent.guardedblock;

import java.util.Random;
import java.util.logging.Logger;

public class Producer implements Runnable {

    private final Logger logger = Logger.getLogger("Producer Logger");
    private DropBox dropBox;

    public Producer(DropBox dropBox){
        this.dropBox = dropBox;
    }
    public void run() {
        logger.info("Producer thread starts to run...");
        String[] rawMessages = {
                "Today is Saturday",
                "Tomorrow is Sunday",
                "Yesterday was Friday",
                "Have a good day"
        };

        for(int i = 0; i< rawMessages.length; i++){
            logger.info("Producer thread ready to put: " + i);
            dropBox.put(rawMessages[i]);
            try{
                logger.info("Producer thread to sleep for random time");
                Thread.sleep(new Random().nextInt(5000));
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }

        dropBox.put("Done");
    }
}