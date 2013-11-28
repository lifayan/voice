package com.springapp.mvc;

import com.springapp.mvc.domain.Item;
import com.voxeo.tropo.Tropo;
import com.voxeo.tropo.TropoException;
import com.voxeo.tropo.TropoSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class DemoController {
    static String template;

    @RequestMapping(value = "/template", method = RequestMethod.POST)
    public String updateTemplate(ModelMap model, @RequestParam("template") String str) {
        template = str;
        System.out.println("template = " + template);
        model.addAttribute("template", template);
        return "template";
    }

    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public String showTemplate(ModelMap model) {
        model.addAttribute("template", template);
        return "template";
    }

    @RequestMapping(value = "/voice")
    public void voice(HttpServletRequest request, HttpServletResponse response) {

        Tropo tropo = new Tropo();
        try{
        TropoSession session = tropo.session(request);
            HelloController.items.addFirst(new Item(session.getCallId(), session.getFrom().getId(), session.getTo().getId()));
        } catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
        }

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(template);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException ioe) {
            throw new RuntimeException("An error happened while rendering response", ioe);
        }

    }

}

