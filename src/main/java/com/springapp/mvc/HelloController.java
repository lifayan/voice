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

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static com.voxeo.tropo.Key.*;
import static com.voxeo.tropo.enums.Mode.DTMF;

@Controller
@RequestMapping("/")
public class HelloController {
    private static String BUSINESS_NAME = "Sensis Fantastic Resort";

    private static String DEBUG_INFO;

//    @RequestMapping(method = RequestMethod.GET)
//    public String printWelcome(ModelMap model) {
//        model.addAttribute("items", Repository.items);
//        model.addAttribute("businessName", BUSINESS_NAME);
//        return "hello";
//    }

    private Item addOrUpdateItem(String uId) {
        for (Item item : Repository.items) {
            if (uId.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    @RequestMapping("/businessName")
    public String businessName(ModelMap model, @RequestParam("businessName") String businessName) {
        BUSINESS_NAME = businessName;
        model.addAttribute("items", Repository.items);
        model.addAttribute("businessName", BUSINESS_NAME);
        return "hello";
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
        } catch (JSONException ignore) {
        }

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
            tropo.say(VOICE(Voice.SIMON), VALUE("Thanks for calling " + BUSINESS_NAME + ". All our customer service representative are busy. "));
            tropo.on(EVENT("continue"), NEXT("loop"));
            tropo.render(response);
        }
    }

//    @RequestMapping(value = "/debug")
//    public void debug(HttpServletRequest request, HttpServletResponse response) {
//        ServletInputStream in = null;
//        BufferedReader br = null;
//        StringBuilder sb = new StringBuilder();
//
//        String line;
//        try {
//            in = request.getInputStream();
//            br = new BufferedReader(new InputStreamReader(in));
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            DEBUG_INFO = sb.toString();
//
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } finally {
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//
//    }



//    @RequestMapping(value = "/showDebug")
//    public String showDebug(ModelMap model) {
//            model.addAttribute("items", Repository.items);
//            model.addAttribute("businessName", BUSINESS_NAME);
//            model.addAttribute("debug", DEBUG_INFO);
//            return "debug";
//
//    }





    @RequestMapping(value = "/loop")
    public void loop(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        tropo.say(VOICE(Voice.SIMON), VALUE("Your are the second in the queue. We will answer your call shortly. Your call is important to us, we will answer your call as soon as possible.  "));
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
    //todo
//    String formatDate(String bookingDate){
//        Map month = new HashMap();
//
//    }

//    @RequestMapping(value = "/bookDate")
//    public void bookDate(HttpServletRequest request, HttpServletResponse response) {
//        Tropo tropo = new Tropo();
//        TropoSession session = tropo.session(request);
//        Item item = addOrUpdateItem(session.getCallId());
//        if (item != null) {
//            TropoResult result = tropo.parse(request);
//            ActionResult actionResult = result.getActions().get(0);
//            String bookingDate = actionResult.getValue();
//            item.setBookingDate(bookingDate);
//            tropo.say(VOICE(Voice.SIMON),VALUE("You have been booked to "+bookingDate));
//        } else {
//            tropo.say(VOICE(Voice.SIMON), VALUE("Sorry there are something wrong with our system, please ring us to try again"));
//        }
//        tropo.hangup();
//        if (item != null) {
//            item.setStatus("Offline");
//        }
//        tropo.render(response);
//    }
//
//    @RequestMapping(value = "/confirmDate")
//    public void confirmDate(HttpServletRequest request, HttpServletResponse response) {
//        try{
//        Tropo tropo = new Tropo();
//        TropoSession session = tropo.session(request);
//        Item item = addOrUpdateItem(session.getCallId());
//        if (item != null) {
//            TropoResult result = tropo.parse(request);
//            ActionResult actionResult = result.getActions().get(0);
//            String bookingDate = actionResult.getValue();
//
//            tropo.ask(NAME("confirmBooking"), BARGEIN(true),VOICE(Voice.SIMON), MODE(DTMF), TIMEOUT(10f), ATTEMPTS(10), RECOGNIZER(Recognizer.BRITISH_ENGLISH), MIN_CONFIDENCE(70)).and(
//                    Do.say(VALUE("Sorry, I didn't hear anything."), EVENT("timeout"))
//                            .say(VALUE("Sorry I didn't get that."), EVENT("nomatch"))
//                            .say("We have booked you in on " + bookingDate + ". Please say no, one or press #1 if you want to change the date. " +
//                                    "Otherwise please hang off. we will ring you to remind you one day prior to the booking date"),
//                    Do.choices(VALUE("No(1,No)")),
//                    Do.on(EVENT("success"), NEXT("askDate"))
//            );
//            item.setBookingDate(bookingDate);
//        } else {
//            tropo.say(VOICE(Voice.SIMON), VALUE("Sorry there are something wrong with our system, please ring us to try again"));
//        }
//        tropo.hangup();
//        if (item != null) {
//            item.setStatus("Offline");
//        }
//        tropo.render(response);} catch (Exception e){
//            DEBUG_INFO = e.getMessage();
//        }
//    }

    @RequestMapping(value = "/askDate")
    public void askDate(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        tropo.ask(NAME("date"), BARGEIN(true), VOICE(Voice.SIMON), TIMEOUT(10.0f), REQUIRED(true), ATTEMPTS(10), RECOGNIZER(Recognizer.BRITISH_ENGLISH), MIN_CONFIDENCE(70)).and(
                Do.say(VALUE("Sorry, I didn't hear anything."), EVENT("timeout"))
                        .say(VALUE("Sorry I didn't get that."), EVENT("nomatch"))
                        .say("Please say or enter the date you would like to book as four digital. For example two three one one means twenty third of November."),
                Do.choices(VALUE("[4 DIGITS]")));
        tropo.on(EVENT("continue"), NEXT("selection"));
        tropo.render(response);
    }

    @RequestMapping(value = "/call")
    public void call(HttpServletResponse response) {
        Tropo tropo = new Tropo();
        tropo.call("+61477301232");
        tropo.say(VOICE(Voice.SIMON), VALUE("this is just a test for Ryan LI!"));
        tropo.render(response);
    }

//    @RequestMapping(value = "/call", method = RequestMethod.POST)
//    public String call(ModelMap model, @RequestParam("number") String number) {
//        String token = "";
//        Tropo tropo = new Tropo();
//        Map params = new HashMap();
//        params.put("numberToDial", number);
//
//        TropoLaunchResult result = tropo.launchSession(token, params);
//
//        model.addAttribute("callResult", result.getSuccess());
//        return "template";
//    }

    public static void main(String[] args) {


//        "token":"TOKEN",
//                3
//        "customerName":"John Dyer",
//                4
//        "numberToDial":"4075551212",
//                5
//        "msg":"the sky is falling."
        Tropo tropo = new Tropo();

        TropoLaunchResult result = tropo.launchSession(token, null);
        System.out.println("result = " + result.getSuccess());


    }

}