package de.flojc.fastlane.loading;

/**
 * The LoadingIndicator class provides a simple loading animation that can be started and stopped.
 */
public class LoadingIndicator implements Runnable {
    private volatile boolean running;

    /**
     * The run() function displays a rotating animation of characters to indicate that a process is currently running.
     */
    @Override
    public void run() {
        running = true;
        String animationChars = "|/-\\";
        int i = 0;
        while (running) {
            System.out.print("\r Processing " + animationChars.charAt(i++ % animationChars.length()));
            try {
                Thread.sleep(650);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The "stop" function prints a message indicating whether the processing was successful or not and sets the "running"
     * variable to false.
     *
     * @param successful A boolean value indicating whether the processing was successful or not.
     */
    public void stop(boolean successful) {
        String output = successful ? "\r Processing done!\n" : "\r Processing failed!\n";
        System.out.print(output);
        this.running = false;
    }
}