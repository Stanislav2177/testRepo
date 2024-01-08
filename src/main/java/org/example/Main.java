package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Main {
    //Change the URL to correspond of your requirements, also the username and password
    public static String URL = "jdbc:mysql://localhost:3306/coursera";
    public static String USERNAME = "change-username";
    public static String PASSWORD = "change-password";

    public static void main(String[] args) throws Exception {

        String query = "select * from students";
        Class.forName("com.mysql.cj.jdbc.Driver");
        List<String> pins = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("Connection Established successfully");

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(query)) {

                while (rs.next()) {
                    String pin = rs.getString("pin");
                    pins.add(pin);
                    System.out.println(pin);
                }
            }
            String startDate = "2019-07-16";
            String endDate = "2020-12-31";
            String outputFormat = "csv";
            String outputPath = "C:\\Users\\Stanislav\\Desktop\\testCoursera\\src\\main\\java\\org\\example\\output";
            generateReports(pins, 50, startDate, endDate, outputFormat, outputPath);
            System.out.println("Connection Closed....");
        }
    }



    public static void generateReports(List<String> selectedPins, int minCredit, String startDate,
                                       String endDate, String outputFormat, String outputPath) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String query = "SELECT " +
                    "s.first_name AS student_name, " +
                    "SUM(c.credit) AS total_credit, " +
                    "GROUP_CONCAT(" +
                    "    CONCAT('\t', c.name, ', ', c.total_time, ' hours, ', c.credit, ' credits, ', i.first_name, ' ', i.last_name) " +
                    "    ORDER BY c.name " +
                    "    SEPARATOR '\n' " +
                    ") AS course_details " +
                    "FROM " +
                    "    students s " +
                    "JOIN students_courses_xref sc ON s.pin = sc.student_pin " +
                    "JOIN courses c ON sc.course_id = c.id " +
                    "JOIN instructors i ON c.instructor_id = i.id " +
                    "WHERE " +
                    "    sc.completion_date BETWEEN ? AND ? " +
                    "GROUP BY " +
                    "    s.pin, s.first_name " +
                    "HAVING " +
                    "    total_credit >= ? " +
                    "ORDER BY " +
                    "    s.first_name";

            if (!selectedPins.isEmpty()) {
                query += " AND s.pin IN (" + "?,".repeat(selectedPins.size() - 1) + "?)";
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, startDate);
                preparedStatement.setString(2, endDate);
                preparedStatement.setInt(3, minCredit);

                int paramIndex = 4;
                for (String pin : selectedPins) {
                    preparedStatement.setString(paramIndex++, pin);
                }

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    System.out.println("Student Name: " + resultSet.getString("student_name"));
                    System.out.println("Total Credit: " + resultSet.getInt("total_credit"));
                    System.out.println("Course Details:\n" + resultSet.getString("course_details"));
                    System.out.println("----------------------------");
                }
                if ("csv".equalsIgnoreCase(outputFormat) || outputFormat == null) {
                    generateCsvReport(resultSet, outputPath);
                }

                //I got some problems with presenting the html file
                //and the time which i got left wasn't enough for fixing
                //the problem so i remove the method
                if ("html".equalsIgnoreCase(outputFormat) || outputFormat == null) {
//                    generateHtmlReport(resultSet, outputPath);
                }
            }

            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }



    private static void generateCsvReport(ResultSet resultSet, String outputPath) throws SQLException, IOException {
        try (FileWriter csvWriter = new FileWriter(outputPath + "/report.csv")) {
            csvWriter.append("PIN,First Name,Last Name,Total Credit,Courses Finished\n");

            while (resultSet.next()) {
                csvWriter.append(String.format("%s,%s,%s,%d,%s\n", resultSet.getString("pin"),
                        resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getInt("total_credit"), resultSet.getString("courses_finished")));
            }
        }
    }



}