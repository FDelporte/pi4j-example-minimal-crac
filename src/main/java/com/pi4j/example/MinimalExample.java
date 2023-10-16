package com.pi4j.example;

public class MinimalExample {

    /**
     * This application blinks a LED and counts the number the button is pressed. The blink speed increases with each
     * button press, and after 5 presses the application finishes.
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] args) throws Exception {
        var pi4j = new Pi4JService();

        while (pi4j.getPressCount() < 5) {
            pi4j.toggleLed();
            Thread.sleep(500 / (pi4j.getPressCount() + 1));
        }

        pi4j.shutdown();
    }
}
