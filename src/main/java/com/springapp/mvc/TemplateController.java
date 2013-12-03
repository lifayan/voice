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
public class TemplateController {


    @RequestMapping(value = "/template", method = RequestMethod.POST)
    public String updateTemplate(ModelMap model, @RequestParam("template") String str) {
        Repository.template = str;
        System.out.println("template = " + Repository.template);
        model.addAttribute("template", Repository.template);
        return "template";
    }

    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public String showTemplate(ModelMap model) {
        model.addAttribute("template", Repository.template);
        return "template";
    }


}

