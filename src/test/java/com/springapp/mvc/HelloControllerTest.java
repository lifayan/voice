package com.springapp.mvc;

import com.voxeo.tropo.Tropo;
import com.voxeo.tropo.actions.Do;
import com.voxeo.tropo.enums.Recognizer;
import com.voxeo.tropo.enums.Voice;
import org.junit.Test;

import static com.voxeo.tropo.Key.*;

public class HelloControllerTest {
    @Test
    public void shouldBuildJsonForAsk(){
        Tropo tropo = new Tropo();
        tropo.ask(NAME("foo"), BARGEIN(true), TIMEOUT(30.0f), INTERDIGIT_TIMEOUT(5), REQUIRED(true),
                ATTEMPTS(3), MIN_CONFIDENCE(3), RECOGNIZER(Recognizer.BRITISH_ENGLISH), VOICE(Voice.SIMON)).and(
                Do.say("Please say self service or press one if you want to try our self service, otherwise please hold the line."),
                Do.on(EVENT("success"), NEXT("bookingDate")),
                Do.choices(VALUE("self service(1, self service)"))
        );

        System.out.println("tropo.text() = " + tropo.text());
    }
}
