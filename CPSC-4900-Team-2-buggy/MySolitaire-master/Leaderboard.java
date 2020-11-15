import javax.swing.*;
import java.io.*;
import java.util.*;

public class Leaderboard {
    /**This class loads the leaderboard data, writes data to persistent storage
     * and is responsible for painting the leaderboard data into
     * a frame when the user selects the "Show leaderboard" button.
     */
    public static int gameScore = Solitaire.getScore();
    private static String scoresTXT = "MySolitaire-master\\leaderboards.txt";
    public static List<String> scores = new ArrayList<>();

    /** No input method to read information from text file containing scores into an ArrayList
     * @return ArrayList of scores found in file
     *
     */
    public static List<String> initializeBoards(){
        try {
            BufferedReader read = new BufferedReader(new FileReader(scoresTXT));
            String line = read.readLine();
            if (line != null){
                String[] temp = line.split(", ");
                scores = new ArrayList<>(Arrays.asList(temp));
            }
            read.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return scores;
    }

    /**No return method to update ArrayList that contains leaderboard scores
     */
    public static void addScore(){
        boolean scoreSpot = false;
        int x = 0;
        while (scoreSpot == false && scores.size() > x ){
            if (gameScore <= Integer.parseInt(scores.get(x))){
                scoreSpot = true;
            }
            x++;
        }
        scores.add(x, String.valueOf(gameScore));
    }

    /**No return method to write current leaderboard held in ArrayList to persistent TXT file storage
     *
     *
     */
    public static void updateFile() {
        try {
            FileWriter write = new FileWriter(scoresTXT);
            String updateScores = scores.toString();
            updateScores = updateScores.replace("[", "");
            updateScores = updateScores.replace("]", "");
            write.write(updateScores);
            write.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}
