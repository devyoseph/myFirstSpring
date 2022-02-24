

@Controller
public class HomeController{

    @GetMappint("/")
    public String home(){
        return "home"
    }
}