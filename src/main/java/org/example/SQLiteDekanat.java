package org.example;

import java.sql.*;

public class SQLiteDekanat {
    private static final String DB_URL = "jdbc:sqlite:dekanat.db";

    public static void main(String[] args) {
        createTables();
        createTeacher("Марина Степанівна");
        createStudent("Олексій Гнатюк", 92.3, "2002-11-05");
        createSubject("Фізика");
        readStudents();
        updateStudent(1, "Олексій Гнатюк", 94.0, "2002-11-05");
        deleteSubject(1);
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void createTables() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS teachers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    avg_grade REAL,
                    birth_date TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS subjects (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
            """);

            System.out.println("Таблиці створено");
        } catch (SQLException e) {
            System.out.println("Помилка створення таблиць:" + e.getMessage());
        }
    }

    public static void createTeacher(String name) {
        String sql = "INSERT INTO teachers(name) VALUES (?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
            System.out.println("Викладача додано");
        } catch (SQLException e) {
            System.out.println("Помилка додавання викладача: " + e.getMessage());
        }
    }

    public static void createStudent(String name, double avgGrade, String birthDate) {
        String sql = "INSERT INTO students(name, avg_grade, birth_date) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, avgGrade);
            ps.setString(3, birthDate);
            ps.executeUpdate();
            System.out.println("Студента додано");
        } catch (SQLException e) {
            System.out.println("Помилка додавання студента: " + e.getMessage());
        }
    }

    public static void createSubject(String name) {
        String sql = "INSERT INTO subjects(name) VALUES (?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
            System.out.println("Дисципліну додано");
        } catch (SQLException e) {
            System.out.println("Помилка додавання дисципліни: " + e.getMessage());
        }
    }

    public static void readStudents() {
        String sql = "SELECT * FROM students";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d | Ім'я: %s | Середній бал: %.2f | Дата народження: %s%n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getDouble("avg_grade"), rs.getString("birth_date"));
            }
        } catch (SQLException e) {
            System.out.println("Помилка читання студентів: " + e.getMessage());
        }
    }

    public static void updateStudent(int id, String name, double avgGrade, String birthDate) {
        String sql = "UPDATE students SET name=?, avg_grade=?, birth_date=? WHERE id=?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, avgGrade);
            ps.setString(3, birthDate);
            ps.setInt(4, id);
            int rows = ps.executeUpdate();
            System.out.println("Оновлено записів: " + rows);
        } catch (SQLException e) {
            System.out.println("Помилка оновлення студента: " + e.getMessage());
        }
    }

    public static void deleteSubject(int id) {
        String sql = "DELETE FROM subjects WHERE id=?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println("Видалено дисциплін: " + rows);
        } catch (SQLException e) {
            System.out.println("Помилка видалення дисципліни: " + e.getMessage());
        }
    }
}
