package com.springapp.mvc;

import com.springapp.mvc.domain.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/")
public class HelloController {
    static LinkedList<Item>  items = new LinkedList<Item>();
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {

		model.addAttribute("message", "Hello world!");
        items.addFirst(new Item("fakeItem"));
        model.addAttribute("items", items);
		return "hello";
	}
}