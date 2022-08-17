package main;

import main.model.Doing;
import main.model.DoingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
public class DefaultController {

    private final DoingRepository doingRepository;

    @Value("${someParameter.value}")
    private Integer someParameter;

    public DefaultController(DoingRepository doingRepository) {
        this.doingRepository = doingRepository;
    }

    @RequestMapping("/")
    public String index(Model model) {
        Iterable<Doing> doingIterable = doingRepository.findAll();
        ArrayList<Doing> doings = new ArrayList<>();

        for (Doing doing : doingIterable) {
            doings.add(doing);
        }

        model.addAttribute("doings", doings)
                .addAttribute("doingsCount", doings.size())
                .addAttribute("someParameter", someParameter);

        return "index";
    }
}
