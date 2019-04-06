import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Move {

    private String moveId;
    private String gameId;
    private String teamId;
    private String move;
    private String symbol;
    private String moveX;
    private String moveY;

    public String getMoveX(){
        return moveX;
    }

    public String getTeamId(){
        return teamId;
    }

    public String getMoveY(){
        return moveY;
    }
}
