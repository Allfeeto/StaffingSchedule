import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.CallableStatement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class DAO {
    private static Connection connection;

    public DAO() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAllFromTable(String tableName) {
        try {
            String[] columnNames;
            String sql;

            if (tableName.equals("Факультет")) {
                tableName = "Faculty";
                columnNames = new String[]{"<html><b>№</b></html>", "<html><b>Id</b></html>", "<html><b>Полное наименование факультета</b></html>", "<html><b>Краткое наименование факультета</b></html>"};
                sql = "SELECT ID, NameFaculty, ShortNameFaculty FROM " + tableName;
            } else if (tableName.equals("Кафедра")) {
                tableName = "Chair";
                columnNames = new String[]{"<html><b>№</b></html>", "<html><b>Id</b></html>", "<html><b>Факультет</b></html>", "<html><b>Полное наименование кафедры</b></html>", "<html><b>Краткое наименование кафедры</b></html>"};
                sql = "SELECT Chair.ID, Faculty.NameFaculty, Chair.NameChair, Chair.ShortNameChair " +
                      "FROM Chair " +
                      "JOIN Faculty ON Chair.IdFaculty = Faculty.ID";
            } else if (tableName.equals("Преподаватель")) {
                tableName = "Teacher";
                columnNames = new String[]{
                    "<html><b>№</b></html>", 
                    "<html><b>Id</b></html>",
                    "<html><b>Кафедра</b></html>", 
                    "<html><b>Должность</b></html>", 
                    "<html><b>Фамилия</b></html>", 
                    "<html><b>Имя</b></html>", 
                    "<html><b>Отчество</b></html>", 
                    "<html><b>Дата рождения</b></html>", 
                    "<html><b>ИНН</b></html>", 
                    "<html><b>СНИЛС</b></html>"
                };
                sql = "SELECT Teacher.ID, Chair.ShortNameChair, Post.NamePost, Teacher.Surname, Teacher.FirstName, Teacher.Patronymic, " +
                      "Teacher.DateOfBirth, Teacher.Inn, Teacher.Snils " +
                      "FROM Teacher " +
                      "JOIN Chair ON Teacher.IdChair = Chair.ID " +
                      "JOIN Post ON Teacher.IdPost = Post.ID";
            } else if (tableName.equals("Штатное расписание")) {
                tableName = "StaffingSchedule";
                columnNames = new String[]{
                    "<html><b>№</b></html>", 
                    "<html><b>Id</b></html>",
                    "<html><b>Кафедра</b></html>", 
                    "<html><b>Должность</b></html>", 
                    "<html><b>Количество</b></html>"
                };
                sql = "SELECT StaffingSchedule.ID, Chair.ShortNameChair, Post.NamePost, StaffingSchedule.RequiredCount " +
                      "FROM StaffingSchedule " +
                      "JOIN Chair ON StaffingSchedule.IdChair = Chair.ID " +
                      "JOIN Post ON StaffingSchedule.IdPost = Post.ID " +
                      "WHERE StaffingSchedule.IdChair = 1";// ВОЗМОЖНО ПОМЕНЯТЬ!!!!!!
            } else {
                tableName = "Post";
                columnNames = new String[]{"<html><b>№</b></html>", "<html><b>Id</b></html>", "<html><b>Должность</b></html>"};
                sql = "SELECT ID, NamePost FROM " + tableName;
            }

            Main.getTableModel().setColumnIdentifiers(columnNames);

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Удаление старых данных из таблицы
            Main.getTableModel().setRowCount(0);

            // Установка ширины столбцов
            Main.table.getColumnModel().getColumn(0).setMaxWidth(30); // max ширина столбца №

            // Добавление данных в таблицу с нумерацией
            int rowNumber = 1;
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount + 1]; // +1 для нумерации строк
                rowData[0] = String.valueOf(rowNumber++); // Нумерация строк
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i] = resultSet.getObject(i);
                }
                Main.getTableModel().addRow(rowData);
            }

            // Скрыть колонку с ID
            if (columnCount > 1) {
                Main.table.removeColumn(Main.table.getColumnModel().getColumn(1));
                if (tableName.equals("StaffingSchedule")) {
                    Main.table.removeColumn(Main.table.getColumnModel().getColumn(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void getTeachersFromChair(int chairId) {
        try {
            String[] columnNames;
            String sql;

                columnNames = new String[]{
                    "<html><b>№</b></html>", 
                    "<html><b>Id</b></html>",
                    "<html><b>Кафедра</b></html>", 
                    "<html><b>Должность</b></html>", 
                    "<html><b>Фамилия</b></html>", 
                    "<html><b>Имя</b></html>", 
                    "<html><b>Отчество</b></html>", 
                    "<html><b>Дата рождения</b></html>", 
                    "<html><b>ИНН</b></html>", 
                    "<html><b>СНИЛС</b></html>", 
                };
                sql = "SELECT Teacher.Id, Chair.ShortNameChair, Post.NamePost, Teacher.Surname, Teacher.FirstName, Teacher.Patronymic, " +
                      "Teacher.DateOfBirth, Teacher.Inn, Teacher.Snils " +
                      "FROM Teacher " +
                      "JOIN Chair ON Teacher.IdChair = Chair.ID " +
                      "JOIN Post ON Teacher.IdPost = Post.ID " +
                      "WHERE Teacher.IdChair = ?";

                
            Main.getTableModel().setColumnIdentifiers(columnNames);

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, chairId);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Удаление старых данных из таблицы
            Main.getTableModel().setRowCount(0);

            // Установка ширины столбцов
            Main.table.getColumnModel().getColumn(0).setMaxWidth(30); // max ширина столбца №
            
            // Добавление данных в таблицу с нумерацией
            int rowNumber = 1;
            while (resultSet.next()) {
                String[] rowData = new String[columnCount + 1]; // +1 для нумерации строк
                rowData[0] = String.valueOf(rowNumber++); 
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i] = resultSet.getString(i);
                }
                Main.getTableModel().addRow(rowData);
            }
            // Скрыть колонку с ID
            if (columnCount > 1) {
                Main.table.removeColumn(Main.table.getColumnModel().getColumn(1));
                Main.table.removeColumn(Main.table.getColumnModel().getColumn(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при выполнении запроса: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void getStaffingScheduleByChair(int chairId) {
        try {
            String[] columnNames = {
                "<html><b>№</b></html>", 
                "<html><b>Id</b></html>",
                "<html><b>Кафедра</b></html>", 
                "<html><b>Должность</b></html>", 
                "<html><b>Количество</b></html>"
            };
            String sql = "SELECT StaffingSchedule.ID, Chair.ShortNameChair, Post.NamePost, StaffingSchedule.RequiredCount " +
                         "FROM StaffingSchedule " +
                         "JOIN Chair ON StaffingSchedule.IdChair = Chair.ID " +
                         "JOIN Post ON StaffingSchedule.IdPost = Post.ID " +
                         "WHERE StaffingSchedule.IdChair = ?";

            Main.getTableModel().setColumnIdentifiers(columnNames);

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, chairId);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Удаление старых данных из таблицы
            Main.getTableModel().setRowCount(0);

            // Установка ширины столбцов
            Main.table.getColumnModel().getColumn(0).setMaxWidth(30); // max ширина столбца №

            // Добавление данных в таблицу с нумерацией
            int rowNumber = 1;
            while (resultSet.next()) {
                String[] rowData = new String[columnCount + 1]; // +1 для нумерации строк
                rowData[0] = String.valueOf(rowNumber++); // Нумерация строк
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i] = resultSet.getString(i);
                }
                Main.getTableModel().addRow(rowData);
            }
            // Скрыть колонку с ID
            if (columnCount > 1) {
                Main.table.removeColumn(Main.table.getColumnModel().getColumn(1));
                Main.table.removeColumn(Main.table.getColumnModel().getColumn(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при выполнении запроса: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void getChairByFaculty(int facultyId) {
        try {
            String[] columnNames = new String[]{"<html><b>№</b></html>", "<html><b>Id</b></html>", "<html><b>Факультет</b></html>", "<html><b>Полное наименование кафедры</b></html>", "<html><b>Краткое наименование кафедры</b></html>"};

            String sql = "SELECT Chair.ID, Faculty.NameFaculty, Chair.NameChair, Chair.ShortNameChair " +
                    	 "FROM Chair " +
                    	 "JOIN Faculty ON Chair.IdFaculty = Faculty.ID " +
                         "WHERE Chair.IdFaculty = ?";
            
            Main.getTableModel().setColumnIdentifiers(columnNames);

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, facultyId);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Удаление старых данных из таблицы
            Main.getTableModel().setRowCount(0);

            // Установка ширины столбцов
            Main.table.getColumnModel().getColumn(0).setMaxWidth(30); // max ширина столбца №

            // Добавление данных в таблицу с нумерацией
            int rowNumber = 1;
            while (resultSet.next()) {
                String[] rowData = new String[columnCount + 1]; // +1 для нумерации строк
                rowData[0] = String.valueOf(rowNumber++); // Нумерация строк
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i] = resultSet.getString(i);
                }
                Main.getTableModel().addRow(rowData);
            }
            // Скрыть колонку с ID
            if (columnCount > 1) {
                Main.table.removeColumn(Main.table.getColumnModel().getColumn(1));
                Main.table.removeColumn(Main.table.getColumnModel().getColumn(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при выполнении запроса: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchTeacher(String surname) {
        try {
            String sql = "{CALL GetTeacherInfo(?)}";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setString(1, surname);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Main.getTableModel().setRowCount(0);

            String[] columnNames = {"<html><b>№</b></html>", 
                    "<html><b>ID</b></html>", 
                    "<html><b>Кафедра</b></html>", 
                    "<html><b>Должность</b></html>", 
                    "<html><b>Фамилия</b></html>", 
                    "<html><b>Имя</b></html>", 
                    "<html><b>Отчество</b></html>", 
                    "<html><b>Дата рождения</b></html>", 
                    "<html><b>ИНН</b></html>", 
                    "<html><b>СНИЛС</b></html>"};

            Main.getTableModel().setColumnIdentifiers(columnNames);
            Main.table.getColumnModel().getColumn(0).setMaxWidth(30);

            int rowNumber = 1;
            boolean found = false;
            while (resultSet.next()) {
                found = true;
                String[] rowData = new String[columnCount + 1];
                rowData[0] = String.valueOf(rowNumber++);
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i] = resultSet.getString(i);
                }
                Main.getTableModel().addRow(rowData);
            }
            // Скрыть колонку с ID
            if (columnCount > 1) {
                Main.table.removeColumn(Main.table.getColumnModel().getColumn(1));
            }

            if (!found) {
                JOptionPane.showMessageDialog(null, "Преподаватель с фамилией \"" + surname + "\" не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при выполнении поиска.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    /////////////////////////
    public List<Faculty> listFaculty() {
        List<Faculty> faculties = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Faculty";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Faculty faculty = new Faculty();
                faculty.setId(resultSet.getInt("ID"));
                faculty.setNameFaculty(resultSet.getString("NameFaculty"));
                faculty.setShortNameFaculty(resultSet.getString("ShortNameFaculty"));
                faculties.add(faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении списка должностей: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return faculties;
    }
    
    // Метод для получения списка всех кафедр
    public List<Chair> listChairs() {
        List<Chair> chairs = new ArrayList<>();
        try {
            String sql = "SELECT ID, IdFaculty, NameChair, ShortNameChair FROM Chair";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Chair chair = new Chair();
                chair.setId(resultSet.getInt("ID"));
                chair.setIdFaculty(resultSet.getInt("IdFaculty"));
                chair.setNameChair(resultSet.getString("NameChair"));
                chair.setShortNameChair(resultSet.getString("ShortNameChair"));
                chairs.add(chair);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении списка должностей: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return chairs;
    }

    // Метод для получения списка всех должностей
    public List<Post> listPosts() {
        List<Post> posts = new ArrayList<>();
        try {
            String sql = "SELECT ID, NamePost FROM Post";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("ID"));
                post.setNamePost(resultSet.getString("NamePost"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении списка должностей: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return posts;
    }
    
    public int getCountOfTeachersByChairAndPost(int idChair, int idPost) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Teacher WHERE IdChair = ? AND IdPost = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idChair);
            statement.setInt(2, idPost);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getRequiredCountByChairAndPost(int idChair, int idPost) {
        int requiredCount = 0;
        try {
            String sql = "SELECT RequiredCount FROM StaffingSchedule WHERE IdChair = ? AND IdPost = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idChair);
            statement.setInt(2, idPost);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                requiredCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requiredCount;
    }
    public boolean isInnExists(String inn) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) FROM Teacher WHERE INN = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, inn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    public void insertTeacher(Teacher teacher) throws SQLException {
        String sql = "INSERT INTO Teacher (IdChair, IdPost, Surname, FirstName, Patronymic, DateOfBirth, Inn, Snils) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, teacher.getIdChair());
            statement.setInt(2, teacher.getIdPost());
            statement.setString(3, teacher.getSurname());
            statement.setString(4, teacher.getFirstName());
            statement.setString(5, teacher.getPatronymic());
            statement.setDate(6, teacher.getDateOfBirth());
            statement.setLong(7, teacher.getInn());
            statement.setLong(8, teacher.getSnils());

            statement.executeUpdate();
        }
    }
    
    public int getCountOfTeachersByChairAndPostExcludingCurrent(int chairId, int postId, int currentTeacherId) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Teacher WHERE IdChair = ? AND IdPost = ? AND ID != ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, chairId);
            statement.setInt(2, postId);
            statement.setInt(3, currentTeacherId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public boolean isInnExistsExceptCurrent(String inn, int currentTeacherId) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) FROM Teacher WHERE INN = ? AND ID != ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, inn);
            statement.setInt(2, currentTeacherId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    
    public void updateTeacher(Teacher teacher) throws SQLException {
    	String sql = "UPDATE Teacher SET IdChair=?, IdPost=?, Surname=?, FirstName=?, Patronymic=?, DateOfBirth=?, Inn=?, Snils=? WHERE Id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, teacher.getIdChair());
            statement.setInt(2, teacher.getIdPost());
            statement.setString(3, teacher.getSurname());
            statement.setString(4, teacher.getFirstName());
            statement.setString(5, teacher.getPatronymic());
            statement.setDate(6, teacher.getDateOfBirth());
            statement.setLong(7, teacher.getInn());
            statement.setLong(8, teacher.getSnils());
            statement.setInt(9, teacher.getId());
            statement.executeUpdate();
        }
    }

    ////////////////////////////
    public void insertStaffingSchedule(StaffingSchedule staffingSchedule) throws SQLException {
        String sql = "INSERT INTO StaffingSchedule (IdChair, IdPost, RequiredCount) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, staffingSchedule.getIdChair());
        statement.setInt(2, staffingSchedule.getIdPost());
        statement.setInt(3, staffingSchedule.getRequiredCount());

        statement.executeUpdate();
    }
    public void updateStaffingSchedule(StaffingSchedule staffingSchedule) {
        try {
            String sql = "UPDATE StaffingSchedule SET IdChair = ?, IdPost = ?, RequiredCount = ? WHERE Id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, staffingSchedule.getIdChair());
            statement.setInt(2, staffingSchedule.getIdPost());
            statement.setInt(3, staffingSchedule.getRequiredCount());
            statement.setInt(4, staffingSchedule.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при обновлении записи: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    ///////////////////////////
    
    public void insertFaculty(Faculty faculty) throws SQLException {
        String sql = "INSERT INTO Faculty (NameFaculty, ShortNameFaculty) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, faculty.getNameFaculty());
        statement.setString(2, faculty.getShortNameFaculty());

        statement.executeUpdate();
    }
    
    public void updateFaculty(Faculty faculty) {
        try {
            String sql = "UPDATE Faculty SET NameFaculty = ?, ShortNameFaculty = ? WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, faculty.getNameFaculty());
            statement.setString(2, faculty.getShortNameFaculty());
            statement.setInt(3, faculty.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void insertChair(Chair chair) throws SQLException {
        String sql = "INSERT INTO Chair (IdFaculty, NameChair, ShortNameChair) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, chair.getIdFaculty());
        statement.setString(2, chair.getNameChair());
        statement.setString(3, chair.getShortNameChair());

        statement.executeUpdate();
    }
    
    public void updateChair(Chair chair) {
        try {
            String sql = "UPDATE Chair SET IdFaculty = ?, NameChair = ?, ShortNameChair = ? WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, chair.getIdFaculty());
            statement.setString(2, chair.getNameChair());
            statement.setString(3, chair.getShortNameChair());
            statement.setInt(4, chair.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /////////////////////
    public void insertPost(Post post) throws SQLException {
        String sql = "INSERT INTO Post (NamePost) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, post.getNamePost());

        statement.executeUpdate();
    }

    public void updatePost(Post post) {
        try {
            String sql = "UPDATE Post SET NamePost = ? WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, post.getNamePost());
            statement.setInt(2, post.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // метод проверки зависимых записей
    public boolean hasDependentRecords(int id, String tableName) {
        try {
            String sql;
            if (tableName.equals("Faculty")) {
                sql = "SELECT 1 FROM Chair WHERE IdFaculty = ? LIMIT 1";
            } else if (tableName.equals("Chair")) {
                sql = "SELECT 1 FROM Teacher WHERE IdChair = ? LIMIT 1";
            } else if (tableName.equals("Post")) {
                sql = "SELECT 1 FROM Teacher WHERE IdPost = ? LIMIT 1";
            } else if (tableName.equals("Teacher")) {
            	return false; // Teacher пока не имеет зависимых таблиц
                //sql = "SELECT 1 FROM StaffingSchedule WHERE IdChair = ? OR IdPost = ? LIMIT 1";
            } else if (tableName.equals("StaffingSchedule")) {
                return false; // StaffingSchedule не имеет зависимых таблиц
            } else {
                throw new IllegalArgumentException("Неизвестная таблица : " + tableName);
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            /*if (tableName.equals("Teacher")) {
                statement.setInt(2, id);
            }*/
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void deleteButton(int id, String tableName) {
        try {
            String sql = "DELETE FROM " + tableName + " WHERE Id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}