package com.springapp.mvc;

import com.springapp.mvc.domain.Item;
import com.voxeo.tropo.*;
import com.voxeo.tropo.actions.Do;
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

    private Item addOrUpdateItem(String uId){
        for (Item item: items){
            if (uId.equals(item.getId())){
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

    @RequestMapping(value="/voice")
    public void voice(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        TropoSession session = tropo.session(request);
        items.addFirst(new Item(session.getCallId(), session.getFrom().getId(), session.getTo().getId()));
        tropo.say(VOICE(Voice.SIMON), VALUE("Thanks for calling Fantastic resort. Your call is very important to us, we will answer your call as soon as possible.  "));
        tropo.say(VOICE(Voice.SIMON),VALUE("Please press one to continue.  "));

        tropo.ask(NAME("date"), BARGEIN(true), TIMEOUT(10.0f), REQUIRED(true)).and(
                Do.say(VOICE(Voice.SIMON),VALUE("Please say or enter the date you want to book as four digital, for example 2311 means twenty third of November")),
                Do.on(EVENT("success"), NEXT("bookingDate")),
                Do.choices(VALUE("[4 DIGITS]")));
        tropo.render(response);
    }

    //todo
//    String formatDate(String bookingDate){
//        Map month = new HashMap();
//
//    }

    @RequestMapping(value="/bookingDate")
    public void bookingDate(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        TropoSession session = tropo.session(request);
        Item item = addOrUpdateItem(session.getCallId());
        if (item!=null){
            TropoResult result = tropo.parse(request);
            ActionResult actionResult = result.getActions().get(0);
            item.setBookingDate(actionResult.getValue());
            tropo.say("thanks for booking with us, your booking date is "+item.getBookingDate());
        }         else {
            tropo.say("Sorry, you didn't book in, please ring us to try again");
        }
        tropo.hangup();
        item.setStatus("Offline");
        tropo.render(response);
    }

    public static void main(String[] args) {

    }

}