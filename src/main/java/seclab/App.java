package seclab;

import java.sql.*;
import java.util.Date;

/**
 * Hello world!
 */
public class App {

    String url = "jdbc:mysql://127.0.0.1/yelp_db";
    String name = "com.mysql.jdbc.Driver";
    String user = "root";
    String password = "root";

    private int index;

    public App(int index) {
        this.index = index;
    }

    public static void main(String[] args) {

    }

    private class FirstTwoQuery extends Thread {

        private String city;

        public FirstTwoQuery(String city) {
            this.city = city;
        }

        @Override
        public void run() {
            Connection connection = null;
            try {
                Class.forName(name);
                connection = DriverManager.getConnection(url, user, password);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 开始时间
            Long begin = new Date().getTime();
            try {

                String sql = "select " +
                        "a.id," +
                        "a.latitude," +
                        "a.longitude," +
                        "b.category," +
                        "c.count " +
                        "from business a, category b, checkin c " +
                        "where b.business_id=a.id and c.business_id=a.id " +
                        "and a.city=?";
                PreparedStatement pst = connection.prepareStatement(sql);//准备执行语句
                pst.setString(1, city);
                ResultSet resultSet = pst.executeQuery();
                pst.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 结束时间
            Long end = new Date().getTime();
            // 耗时
            System.out.println("Thread " + Thread.currentThread().getName() + " cost " + (end - begin));
        }
    }
}
