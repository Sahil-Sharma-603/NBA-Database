import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class basketball{

    public static void main(String[] args) {
        Properties prop = new Properties();
        String fileName = "auth.cfg";
        try {
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find config file.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Error reading config file.");
            System.exit(1);
        }
    
        String username = (prop.getProperty("username"));
        String password = (prop.getProperty("password"));
    
        if (username == null || password == null){
            System.out.println("Username or password not provided.");
            System.exit(1);
        }
        Database db = new Database(username, password);
        runConsole(db);

        System.out.println("Exiting Basketball Database..Have a wonderful day :)");
    
    }

    private static void runConsole(Database db) {
        Scanner data = new Scanner(System.in);
        System.out.println("Welcome to Basketball Database");
        System.out.println("Type h for help");
        System.out.println("db > ");
        String value = data.nextLine();

        while (value != null && !value.equals("q")) {
            String[] arg = value.split("\\s+");
            String value2;
            if (value.indexOf(" ") > 0) {
                value2 = value.substring(value.indexOf(" ")).trim();
            } else {
                value2 = "";
            }
            
            try {
                if (arg[0].equals("h")) {
                    printHelp();
                }else if(arg[0].equals("games")){
                        db.games();
                }else if(arg[0].equals("official")){
                    db.officials();
                } else if(arg[0].equals("player")){
                    db.playerinfo();
                } else if(arg[0].equals("teams")){
                    db.teaminfo();
                }
                else if (arg[0].equals("p")) {
                    if (!value2.isEmpty()) {
                        db.homeTeamScores(value2);
                    } else {
                        System.out.println("Requires scores for this command");
                    }
                } else if (arg[0].equals("t")) {
                    if (!value2.isEmpty()) {
                        db.playersDraftedByTeam(value2);
                    } else {
                        System.out.println("Requires team name for this command");
                    }
                } else if (arg[0].equals("te")) {
                    if (!value2.isEmpty()) {
                        db.teamGamesBySeason(value2);
                    } else {
                        System.out.println("Requires season ID for this command");
                    }
                } else if (arg[0].equals("o")) {
                    if (!value2.isEmpty()) {
                        db.organizeTeamsByAvgPoints(value2);
                    } else {
                        System.out.println("Requires season ID for this command");
                    }
                } else if (arg[0].equals("d")) {
                    if (!value2.isEmpty()) {
                        db.draftCombineStats(value2);
                    } else {
                        System.out.println("Requires team ID for this command");
                    }
                } else if (arg[0].equals("f")) {
                    if (!value2.isEmpty()) {
                        db.foulsByQuarter(value2);
                    } else {
                        System.out.println("Requires game ID for this command");
                    }
                } else if (arg[0].equals("g")) {
                    if (!value2.isEmpty()) {
                        db.gamesByDate(value2);
                    } else {
                        System.out.println("Requires date for this command");
                    }
                } else if (arg[0].equals("pl")) {
                    if (!value2.isEmpty()) {
                        db.playerStats(value2);
                    } else {
                        System.out.println("Requires player name for this command");
                    }
                } else if (arg[0].equals("of")) {
                    if (!value2.isEmpty()) {
                        db.officialGamesCount(value2);
                    } else {
                        System.out.println("Requires official ID for this command");
                    }
                } else if (arg[0].equals("s")) {
                    if (!value2.isEmpty()) {
                        db.seasonsByTeam(value2);
                    } else {
                        System.out.println("Requires team name for this command");
                    }
                } else if (arg[0].equals("l")) {
                    if (!value2.isEmpty()) {
                        db.teamPointStats(value2);
                    } else {
                        System.out.println("Requires game ID for this command");
                    }
                } else if (arg[0].equals("dr")) {
                    String[] params = value2.split(" ");
                    if (params.length > 1) {
                        String season = params[params.length - 1];  // The last part is the season
                        String teamName = String.join(" ", java.util.Arrays.copyOf(params, params.length - 1));  // Join all parts except the last as the team name
                        
                        // Check if teamName is not empty and valid before calling the method
                        if (!teamName.trim().isEmpty() && !season.trim().isEmpty()) {
                            db.draftByYearAndTeam(teamName, season);
                        } else {
                            System.out.println("Invalid team name or season.");
                        }
                    } else {
                        System.out.println("Requires team name and season for this command.");
                    }
                }   else {
                    System.out.println("Invalid command. Type h for help.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("db > ");
            value = data.nextLine();
        }

        data.close();
    }

    //end of method

    private static void printHelp(){
        System.out.println("NBA Basketball Database Console");
        System.out.println("Commands");
        System.out.println("h - Get help");
        System.out.println("games - Displaying all game records with game ID, date, home win status, and points for both teams");
        System.out.println("official - Displaying information for officials including their official id, name, jersey number");
        System.out.println("player - Displaying information for players including their player id and name");
        System.out.println("teams - Displaying information for teams including their team id, name and abbreviation");
        System.out.println("p scores - Games where home team scored more than given scores");
        System.out.println("t team_name - List players drafted by a team");
        System.out.println("te seasonId - Teams & game count for a season, ordered by active status - For instance: Season Id is 2008");
        System.out.println("o seasonId - Teams ranked by average points per game in a season year - For instance: Season Id is 2008");
        System.out.println("d teamId - Draft combine stats for players drafted by a team");
        System.out.println("f gameId - Count of fouls in each quarter of a game");
        System.out.println("g date - Games played on a specific date");
        System.out.println("pl player_name - Games played and stats for a player");
        System.out.println("of officialId - Count of games an official reffed for all teams");
        System.out.println("s team_name - Different seasons a team has played in");
        System.out.println("l gameId - Team's point stats, opponent, and date, sorted by most recent");
        System.out.println("dr team_name season - Players drafted by a team in a given season");
        
    }
} // end of class

class Database{

    private Connection connection;

    public Database(String username, String password){
        try {
            String connectionUrl =
            "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
            + "database=cs3380;"
            + "user=" + username + ";"
            + "password="+ password +";"
            + "encrypt=false;"
            + "trustServerCertificate=false;"
            + "loginTimeout=30;";
             connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void games(){
        try {
        
            String sql = "SELECT game_id, date, win_home, points_home, points_away FROM games";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Loop through the result set and print all the information
            while(rs.next()){
                System.out.println("Game ID: " + rs.getInt("game_id") + 
                                   ", Date: " + rs.getDate("date") +
                                   ", Home Win: " + rs.getBoolean("win_home") +
                                   ", Home Points: " + rs.getInt("points_home") +
                                   ", Away Points: " + rs.getInt("points_away"));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void officials(){
        try {
        
            String sql = "SELECT official_id, first_name, last_name, jersey_number FROM officials";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Loop through the result set and print all the information
            while(rs.next()){
                System.out.println("Offical ID: " + rs.getInt("official_id") + 
                                   ", First Name: " + rs.getString("first_name") +
                                   ", Last Name: " + rs.getString("last_name") +
                                   ", Jersey Number: " + rs.getInt("jersey_number"));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void playerinfo(){
        try {
        
            String sql = "SELECT player_id, first_name, last_name FROM players";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Loop through the result set and print all the information
            while(rs.next()){
                System.out.println("Player ID: " + rs.getInt("player_id") + 
                                   ", First Name: " + rs.getString("first_name") +
                                   ", Last Name: " + rs.getString("last_name"));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void teaminfo(){
        try {
        
            String sql = "SELECT team_id, name, abbreviation FROM teams";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Loop through the result set and print all the information
            while(rs.next()){
                System.out.println("Team ID: " + rs.getInt("team_id") + 
                                   ", Team Name: " + rs.getString("name") +
                                   ", Abbreviation: " + rs.getString("abbreviation"));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void homeTeamScores(String str){
    
        try {
            String sql = "SELECT * FROM Games WHERE Points_Home > ? ORDER BY date;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(str));
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                System.out.println("Game ID: " + rs.getInt("game_id") + 
                ", Home Points: " + rs.getInt("points_home") +
                ", Away Points: " + rs.getInt("points_away") +
                ", Date: " + rs.getDate("date"));            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void playersDraftedByTeam(String teamName) {
        try {
            String sql = "SELECT players.player_id, players.first_name, players.last_name, " +
                         "draft_picks.round_number, draft_picks.round_pick, draft_picks.overall_pick " +
                         "FROM players " +
                         "JOIN draft_picks ON players.player_id = draft_picks.player_id " +
                         "JOIN teams ON draft_picks.team_id = teams.team_id " +
                         "WHERE teams.name = ?;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Player ID: " + rs.getInt("player_id") +
                                   ", Name: " + rs.getString("first_name") + " " + rs.getString("last_name") +
                                   ", Round: " + rs.getInt("round_number") +
                                   ", Pick: " + rs.getInt("round_pick") +
                                   ", Overall Pick: " + rs.getInt("overall_pick"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void teamGamesBySeason(String seasonId) {
        try {
            String sql = "SELECT teams.name AS TeamName, " +
                         "       COUNT(games.game_id) AS GamesPlayed " +
                         "FROM teams " +
                         "JOIN play ON teams.team_id = play.team_id " +  // Join with the play table to find teams
                         "JOIN games ON games.game_id = play.game_id " +  // Join with the games table to get game data
                         "JOIN draft_picks ON draft_picks.team_id = teams.team_id " + // Join with draft_picks to filter by season
                         "WHERE draft_picks.season = ? " +  // Filter by the specific season_id
                         "GROUP BY teams.name " +  // Group by team name
                         "ORDER BY teams.name;";  // Order by team name
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, seasonId);  // Set the seasonId as a string parameter
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                System.out.println("Team: " + rs.getString("TeamName") +
                                   ", Games Played: " + rs.getInt("GamesPlayed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void organizeTeamsByAvgPoints(String seasonId) {
        try {
            // SQL query to calculate average points per team in a season
            String sql = "WITH TeamPoints AS ( " +
                         "   SELECT teams.team_id, " +
                         "          teams.name AS TeamName, " +
                         "          YEAR(games.date) AS Season, " + // SQL Server uses YEAR() instead of EXTRACT()
                         "          SUM(CASE " +
                         "              WHEN play.team_id = teams.team_id AND play.game_id = games.game_id THEN games.points_home " + // Points for home team
                         "              WHEN play.team_id = teams.team_id AND play.game_id = games.game_id THEN games.points_away " + // Points for away team
                         "              ELSE 0 END) AS TotalPoints, " +  // Points for the team
                         "          COUNT(DISTINCT games.game_id) AS GamesPlayed " + // Count of games played by the team
                         "   FROM games " +
                         "   JOIN play ON games.game_id = play.game_id " +  // Join for teams playing in the game
                         "   JOIN teams ON teams.team_id = play.team_id " +  // Link teams to play table
                         "   WHERE YEAR(games.date) = ? " +  // Filter by the year (season)
                         "   GROUP BY teams.team_id, teams.name, YEAR(games.date) " +  // Group by team and season
                         ") " +
                         "SELECT TeamName, Season, " +
                         "       TotalPoints / CAST(GamesPlayed AS FLOAT) AS AvgPoints " +  // Calculate average points per game
                         "FROM TeamPoints " +
                         "ORDER BY Season DESC, AvgPoints DESC;";  // Order by season and average points
            
            // Prepare and execute the query
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, seasonId); // Set the season parameter (year)
            ResultSet rs = stmt.executeQuery();
        
            // Output the results
            while (rs.next()) {
                double avgPoints = rs.getDouble("AvgPoints");
                System.out.println("Season: " + rs.getString("Season") + 
                                   ", Team: " + rs.getString("TeamName") +
                                   ", Average Points: " + Math.round(avgPoints));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void draftCombineStats(String teamId) {  
        try {
            String sql = "SELECT players.first_name AS FirstName, players.last_name AS LastName, " +
                         "draft_combine_stats.height_in AS HeightInches, " +
                         "draft_combine_stats.weight_lbs AS WeightPounds " +
                         "FROM draft_combine_stats " +
                         "JOIN players ON draft_combine_stats.player_id = players.player_id " +
                         "JOIN draft_picks ON players.player_id = draft_picks.player_id " +
                         "WHERE draft_picks.team_id = ?;";
    
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(teamId));  // Use setInt instead of setString for an Integer type
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                System.out.println("Player: " + rs.getString("FirstName") + " " + rs.getString("LastName") +
                                   ", Height: " + rs.getFloat("HeightInches") +  // Use getFloat for height
                                   ", Weight: " + rs.getFloat("WeightPounds"));  // Use getFloat for weight
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void foulsByQuarter(String gameId) {  // Changed gameId to int to match the schema
        try {
            String sql = "SELECT events.quarter_number AS Quarter, COUNT(events.event_number) AS Fouls " +
                         "FROM events " +
                         "WHERE events.game_id = ? AND events.event_message_type = 6 " + // 6 corresponds to fouls
                         "GROUP BY events.quarter_number;";
    
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, gameId);  // Use setInt instead of setString for an Integer type
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                System.out.println("Quarter: " + rs.getInt("Quarter") +
                                   ", Fouls: " + rs.getInt("Fouls"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void gamesByDate(String date) {
        try {
            String sql = "SELECT games.game_id, " +
                         "       home.team_id AS home_team_id, " +
                         "       away.team_id AS away_team_id, " +
                         "       games.points_home, " +
                         "       games.points_away " +
                         "FROM games " +
                         "JOIN played_for AS home ON games.game_id = home.game_id " +  // Home team join
                         "JOIN played_for AS away ON games.game_id = away.game_id " +  // Away team join
                         "WHERE games.date = ? " + 
                         "AND home.team_id != away.team_id";  // Ensuring that home and away teams are different
    
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, date); // Assuming the date format matches the one in the database
    
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                int gameId = rs.getInt("game_id");
                int homeTeamId = rs.getInt("home_team_id");
                int awayTeamId = rs.getInt("away_team_id");
                int pointsHome = rs.getInt("points_home");
                int pointsAway = rs.getInt("points_away");
    
                // Determine the winner
                String winner = (pointsHome > pointsAway) ? "Home" : "Away";
    
                System.out.println("Game ID: " + gameId +
                                   ", Home Team ID: " + homeTeamId +
                                   ", Away Team ID: " + awayTeamId +
                                   ", Winner: " + winner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void playerStats(String playerName) {
        try {
            // Split the full name into first and last name
            String firstName = playerName.split(" ")[0];
            String lastName = playerName.split(" ")[1];
    
            // SQL query to count games played based on played_for table
            String sql = "SELECT players.player_id, " +
                         "       COUNT(DISTINCT played_for.game_id) AS GamesPlayed " + // Count distinct games from played_for
                         "FROM players " +
                         "JOIN played_for ON players.player_id = played_for.player_id " + // Ensure game participation
                         "WHERE players.first_name = ? AND players.last_name = ? " +
                         "GROUP BY players.player_id;";
    
            // Prepare the SQL statement
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, firstName); // First name input
            stmt.setString(2, lastName);  // Last name input
    
            // Execute the query and retrieve the result
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Player ID: " + rs.getInt("player_id") +
                                   ", Games Played: " + rs.getInt("GamesPlayed"));
            } else {
                // Handle case where player is not found
                System.out.println("No data found for player: " + playerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void officialGamesCount(String officialId) {
        try {
            String sql = "SELECT COUNT(games.game_id) AS GamesCount " +
                         "FROM games " +
                         "JOIN ref ON games.game_id = ref.game_id " +
                         "WHERE ref.official_id = ?;";
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, officialId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Games Officiated: " + rs.getInt("GamesCount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void seasonsByTeam(String teamName) {
        try {
            String sql = "SELECT DISTINCT draft_picks.season FROM draft_picks " +
                         "JOIN teams ON draft_picks.team_id = teams.team_id " +
                         "WHERE teams.name = ?;";
    
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Season: " + rs.getInt("season"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void teamPointStats(String gameId) {
        try {
            String sql = "SELECT teams.name AS TeamName, games.points_home, games.points_away, games.date " +
                         "FROM games " +
                         "JOIN played_for ON games.game_id = played_for.game_id " +
                         "JOIN teams ON played_for.team_id = teams.team_id " +
                         "WHERE games.game_id = ?;";
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, gameId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Team: " + rs.getString("TeamName") +
                                   ", Points: " + rs.getInt("points_home") +
                                   ", Against: " + rs.getInt("points_away") +
                                   ", Date: " + rs.getDate("date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void draftByYearAndTeam(String teamName, String season) {
        try {
            String sql = "SELECT players.player_id, players.first_name, players.last_name, " +
                         "draft_picks.round_number, draft_picks.round_pick, draft_picks.overall_pick " +
                         "FROM players " +
                         "JOIN draft_picks ON players.player_id = draft_picks.player_id " +
                         "JOIN teams ON draft_picks.team_id = teams.team_id " +
                         "WHERE teams.name = ? AND draft_picks.season = ?;";  // Filter by team and season
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, teamName);  // Set the team name as the first parameter
            stmt.setString(2, season);    // Set the season as the second parameter
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                System.out.println("Player ID: " + rs.getInt("player_id") +
                                   ", Name: " + rs.getString("first_name") + " " + rs.getString("last_name") +
                                   ", Round: " + rs.getInt("round_number") +
                                   ", Pick: " + rs.getInt("round_pick") +
                                   ", Overall Pick: " + rs.getInt("overall_pick"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
