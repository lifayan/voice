package com.springapp.mvc;

import com.springapp.mvc.domain.Item;
import com.voxeo.tropo.*;
import com.voxeo.tropo.actions.Do;
import com.voxeo.tropo.enums.Recognizer;
import com.voxeo.tropo.enums.Voice;
import net.sf.json.JSONException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.voxeo.tropo.Key.*;
import static com.voxeo.tropo.enums.Mode.DTMF;

@Controller
@RequestMapping("/")
public class HelloController {


    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("items", Repository.items);
        return "hello";
    }

    private Item addOrUpdateItem(String uId) {
        for (Item item : Repository.items) {
            if (uId.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    @RequestMapping("/add")
    public String add(ModelMap model) {
        Item item = new Item("id", "from", "to");
        item.setBookingDate("2311");
        Repository.items.addFirst(item);
        model.addAttribute("items", Repository.items);
        return "hello";
    }

    @RequestMapping(value = "/voice")
    public void voice(HttpServletRequest request, HttpServletResponse response) {

        Tropo tropo = new Tropo();
        try {
            TropoSession session = tropo.session(request);
            Repository.items.addFirst(new Item(session.getCallId(), session.getFrom().getId(), session.getTo().getId()));
        } catch (JSONException ignore) {        }

        if (StringUtils.isNotEmpty(Repository.template)) {
            try {
                if (Repository.template != null) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    response.getWriter().write(Repository.template);
                    response.getWriter().flush();
                    response.getWriter().close();
                }
            } catch (IOException ioe) {
                throw new RuntimeException("An error happened while rendering response", ioe);
            }
        } else {
            tropo.say(VOICE(Voice.SIMON), VALUE("Thanks for calling Sensis Fantastic resort. All our customer service representative are currently busy. "));
            tropo.on(EVENT("continue"), NEXT("loop"));
            tropo.render(response);
        }
    }



    @RequestMapping(value = "/loop")
    public void loop(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        tropo.say(VOICE(Voice.SIMON), VALUE("Your were put in the queue. We will answer your call shortly. Your call is important to us, we will answer your call as soon as possible.  "));
        tropo.ask(NAME("userChoice"), VOICE(Voice.SIMON), BARGEIN(true), MODE(DTMF), TIMEOUT(10f), ATTEMPTS(10), RECOGNIZER(Recognizer.BRITISH_ENGLISH), MIN_CONFIDENCE(70)).and(
                Do.say(VALUE("Sorry, I didn't hear anything."), EVENT("timeout"))
                        .say(VALUE("Sorry I didn't get that."), EVENT("nomatch"))
                        .say("Please say booking, one or press #1 if you want to book use self service. Otherwise please hold the line, we will answer your call as soon as possible."),
                Do.choices(VALUE("booking(1, booking), one(1, one)"))
        );
        tropo.on(EVENT("continue"), NEXT("askDate"));
        tropo.on(EVENT("incomplete"), NEXT("loop"));
        tropo.render(response);
    }
//    @RequestMapping(value = "/loop")
//    public void loop(HttpServletRequest request, HttpServletResponse response) {
//        Tropo tropo = new Tropo();
//        tropo.say(VOICE(Voice.SIMON), VALUE("Thanks for hold the line. Your call is important to us, we will answer your call as soon as possible.  "));
//        tropo.ask(NAME("foo"), BARGEIN(true), TIMEOUT(30.0f), INTERDIGIT_TIMEOUT(5), REQUIRED(true),
//                ALLOW_SIGNALS("exit", "stopHold"), ATTEMPTS(5), MIN_CONFIDENCE(3), RECOGNIZER(Recognizer.BRITISH_ENGLISH), VOICE(Voice.SIMON)).and(
//                Do.say("Please say self service or press one if you want to try our self service, otherwise please hold the line."),
//                Do.on(EVENT("success"), NEXT("bookingDate")),
//                Do.choices(VALUE("self service(1, self service)"))
//        );
//        tropo.on(EVENT("continue"), NEXT("loop"));
//        tropo.render(response);
//    }

    //todo
//    String formatDate(String bookingDate){
//        Map month = new HashMap();
//
//    }

    @RequestMapping(value = "/confirmDate")
    public void bookingDate(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        TropoSession session = tropo.session(request);
        Item item = addOrUpdateItem(session.getCallId());
        if (item != null) {
            TropoResult result = tropo.parse(request);
            ActionResult actionResult = result.getActions().get(0);
            String bookingDate = actionResult.getValue();

            tropo.ask(NAME("confirmBooking"), BARGEIN(true),VOICE(Voice.SIMON), MODE(DTMF), TIMEOUT(10f), ATTEMPTS(10), RECOGNIZER(Recognizer.BRITISH_ENGLISH), MIN_CONFIDENCE(70)).and(
                    Do.say(VALUE("Sorry, I didn't hear anything."), EVENT("timeout"))
                            .say(VALUE("Sorry I didn't get that."), EVENT("nomatch"))
                            .say("We have booked you in on "+bookingDate+". Please say no, one or press #1 if you want to change the date. " +
                                    "Otherwise please hang off. we will ring you to remind you one day prior to the booking date"),
                    Do.choices(VALUE("No(1,No)"))
            );
            item.setBookingDate(bookingDate);
            tropo.on(EVENT("continue"), NEXT("askDate"));

        } else {
            tropo.say(VOICE(Voice.SIMON), VALUE("Sorry there are something wrong with our system, please ring us to try again"));
        }
        tropo.hangup();
        if (item != null) {
            item.setStatus("Offline");
        }
        tropo.render(response);
    }

    @RequestMapping(value = "/askDate")
    public void askDate(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        tropo.ask(NAME("date"), BARGEIN(true), VOICE(Voice.SIMON), TIMEOUT(10.0f), REQUIRED(true),ATTEMPTS(10), RECOGNIZER(Recognizer.BRITISH_ENGLISH), MIN_CONFIDENCE(70)).and(
                Do.say(VALUE("Please say or enter the date you want to book as four digital. For example 2311 means twenty third of November.")),
                Do.on(EVENT("success"), NEXT("confirmDate")),
                Do.choices(VALUE("[4 DIGITS]")));
        tropo.render(response);
    }

    @RequestMapping(value = "/call", method = RequestMethod.POST)
    public String call(ModelMap model, @RequestParam("number") String number) {
        String token = "";
        Tropo tropo = new Tropo();
        Map params = new HashMap();
        params.put("customerName", "Ryan");
        params.put("numberToDial", "+61432248706");
        params.put("say", "Hello this is a automatic call from Sensis Fantastic resort, just to remind that you have a booking with us.");
        params.put("network", "PSTN");

        TropoLaunchResult result = tropo.launchSession(token, params);

        model.addAttribute("callResult", result.getSuccess());
        return "template";
    }

    public static void main(String[] args) {



//        "token":"TOKEN",
//                3
//        "customerName":"John Dyer",
//                4
//        "numberToDial":"4075551212",
//                5
//        "msg":"the sky is falling."


        Tropo tropo = new Tropo();
        Map params = new HashMap();
        params.put("customerName", "Ryan LI");
        params.put("numberToDial", "+61393957901");
        params.put("say", "Just a reminder");
        params.put("network", "PSTN");
        tropo.call("+61393957901");

//        TropoLaunchResult result = tropo.launchSession(token, params);
//        System.out.println("result = " + result.getSuccess());


    }

}