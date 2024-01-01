package SimpleBoard.sb.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExampleController {
    @GetMapping("/thymeleaf/test")
    public String example(Model model) {
        Person person = new Person();
        person.setId(1L);
        person.setName("test");
        person.setAge(22);
        person.setHobbies(List.of("1", "2"));
        model.addAttribute("testPerson", person);
        model.addAttribute("today", LocalDate.now());

        return "example";
    }

}

@Getter
@Setter
class Person {
    private long id;
    private String name;
    private int age;
    private List<String> hobbies;
}
