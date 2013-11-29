package com.springapp.mvc;

import com.voxeo.tropo.ActionResult;
import com.voxeo.tropo.Tropo;
import com.voxeo.tropo.TropoResult;
import com.voxeo.tropo.actions.Do;
import com.voxeo.tropo.enums.Voice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.voxeo.tropo.Key.*;
import static com.voxeo.tropo.enums.Mode.*;

@Controller
@RequestMapping("/")
public class IvrController {

//
//    @RequestMapping(value = "/voice")
//    @RequestMapping(value = "/ivr")
    public void ivr(HttpServletRequest request, HttpServletResponse response) {

        // Note that Tropo will send you a POST request so either overriding service or post is ok

        Tropo tropo = new Tropo();
        tropo.say("Welcome to our company. Please enter the number of the department you wish to be forwarded to:");
        tropo.ask(NAME("userChoice"), BARGEIN(true), MODE(DTMF), TIMEOUT(10f), ATTEMPTS(2)).and(
                Do.say(VALUE("Sorry, I didn't hear anything"), EVENT("timeout"))
                        .say("Press #1 for Customer Support. Press #2 for sales. Press #3 for emergencies. Press #4 for any other thing."),
                Do.choices(VALUE("[1 DIGIT]"))
        );
        tropo.on(EVENT("continue"), NEXT("redirect"));
        tropo.render(response);
    }


    @RequestMapping(value = "/selection")
    public void redirect(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        TropoResult result = tropo.parse(request);
        ActionResult actionResult = result.getActions().get(0);

        tropo.say(VOICE(Voice.KATE),VALUE("you have chosen "+actionResult.getValue()+"."));
        tropo.say(VOICE(Voice.KATE),VALUE("This is the end of the demo, bye bye."));
//        Integer choice = Integer.parseInt(actionResult.getValue());
//
//        switch (choice) {
//            case 1:
//                tropo.say("Thank you. We are going to redirect you now to one of our customer support agents");
//                break;
//            case 2:
//                tropo.say("Thank you. We are going to redirect you now to one of our sale agents");
//                break;
//            case 3:
//                tropo.say("Thank you. We are going to redirect you now to one of our emergencies agents");
//                break;
//            default:
//                tropo.say("Thank you. Please bear with us. One of our agents will respond your call very shortly.");
//                break;
//        }
//        tropo.say("http://ccmixter.org/content/DoKashiteru/DoKashiteru_-_you_(na-na-na-na).mp3");
        tropo.hangup();

        tropo.render(response);
    }


}
