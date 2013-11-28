package com.springapp.mvc;

import com.springapp.mvc.domain.Item;
import com.voxeo.tropo.*;
import com.voxeo.tropo.actions.Do;
import com.voxeo.tropo.enums.Recognizer;
import com.voxeo.tropo.enums.Voice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.voxeo.tropo.Key.*;

@Controller
@RequestMapping("/")
public class HelloController {
    static LinkedList<Item> items = new LinkedList<Item>();

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("items", items);
        return "hello";
    }

    private Item addOrUpdateItem(String uId) {
        for (Item item : items) {
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
        items.addFirst(item);
        model.addAttribute("items", items);
        return "hello";
    }

    @RequestMapping(value = "/voice")
    public void voice(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        TropoSession session = tropo.session(request);
        items.addFirst(new Item(session.getCallId(), session.getFrom().getId(), session.getTo().getId()));
        tropo.say(VOICE(Voice.SIMON), VALUE("Thanks for calling Fantastic resort. All our customer service representative are currently busy. "));
        tropo.on(EVENT("continue"), NEXT("loop"));
        tropo.render(response);
    }

    @RequestMapping(value = "/askDate")
    public void askDate(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        tropo.ask(NAME("date"), BARGEIN(true), VOICE(Voice.SIMON), TIMEOUT(10.0f), REQUIRED(true)).and(
                Do.say(VOICE(Voice.SIMON), VALUE("Please say or enter the date you want to book as four digital, for example 2311 means twenty third of November")),
                Do.on(EVENT("success"), NEXT("bookingDate")),
                Do.choices(VALUE("[4 DIGITS]")));
        tropo.render(response);
    }

    @RequestMapping(value = "/loop")
    public void loop(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        tropo.say(VOICE(Voice.SIMON), VALUE("Thanks for hold the line. Your call is important to us, we will answer your call as soon as possible.  "));
        tropo.say(VOICE(Voice.SIMON), VALUE("Please say self service or press one if you want to try our self service, otherwise please hold the line.  "));

        tropo.ask(NAME("foo"), BARGEIN(true), TIMEOUT(30.0f), INTERDIGIT_TIMEOUT(5), REQUIRED(true),
                ALLOW_SIGNALS("exit", "stopHold"), ATTEMPTS(5), MIN_CONFIDENCE(3), RECOGNIZER(Recognizer.BRITISH_ENGLISH), VOICE(Voice.SIMON)).and(
                Do.say("Please say self service or press one if you want to try our self service, otherwise please hold the line."),
                Do.on(EVENT("success"), NEXT("bookingDate")),
                Do.choices(VALUE("self service(1, self service)"))
        );
        tropo.on(EVENT("continue"), NEXT("loop"));
        tropo.render(response);
    }

    //todo
//    String formatDate(String bookingDate){
//        Map month = new HashMap();
//
//    }

    @RequestMapping(value = "/bookingDate")
    public void bookingDate(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        TropoSession session = tropo.session(request);
        Item item = addOrUpdateItem(session.getCallId());
        if (item != null) {
            TropoResult result = tropo.parse(request);
            ActionResult actionResult = result.getActions().get(0);
            item.setBookingDate(actionResult.getValue());
            tropo.say("thanks for booking with us, your booking date is " + item.getBookingDate());
        } else {
            tropo.say("Sorry, you didn't book in, please ring us to try again");
        }
        tropo.hangup();
        if (item != null) {
            item.setStatus("Offline");
        }
        tropo.render(response);
    }

    public static void main(String[] args) {
        String token = "25f5f0a9930a7142a8062d87a11ee20268e12af6a377ef3c14b8ec97ecb09b6780b16fe2103ece9d4ee7e1b3";


//        "token":"TOKEN",
//                3
//        "customerName":"John Dyer",
//                4
//        "numberToDial":"4075551212",
//                5
//        "msg":"the sky is falling."

//        tring token = "bb308b34ed83d54cab226f4af7969e4c7d7d9196cdb3210b5ef0cb345616629005bfd05efe3f4409cd496ca2";
        Tropo tropo = new Tropo();
        Map params = new HashMap();
        params.put("customerName", "John Dyer");
        params.put("numberToDial", "+61432248706");
        params.put("msg", "Just a reminder");
        params.put("network", "PSTN");

        TropoLaunchResult result = tropo.launchSession(token, params);
        System.out.println("result = " + result);


    }

}