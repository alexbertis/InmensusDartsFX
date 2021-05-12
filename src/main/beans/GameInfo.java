package main.beans;


import main.utils.Constants;

public class GameInfo {

    private int numPlayers;
    private String game;
    private int rounds;
    private String gameType;
    private String gameMode;

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isSingleInSingleOutType(){
        return (null != gameType && gameType.equals(Constants.X01_SINGLEINSINGLEOUT));
    }
    public boolean isDoubleInDoubleOutType(){
        return (null != gameType && gameType.equals(Constants.X01_DOUBLEINDOUBLEOUT));
    }

    public GameInfo(int numPlayers, String game, int rounds, String gameType, String gameMode){
        this.numPlayers = numPlayers;
        this.game = game;
        this.rounds = rounds;
        this.gameType = gameType;
        this.gameMode = gameMode;
    }
}
