
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Jsontrans {
    private List<Move> moves;
    private String code;

    public List<Move> getMoves(){
        return  moves;
    }
    public String getCode(){
        return  code;
    }
}
