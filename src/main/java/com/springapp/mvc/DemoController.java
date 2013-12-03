package com.springapp.mvc;

import com.springapp.mvc.domain.UpdateDemo;
import com.springapp.mvc.domain.Item;
import com.voxeo.tropo.Tropo;
import com.voxeo.tropo.TropoSession;
import com.voxeo.tropo.actions.Do;
import com.voxeo.tropo.enums.Recognizer;
import com.voxeo.tropo.enums.Voice;
import net.sf.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static com.voxeo.tropo.Key.*;
import static com.voxeo.tropo.enums.Mode.DTMF;

@Controller
@RequestMapping("/")
public class DemoController {
    static String MESSAGE = "Roses are red, Violets are blue. What is the color of Rose?";
    static String ANSWER = "Red";
    static Voice VOICE = Voice.SIMON;
    static UpdateDemo UPDATE_DEMO = new UpdateDemo(MESSAGE, ANSWER, VOICE);
    static List<Voice> VOICES = Arrays.asList(Voice.SIMON, Voice.KATE, Voice.DAVE, Voice.ALLISON);


    @RequestMapping(method = RequestMethod.GET)
    public String demo(ModelMap model) {
        model.addAttribute("items", Repository.items);
        model.addAttribute("updateDemo", UPDATE_DEMO);
        model.addAttribute("voices", VOICES);
        return "demo";
    }
    @RequestMapping(value = "/updateDemo", method = RequestMethod.POST)
    public String updateDemo( @ModelAttribute("updateDemo")UpdateDemo updateDemo) {
        UPDATE_DEMO.setAnswer(updateDemo.getAnswer());
        UPDATE_DEMO.setMessage(updateDemo.getMessage());
        UPDATE_DEMO.setVoice(updateDemo.getVoice());
        return "redirect:/";
    }

    @RequestMapping(value = "/demo")
    public void demo(HttpServletRequest request, HttpServletResponse response) {

        Tropo tropo = new Tropo();
        try {
            TropoSession session = tropo.session(request);
            Repository.items.addFirst(new Item(session.getCallId(), session.getFrom().getId(), session.getTo().getId()));
        } catch (JSONException ignore) {
        }

        tropo.ask(NAME("userChoice"), VOICE(UPDATE_DEMO.getVoice()), BARGEIN(true), MODE(DTMF), TIMEOUT(10f), ATTEMPTS(10), RECOGNIZER(Recognizer.BRITISH_ENGLISH), MIN_CONFIDENCE(70)).and(
                Do.say(UPDATE_DEMO.getMessage()),
                Do.choices(VALUE(UPDATE_DEMO.getAnswer()))
        );
        tropo.on(EVENT("continue"), NEXT("selection"));

        tropo.render(response);

    }

}
