package seclab;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Hello world!
 */
public class App {

    static String url = "jdbc:mysql://127.0.0.1/yelp_db";
    static String name = "com.mysql.jdbc.Driver";
    static String user = "root";
    static String password = "root";

    private static final List<ResultEntity> resultEntityList = new ArrayList<>();
    private static Connection connection = null;

    public static void main(String[] args) {
        String city = "Charlotte";

        Connection connection = null;
        try {
            Class.forName(name);
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String sql = "select " +
                    "a.id," +
                    "a.city," +
                    "a.latitude," +
                    "a.longitude " +
                    "from business a " +
                    "where a.city=? order by a.city";
            PreparedStatement pst = connection.prepareStatement(sql);//准备执行语句
            pst.setString(1, city);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                ResultEntity resultEntity = new ResultEntity();
                resultEntity.setBusiness_id(resultSet.getString("id"));
                resultEntity.setLatitude(resultSet.getFloat("latitude"));
                resultEntity.setLongitude(resultSet.getFloat("longitude"));
                resultEntity.setCity(resultSet.getString("city"));
                resultEntityList.add(resultEntity);
            }

            System.out.println("business表查询完毕，总共有 " + resultEntityList.size() + " 条结果");

            System.out.println("子线程开始执行！");

            Thread a = new CategoryQuery("category");
            a.start();
            Thread b = new CountQuery("count");
            b.start();
//            Thread c = new ReviewQuery("review");
//            c.start();

            a.join();
            b.join();
//            c.join();

            System.out.println("子线程执行完毕！");
            System.out.println("开始写入结果...");
            // 将结果写入文件
            File file = new File("E:\\idea-projects\\query\\src\\main\\java\\seclab\\result_" + city + ".txt");
            file.createNewFile();
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < resultEntityList.size(); i++) {
                br.write(resultEntityList.get(i).toString() + "\r\n");
            }
            br.flush(); // 把缓存区内容压入文件
            br.close(); // 最后记得关闭文件
            System.out.println("写入结果完毕！");
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class CategoryQuery extends Thread {

        public CategoryQuery(String name) {
            super(name);
        }

        @Override
        public void run() {

            System.out.println("Thread " + Thread.currentThread() + " started...");

            Connection connection = null;
            try {
                Class.forName(name);
                connection = DriverManager.getConnection(url, user, password);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // category
            String categorySql = "select " +
                    "category " +
                    "from category " +
                    "where business_id=?";
            PreparedStatement pst = null;
            try {
                for (ResultEntity resultEntity1 : resultEntityList) {
                    pst = connection.prepareStatement(categorySql);//准备执行语句
                    pst.setString(1, resultEntity1.getBusiness_id());
                    System.out.println("query category for business_id=" + resultEntity1.getBusiness_id());
                    ResultSet resultSetCategory = pst.executeQuery();
                    List<String> categories = new ArrayList<>();
                    while (resultSetCategory.next()) {
                        categories.add(resultSetCategory.getString("category"));
                    }
                    resultEntity1.setCategories(categories);
                }
                pst.close();
                connection.close();
            } catch (Exception e) {
            }

            System.out.println("Thread " + Thread.currentThread() + " finished...");
        }
    }

    private static class CountQuery extends Thread {

        public CountQuery(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println("Thread " + Thread.currentThread() + " started...");
            Connection connection = null;
            try {
                Class.forName(name);
                connection = DriverManager.getConnection(url, user, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // count
            String countSql = "select sum(count) as count from checkin where business_id=?";
            PreparedStatement pst = null;
            try {
                for (ResultEntity resultEntity1 : resultEntityList) {
                    pst = connection.prepareStatement(countSql);
                    pst.setString(1, resultEntity1.getBusiness_id());

                    System.out.println("query count for business_id=" + resultEntity1.getBusiness_id());
                    ResultSet resultSetCount = pst.executeQuery();
                    int count = 0;

                    while (resultSetCount.next()) {
                        count += resultSetCount.getInt("count");
                    }
                    resultEntity1.setCount(count);
                }

                pst.close();
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Thread " + Thread.currentThread() + " finished...");
        }
    }

    private static class ReviewQuery extends Thread {

        public ReviewQuery(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println("Thread " + Thread.currentThread() + " started...");
            Connection connection = null;
            try {
                Class.forName(name);
                connection = DriverManager.getConnection(url, user, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // review
            String reviewSql = "select " +
                    "text " +
                    "from review " +
                    "where business_id=?";
            PreparedStatement pst = null;
            try {
                for (ResultEntity resultEntity1 : resultEntityList) {
                    pst = connection.prepareStatement(reviewSql);
                    pst.setString(1, resultEntity1.getBusiness_id());
                    ResultSet resultSetReview = pst.executeQuery();
                    List<String> reviews = new ArrayList<>();
                    while (resultSetReview.next()) {
                        reviews.add(resultSetReview.getString("text"));
                    }
                    resultEntity1.setReviews(reviews);
                }

                pst.close();
                connection.close();
            } catch (Exception e) {
            }
            System.out.println("Thread " + Thread.currentThread() + " finished...");
        }
    }
}
