import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class YelpQueries
{
  public static void main(String[] args) throws ClassNotFoundException
  {
    // load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC");

    String dbLocation = "yelp_dataset.db"; 

    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

      Statement statement = connection.createStatement();

      // Question 0
      statement.execute("DROP VIEW IF EXISTS q0"); // Clean out views
      String q0 = "CREATE VIEW q0 AS "
                   + "SELECT count(*) FROM reviews";
      statement.execute(q0);

      // Question 1
      statement.execute("DROP VIEW IF EXISTS q1");
      String q1 = "CREATE VIEW q1 AS " 
                  + "SELECT AVG(u.review_count) FROM users u WHERE u.review_count < 10"; // Replace this line
      statement.execute(q1);

      // Question 2
      statement.execute("DROP VIEW IF EXISTS q2");
      String q2 = "CREATE VIEW q2 AS "
                  + "SELECT u.name FROM users u WHERE u.review_count>50 AND u.yelping_since>'2014-11'"; // Replace this line
      statement.execute(q2);

      // Question 3
      statement.execute("DROP VIEW IF EXISTS q3");
      String q3 = "CREATE VIEW q3 AS "
                  + "SELECT b.name,b.stars FROM businesses b WHERE b.stars>3 AND b.city='Pittsburgh'"; // Replace this line
      statement.execute(q3);

      // Question 4
      statement.execute("DROP VIEW IF EXISTS q4");
      String q4 = "CREATE VIEW q4 AS "
                  + "SELECT b.name FROM businesses b WHERE b.city='Las Vegas' AND b.review_count>=500 AND b.stars=(SELECT MIN(b1.stars) FROM businesses b1 WHERE b1.city='Las Vegas' AND b1.review_count>=500)";
      statement.execute(q4);

      // Question 5
      statement.execute("DROP VIEW IF EXISTS q5");
      String q5 = "CREATE VIEW q5 AS "
                  + "SELECT b.name FROM businesses b WHERE b.business_id IN  "
                  + "(SELECT c.business_id FROM checkins c WHERE c.day=0 ORDER BY c.num_checkins DESC LIMIT 5)"; // Replace this line
      statement.execute(q5);

      // Question 6
      statement.execute("DROP VIEW IF EXISTS q6");
      String q6 = "CREATE VIEW q6 AS "
                  + "SELECT c.day FROM checkins c GROUP BY c.day HAVING SUM(c.num_checkins)="
                  + "(SELECT MAX(y.num) FROM (SELECT SUM(c1.num_checkins) as num FROM checkins c1 GROUP BY c1.day) y)"; // count of the most checkins
      statement.execute(q6);

      // Question 7
      statement.execute("DROP VIEW IF EXISTS q7");
      String q7 = "CREATE VIEW q7 AS "
                  + "SELECT b.name FROM businesses b WHERE b.business_id IN "
                  + "(SELECT r.business_id FROM reviews r WHERE r.user_id="
                  + "(SELECT u.user_id FROM users u WHERE u.review_count= (SELECT MAX(u1.review_count) FROM users u1)))";
      statement.execute(q7);

      // Question 8
      statement.execute("DROP VIEW IF EXISTS q8");
      String q8 = "CREATE VIEW q8 AS "
              + "SELECT AVG(e.stars) FROM "
              + "(SELECT * FROM businesses b WHERE b.city='Edinburgh' ORDER BY b.review_count DESC LIMIT (SELECT COUNT(*)/10 FROM businesses bb WHERE bb.city='Edinburgh')) as e";
      statement.execute(q8);

      // Question 9
      statement.execute("DROP VIEW IF EXISTS q9");
      String q9 = "CREATE VIEW q9 AS "
                  + "SELECT u.name FROM users u WHERE u.name LIKE '%..%'";
      statement.execute(q9);

      // Question 10
      statement.execute("DROP VIEW IF EXISTS q10");
      String q10 = "CREATE VIEW q10 AS "

      // "SELECT filt.city FROM _____ AS filt ORDER BY filt.count LIMIT 1"
      // "SELECT b.city, count(*) as count FROM businesses b WHERE b.business_id IN _____ GROUP BY b.city"
      // "SELECT r.business_id FROM reviews r WHERE r.user_id IN _____ "
      // "SELECT u.user_id FROM users u WHERE u.name LIKE '%..%'"

      + "SELECT filt.city FROM (SELECT b.city, count(*) as count FROM businesses b WHERE b.business_id IN (SELECT r.business_id FROM reviews r WHERE r.user_id IN (SELECT u.user_id FROM users u WHERE u.name LIKE '%..%') ) GROUP BY b.city) AS filt ORDER BY filt.count DESC LIMIT 1";

      statement.execute(q10);

      connection.close();

    }
    catch(SQLException e)
    {
      // if the error message is "out of memory", 
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }
}
