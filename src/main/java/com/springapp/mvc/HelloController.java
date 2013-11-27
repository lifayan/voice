package com.springapp.mvc;

import com.springapp.mvc.domain.Item;
import com.voxeo.tropo.Tropo;
import com.voxeo.tropo.TropoLaunchResult;
import com.voxeo.tropo.TropoSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/")
public class HelloController {
    static LinkedList<Item> items = new LinkedList<Item>();

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        model.addAttribute("items", items);
        return "hello";
    }

    @RequestMapping(value="/voice")
    public void voice(HttpServletRequest request, HttpServletResponse response) {
        Tropo tropo = new Tropo();
        TropoSession session = tropo.session(request);
        String callId = session.getCallId();
        items.addFirst(new Item(callId));
        tropo.say("Thanks for calling fantastic resort, your call is very important to us, we will answer your call as soon as possible.");
        tropo.render(response);
    }

}