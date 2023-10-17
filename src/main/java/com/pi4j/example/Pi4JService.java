package com.pi4j.example;

import com.pi4j.Pi4J;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.util.Console;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;

public class Pi4JService implements Resource {

    private static final int PIN_BUTTON = 24; // PIN 18 = BCM 24
    private static final int PIN_LED = 22; // PIN 15 = BCM 22

    private int pressCount = 0;
    private boolean shuttingDown = false;
    private Console console;
    private com.pi4j.context.Context pi4j;
    private final DigitalOutput led;
    private final DigitalInput button;

    public Pi4JService() {
        Core.getGlobalContext().register(this);

        initPi4j();

        var ledConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("led")
                .name("LED Flasher")
                .address(PIN_LED)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .provider("pigpio-digital-output");
        led = pi4j.create(ledConfig);

        var buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button")
                .name("Press button")
                .address(PIN_BUTTON)
                .pull(PullResistance.PULL_DOWN)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        button = pi4j.create(buttonConfig);
        button.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                pressCount++;
                console.println("Button was pressed for the " + pressCount + "th time");
            }
        });
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        console.println("CRaC: beforeCheckpoint");
        shuttingDown = true;
        shutdown();
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        console.println("CRaC: afterRestore");
        initPi4j();
        shuttingDown = false;
    }

    private void initPi4j() {
        console = new Console();
        console.title("<-- The Pi4J Project -->", "Initializing Pi4J");

        pi4j = Pi4J.newAutoContext();

        PrintInfo.printLoadedPlatforms(console, pi4j);
        PrintInfo.printDefaultPlatform(console, pi4j);
        PrintInfo.printProviders(console, pi4j);

        PrintInfo.printRegistry(console, pi4j);
    }

    public int getPressCount() {
        return pressCount;
    }

    public void toggleLed() {
        if (shuttingDown) {
            console.println("Can't change LED, shutting down...");
        }
        if (led.state().equals(DigitalState.HIGH)) {
            console.println("Setting LED high");
            led.high();
        } else {
            console.println("Setting LED low");
            led.low();
        }
    }

    public void shutdown() throws InterruptedException {
        console.println("Shutting Pi4J down");
        pi4j.shutdown();
        Thread.sleep(5000);
    }
}
