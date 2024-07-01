import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


import javax.swing.UIManager.*;


public class Main {
	private static DAO DAO = new DAO();
    private static JComboBox<String> tableSelector;
    static JTable table;
    private static DefaultTableModel tableModel;
    private static JTextField surnameField;
    private static JButton searchButton;
    
    public static DefaultTableModel getTableModel() {
        return tableModel;
    }
    
	public static void main(String[] args) {
        // Устанавливаем Look and Feel Nimbus
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Если Nimbus не найден, используем стандартный Look and Feel
            e.printStackTrace();
        }
		JFrame frame = new JFrame("Штатное расписание");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
       
        // Установка иконки приложения
        try {
            // Используем getResource для загрузки иконки
            ImageIcon icon = new ImageIcon(Main.class.getResource("/logo.png"));
            frame.setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Иконка не найдена.");
        }
        
        // Устанавливаем локализацию для диалоговых окон
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.okButtonText", "ОК");
        

        JPanel panel = new JPanel(new BorderLayout());
        
        
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);   
        // Запретить редактирование всех ячеек таблицы
        table.setDefaultEditor(Object.class, null); // Запретить редактирование всех ячеек
        JScrollPane scrollPane = new JScrollPane(table);

        //JButton viewButton = new JButton("Просмотр таблицы");
        JButton insertButton = new JButton("Добавление записи");
        JButton editButton = new JButton("Редактирование записи");
        JButton deleteButton = new JButton("Удаление записи");
        

        //Styles.setButtonStyle(viewButton);
        Styles.setButtonStyle(insertButton);
        Styles.setButtonStyle(editButton);
        Styles.setButtonStyle(deleteButton);
        
        
        surnameField = new JTextField(10);
        searchButton = new JButton("Найти");
        
        Styles.setButtonStyle(searchButton); 

        
        JPanel buttonPanelAll = new JPanel(new BorderLayout());

        // Создаем JComboBox с выбором посреди современных стилей
        JComboBox<String> tableSelector = new JComboBox<>(new String[]{"Преподаватель", "Штатное расписание", "Факультет", "Кафедра", "Должность"});
        
        surnameField.setBorder(null);
        
        // Создаем JComboBox для выбора кафедр
        JComboBox<Chair> mainChairComboBox = new JComboBox<>();
        
        // Получаем список кафедр и заполняем chairComboBox
        List<Chair> chairs = DAO.listChairs();
        mainChairComboBox.removeAllItems();
        // Добавляем специальный элемент для выбора "ВСЕ"
        mainChairComboBox.addItem(new Chair(0, "ВСЕ", "ВСЕ"));
        for (Chair chair : chairs) {
        	mainChairComboBox.addItem(chair);
        }
        // Создаем JComboBox для выбора Факультетов
        JComboBox<Faculty> mainFacultyComboBox = new JComboBox<>();
        
        // Получаем список кафедр и заполняем chairComboBox
        List<Faculty> faculties = DAO.listFaculty();
        mainFacultyComboBox.removeAllItems();
        // Добавляем специальный элемент для выбора "ВСЕ"
        mainFacultyComboBox.addItem(new Faculty(0, "ВСЕ", "ВСЕ"));
        for (Faculty faculty : faculties) {
        	mainFacultyComboBox.addItem(faculty);
        }
        
        JPanel buttonPanel = new JPanel();
        Color customColor = new Color(30, 39, 120);
        buttonPanel.setBackground(customColor);
        buttonPanel.add(new JLabel("Справочники:")).setForeground(Color.WHITE);
        buttonPanel.add(tableSelector);
        buttonPanel.add(insertButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        JPanel chairPanel = new JPanel();
        chairPanel.setBackground(customColor);
        chairPanel.add(new JLabel("Выбор кафедры:")).setForeground(Color.WHITE);
        chairPanel.add(mainChairComboBox);
        
        JPanel facultyPanel = new JPanel();
        facultyPanel.setBackground(customColor);
        facultyPanel.add(new JLabel("Выбор факультета:")).setForeground(Color.WHITE);
        facultyPanel.add(mainFacultyComboBox);
        facultyPanel.setVisible(false);
        
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(customColor);
        searchPanel.add(new JLabel("Поиск преподавателя по фамилии:")).setForeground(Color.WHITE);
        searchPanel.add(surnameField);
        searchPanel.add(searchButton);

        buttonPanelAll.setLayout(new BoxLayout(buttonPanelAll, BoxLayout.Y_AXIS));
        buttonPanelAll.add(buttonPanel); // Добавляем кнопки
        buttonPanelAll.add(chairPanel); // Добавляем панель с кафедрами
        buttonPanelAll.add(facultyPanel); // Добавляем панель с факультетами
        buttonPanelAll.add(searchPanel); // Добавляем панель с поиском

        panel.add(buttonPanelAll, BorderLayout.NORTH); // Добавляем панель с кнопками вверху
        panel.add(scrollPane, BorderLayout.CENTER); // Добавляем таблицу в центр
       
        frame.getContentPane().add(panel);
        
        Color customColorButton = new Color(61, 78, 235);
        Styles.ButtonHoverListener hoverListener = new Styles.ButtonHoverListener(customColorButton);

        searchButton.addMouseListener(hoverListener);
        insertButton.addMouseListener(hoverListener);
        editButton.addMouseListener(hoverListener);
        deleteButton.addMouseListener(hoverListener);

        DAO.getAllFromTable("Преподаватель");
        
        
        tableSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTable = (String) tableSelector.getSelectedItem();
                if (selectedTable.equals("Преподаватель")) {
                    // Показываем chairComboBox
                	chairPanel.setVisible(true);
                	searchPanel.setVisible(true);
                	facultyPanel.setVisible(false);
                    // Получаем список кафедр и заполняем chairComboBox
                    // Добавляем специальный элемент для выбора "ВСЕ"
                    List<Chair> chairs = DAO.listChairs();
                    mainChairComboBox.removeAllItems();
                    mainChairComboBox.addItem(new Chair(0, "ВСЕ", "ВСЕ"));
                    for (Chair chair : chairs) {
                    	mainChairComboBox.addItem(chair);
                    }
                } else if (selectedTable.equals("Штатное расписание")) {
                    // Показываем chairComboBox
                	chairPanel.setVisible(true);
                	searchPanel.setVisible(false);
                	facultyPanel.setVisible(false);
                    // Получаем список кафедр и заполняем chairComboBox
                    List<Chair> chairs = DAO.listChairs();
                    mainChairComboBox.removeAllItems();
                    for (Chair chair : chairs) {
                    	mainChairComboBox.addItem(chair);
                    }
                } else if (selectedTable.equals("Кафедра")) {
                    // Показываем chairComboBox
                	chairPanel.setVisible(false);
                	searchPanel.setVisible(false);
                	facultyPanel.setVisible(true);
                    // Обновляем главный comboBox
                    mainFacultyComboBox.setSelectedIndex(0);

                } else {
                    // Скрываем chairComboBox, если выбран другой справочник
                	chairPanel.setVisible(false);
                	searchPanel.setVisible(false);
                	facultyPanel.setVisible(false);
                }
                DAO.getAllFromTable(selectedTable);
            }
        });

        mainFacultyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Получаем выбранную кафедру
            	Faculty selectedFaculty = (Faculty) mainFacultyComboBox.getSelectedItem();
            	if (selectedFaculty != null) {
                	if (selectedFaculty.getId() == 0) {
                        // Если выбрано "ВСЕ", отображаем всех преподавателей
                        DAO.getAllFromTable("Кафедра");
                    } else {
                        // Иначе отображаем преподавателей, связанных с выбранной кафедрой
                        int facultyId = selectedFaculty.getId();
                        DAO.getChairByFaculty(facultyId);
                    }
                }
            }
        });
        
        mainChairComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Получаем выбранную кафедру
                Chair selectedChair = (Chair) mainChairComboBox.getSelectedItem();
                String selectedTable = (String) tableSelector.getSelectedItem();
                if (selectedTable.equals("Преподаватель")) {
	                if (selectedChair != null) {
	                	if (selectedChair.getId() == 0) {
	                        // Если выбрано "ВСЕ", отображаем всех преподавателей
	                        DAO.getAllFromTable("Преподаватель");
	                    } else {
	                        // Иначе отображаем преподавателей, связанных с выбранной кафедрой
	                        int chairId = selectedChair.getId();
	                        DAO.getTeachersFromChair(chairId);
	                    }
	                }
                } else {
                	if (selectedChair != null) {
                        int chairId = selectedChair.getId();
                        DAO.getStaffingScheduleByChair(chairId);
                	}
                }
            }
        });

        surnameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String id = surnameField.getText().trim();
                    if (!id.isEmpty()) {
                        DAO.searchTeacher(id);
                    }
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = surnameField.getText().trim();
                if (!id.isEmpty()) {
                	DAO.searchTeacher(id);
                }
            }
        });
        
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String selectedTable = (String) tableSelector.getSelectedItem();
                Chair selectedChairComboBox = (Chair) mainChairComboBox.getSelectedItem();
	            if (selectedTable == "Преподаватель") {
	            	boolean validInput = false;

	                // Создаем компоненты ввода данных вне цикла
	                JPanel panel = new JPanel(new GridLayout(9, 2));
	                JComboBox<Chair> chairComboBox = new JComboBox<>();
	                JComboBox<Post> postComboBox = new JComboBox<>();
	                JTextField surnameField = new JTextField();
	                JTextField firstNameField = new JTextField();
	                JTextField patronymicField = new JTextField();
	                JTextField dateOfBirthField = new JTextField();
	                JTextField innField = new JTextField();
	                JTextField snilsField = new JTextField();

	                List<Chair> chairs = DAO.listChairs();
	                for (Chair chair : chairs) {
	                    chairComboBox.addItem(chair);
	                }

	                // Устанавливаем выбранную кафедру, которая соответствует информации о кафедре из ComboBox
	                for (Chair chair : chairs) {
	                    if (chair.getNameChair().equals(selectedChairComboBox.toString())) {
	                        chairComboBox.setSelectedItem(chair);
	                        break;
	                    }
	                }

	                List<Post> posts = DAO.listPosts();
	                for (Post post : posts) {
	                    postComboBox.addItem(post);
	                }

	                panel.add(new JLabel("Кафедра:"));
	                panel.add(chairComboBox);
	                panel.add(new JLabel("Должность:"));
	                panel.add(postComboBox);
	                panel.add(new JLabel("Фамилия:"));
	                panel.add(surnameField);
	                panel.add(new JLabel("Имя:"));
	                panel.add(firstNameField);
	                panel.add(new JLabel("Отчество:"));
	                panel.add(patronymicField);
	                panel.add(new JLabel("Дата рождения (ГГГГ-ММ-ДД):"));
	                panel.add(dateOfBirthField);
	                panel.add(new JLabel("ИНН:"));
	                panel.add(innField);
	                panel.add(new JLabel("СНИЛС:"));
	                panel.add(snilsField);

	                while (!validInput) {
	                    // Отображаем диалоговое окно для ввода данных
	                    int result = JOptionPane.showConfirmDialog(null, panel, "Добавление записи", JOptionPane.OK_CANCEL_OPTION);
	                    if (result == JOptionPane.OK_OPTION) {
	                        // Получаем введенные значения
	                        Chair selectedChair = (Chair) chairComboBox.getSelectedItem();
	                        Post selectedPost = (Post) postComboBox.getSelectedItem();
	                        String surname = surnameField.getText();
	                        String firstName = firstNameField.getText();
	                        String patronymic = patronymicField.getText();
	                        String dateOfBirth = dateOfBirthField.getText();
	                        String inn = innField.getText();
	                        String snils = snilsField.getText();

	                        // Проверка на пустые поля
	                        if (surname.isEmpty() || firstName.isEmpty() || patronymic.isEmpty() || dateOfBirth.isEmpty() || inn.isEmpty() || snils.isEmpty()) {
	                            JOptionPane.showMessageDialog(null, "Все поля должны быть заполнены.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                            continue;
	                        }
	                        
	                        // Проверка корректности даты
	                        java.sql.Date sqlDateOfBirth;
	                        try {
	                            sqlDateOfBirth = java.sql.Date.valueOf(dateOfBirth);
	                        } catch (IllegalArgumentException ex) {
	                            JOptionPane.showMessageDialog(null, "Некорректная дата. Формат должен быть ГГГГ-ММ-ДД.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                            continue;
	                        }

	                        // Проверка ИНН и СНИЛС
	                        if (inn.length() != 12) {
	                            JOptionPane.showMessageDialog(null, "ИНН должен содержать 12 цифр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                            continue;
	                        }
	                        if (snils.length() != 11) {
	                            JOptionPane.showMessageDialog(null, "СНИЛС должен содержать 11 цифр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                            continue;
	                        }
	                        
	                        // Проверка на количество людей на должностях
	                        int currentCount = DAO.getCountOfTeachersByChairAndPost(selectedChair.getId(), selectedPost.getId());
	                        int requiredCount = DAO.getRequiredCountByChairAndPost(selectedChair.getId(), selectedPost.getId());
	                        if (currentCount >= requiredCount) {
	                            JOptionPane.showMessageDialog(null, "Невозможно добавить преподавателя. Достигнуто максимальное количество сотрудников на данной должности.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                            continue;
	                        }
	                        // Проверка на дублирование ИНН
	                        if (DAO.isInnExists(inn)) {
	                            JOptionPane.showMessageDialog(null, "Преподаватель с таким ИНН уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                            continue;
	                        }

	                        // Создаем объект Teacher
	                        Teacher teacher = new Teacher();
	                        teacher.setIdChair(selectedChair.getId());
	                        teacher.setIdPost(selectedPost.getId());
	                        teacher.setSurname(surname);
	                        teacher.setFirstName(firstName);
	                        teacher.setPatronymic(patronymic);
	                        teacher.setDateOfBirth(sqlDateOfBirth);
	                        teacher.setInn(Long.parseLong(inn));
	                        teacher.setSnils(Long.parseLong(snils));

	                        // Вызываем метод insertTeacher класса TeacherDAO, передавая объект Teacher
	                        try {
	                            DAO.insertTeacher(teacher);
	                            int chairId = selectedChair.getId();
	                            DAO.getTeachersFromChair(chairId);
	                            // Обновляем главный comboBox на выбранную кафедру
	                            for (int i = 0; i < mainChairComboBox.getItemCount(); i++) {
	                                if (mainChairComboBox.getItemAt(i).getId() == chairId) {
	                                    mainChairComboBox.setSelectedIndex(i);
	                                    break;
	                                }
	                            }
	                            JOptionPane.showMessageDialog(null, "Преподаватель успешно добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
	                            validInput = true;
	                        } catch (SQLException ex) {
	                            JOptionPane.showMessageDialog(null, "Ошибка при добавлении преподавателя: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
	                            ex.printStackTrace();
	                        }
	                    } else {
	                        validInput = true;  // Пользователь нажал отмену
	                    }
	                }
	                
	             }else if (selectedTable == "Штатное расписание") {
	            	 boolean validInput = false;

	                 // Создаем компоненты ввода данных вне цикла
	                 JPanel panel = new JPanel(new GridLayout(3, 2));
	                 JComboBox<Chair> chairComboBox = new JComboBox<>();
	                 JComboBox<Post> postComboBox = new JComboBox<>();
	                 JTextField requiredCountField = new JTextField();

	                 List<Chair> chairs = DAO.listChairs();
	                 for (Chair chair : chairs) {
	                     chairComboBox.addItem(chair);
	                 }

	                 // Устанавливаем выбранную кафедру, которая соответствует информации о кафедре из ComboBox
	                 for (Chair chair : chairs) {
	                     if (chair.getNameChair().equals(selectedChairComboBox.toString())) {
	                         chairComboBox.setSelectedItem(chair);
	                         break;
	                     }
	                 }

	                 List<Post> posts = DAO.listPosts();
	                 for (Post post : posts) {
	                     postComboBox.addItem(post);
	                 }

	                 panel.add(new JLabel("Кафедра:"));
	                 panel.add(chairComboBox);
	                 panel.add(new JLabel("Должность:"));
	                 panel.add(postComboBox);
	                 panel.add(new JLabel("Необходимое количество:"));
	                 panel.add(requiredCountField);

	                 while (!validInput) {
	                     // Отображаем диалоговое окно для ввода данных
	                     int result = JOptionPane.showConfirmDialog(null, panel, "Добавление записи", JOptionPane.OK_CANCEL_OPTION);
	                     if (result == JOptionPane.OK_OPTION) {
	                         // Получаем введенные значения
	                         Chair selectedChair = (Chair) chairComboBox.getSelectedItem();
	                         Post selectedPost = (Post) postComboBox.getSelectedItem();
	                         String requiredCountStr = requiredCountField.getText();

	                         // Проверка на пустые поля
	                         if (requiredCountStr.isEmpty()) {
	                             JOptionPane.showMessageDialog(null, "Поле 'Необходимое количество' должно быть заполнено.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                             continue;
	                         }

	                         int requiredCount;
	                         try {
	                             requiredCount = Integer.parseInt(requiredCountStr);
	                             if (requiredCount < 1) {
	                                 throw new NumberFormatException();
	                             }
	                         } catch (NumberFormatException ex) {
	                             JOptionPane.showMessageDialog(null, "Поле 'Необходимое количество' должно быть положительным целым числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                             continue;
	                         }

	                         // Создаем объект StaffingSchedule
	                         StaffingSchedule staffingSchedule = new StaffingSchedule();
	                         staffingSchedule.setIdChair(selectedChair.getId());
	                         staffingSchedule.setIdPost(selectedPost.getId());
	                         staffingSchedule.setRequiredCount(requiredCount);

	                         // Вызываем метод insertStaffingSchedule класса DAO, передавая объект StaffingSchedule
	                         try {
	                             DAO.insertStaffingSchedule(staffingSchedule);
	                             int chairId = selectedChair.getId();
	                             DAO.getStaffingScheduleByChair(chairId);
	                             // Обновляем главный comboBox на выбранную кафедру
	                             for (int i = 0; i < mainChairComboBox.getItemCount(); i++) {
	                                 if (mainChairComboBox.getItemAt(i).getId() == chairId) {
	                                     mainChairComboBox.setSelectedIndex(i);
	                                     break;
	                                 }
	                             }
	                             JOptionPane.showMessageDialog(null, "Запись успешно добавлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
	                             validInput = true;
	                         } catch (SQLException ex) {
	                             JOptionPane.showMessageDialog(null, "Ошибка при добавлении записи: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
	                             ex.printStackTrace();
	                         }
	                     } else {
	                         validInput = true;  // Пользователь нажал отмену
	                     }
	                 }
	                    
	                }else if (selectedTable == "Факультет") {
	                	boolean validInput = false;

	                    // Создаем компоненты ввода данных вне цикла
	                    JPanel panel = new JPanel(new GridLayout(2, 2));
	                    JTextField nameFacultyField = new JTextField();
	                    JTextField shortNameFacultyField = new JTextField();

	                    panel.add(new JLabel("Название факультета:"));
	                    panel.add(nameFacultyField);
	                    panel.add(new JLabel("Сокращенное название факультета:"));
	                    panel.add(shortNameFacultyField);

	                    while (!validInput) {
	                        // Отображаем диалоговое окно для ввода данных
	                        int result = JOptionPane.showConfirmDialog(null, panel, "Добавление записи", JOptionPane.OK_CANCEL_OPTION);
	                        if (result == JOptionPane.OK_OPTION) {
	                            // Получаем введенные значения
	                            String nameFaculty = nameFacultyField.getText();
	                            String shortNameFaculty = shortNameFacultyField.getText();

	                            // Проверка на пустые поля
	                            if (nameFaculty.isEmpty() || shortNameFaculty.isEmpty()) {
	                                JOptionPane.showMessageDialog(null, "Все поля должны быть заполнены.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                                continue;
	                            }

	                            // Создаем объект Faculty
	                            Faculty faculty = new Faculty();
	                            faculty.setNameFaculty(nameFaculty);
	                            faculty.setShortNameFaculty(shortNameFaculty);

	                            // Вызываем метод insertFaculty класса FacultyDAO, передавая объект Faculty
	                            try {
	                                DAO.insertFaculty(faculty);
	                                DAO.getAllFromTable(selectedTable);
	                                JOptionPane.showMessageDialog(null, "Запись успешно добавлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
	                                validInput = true;
	                            } catch (SQLException ex) {
	                                JOptionPane.showMessageDialog(null, "Ошибка при добавлении записи: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
	                                ex.printStackTrace();
	                            }
	                        } else {
	                            validInput = true;  // Пользователь нажал отмену
	                        }
	                    }
		            } else if (selectedTable == "Кафедра") {
		            	Faculty selectedFacultyComboBox = (Faculty) mainFacultyComboBox.getSelectedItem();
		            	boolean validInput = false;

		                // Создаем компоненты ввода данных вне цикла
		                JPanel panel = new JPanel(new GridLayout(3, 2));
		                JComboBox<Faculty> facultyComboBox = new JComboBox<>();
		                JTextField nameChairField = new JTextField();
		                JTextField shortNameChairField = new JTextField();

		                List<Faculty> faculties = DAO.listFaculty();
		                for (Faculty faculty : faculties) {
		                    facultyComboBox.addItem(faculty);
		                }
		                
		                // Устанавливаем выбранную кафедру, которая соответствует информации о кафедре из ComboBox
		                for (Faculty faculty : faculties) {
		                    if (faculty.getNameFaculty().equals(selectedFacultyComboBox.toString())) {
		                    	facultyComboBox.setSelectedItem(faculty);
		                        break;
		                    }
		                }

		                panel.add(new JLabel("Факультет:"));
		                panel.add(facultyComboBox);
		                panel.add(new JLabel("Название кафедры:"));
		                panel.add(nameChairField);
		                panel.add(new JLabel("Сокращенное название кафедры:"));
		                panel.add(shortNameChairField);

		                while (!validInput) {
		                    // Отображаем диалоговое окно для ввода данных
		                    int result = JOptionPane.showConfirmDialog(null, panel, "Добавление записи", JOptionPane.OK_CANCEL_OPTION);
		                    if (result == JOptionPane.OK_OPTION) {
		                        // Получаем введенные значения
		                        Faculty selectedFaculty = (Faculty) facultyComboBox.getSelectedItem();
		                        int idFaculty = selectedFaculty.getId();
		                        String nameChair = nameChairField.getText();
		                        String shortNameChair = shortNameChairField.getText();

		                        // Проверка на пустые поля
		                        if (nameChair.isEmpty() || shortNameChair.isEmpty()) {
		                            JOptionPane.showMessageDialog(null, "Все поля должны быть заполнены.", "Ошибка", JOptionPane.ERROR_MESSAGE);
		                            continue;
		                        }

		                        // Создаем объект Chair
		                        Chair chair = new Chair(idFaculty, nameChair, shortNameChair);

		                        // Вызываем метод insertChair класса DAO, передавая объект Chair
		                        try {
		                            DAO.insertChair(chair);
		                            DAO.getChairByFaculty(idFaculty);
		                            // Обновляем главный comboBox на выбранную кафедру
		                            for (int i = 0; i < mainFacultyComboBox.getItemCount(); i++) {
		                                if (mainFacultyComboBox.getItemAt(i).getId() == idFaculty) {
		                                    mainFacultyComboBox.setSelectedIndex(i);
		                                    break;
		                                }
		                            }
		                            JOptionPane.showMessageDialog(null, "Запись успешно добавлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
		                            validInput = true;
		                        } catch (SQLException ex) {
		                            JOptionPane.showMessageDialog(null, "Ошибка при добавлении записи: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
		                            ex.printStackTrace();
		                        }
		                    } else {
		                        validInput = true;  // Пользователь нажал отмену
		                    }
		                }
	                } else {
	                	// Создаем компоненты ввода данных
	                    JPanel panel = new JPanel(new GridLayout(1, 2));
	                    JTextField namePostField = new JTextField();

	                    panel.add(new JLabel("Название должности:"));
	                    panel.add(namePostField);

	                    // Отображаем диалоговое окно для ввода данных
	                    int result = JOptionPane.showConfirmDialog(null, panel, "Добавление записи", JOptionPane.OK_CANCEL_OPTION);
	                    if (result == JOptionPane.OK_OPTION) {
	                        // Получаем введенные значения
	                        String namePost = namePostField.getText();

	                        // Проверка на пустое значение поля
	                        if (namePost.isEmpty()) {
	                            JOptionPane.showMessageDialog(null, "Поле 'Название должности' должно быть заполнено.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                            return;
	                        }

	                        // Создаем объект Post
	                        Post post = new Post(namePost);

	                        // Вызываем метод insertPost класса PostDAO, передавая объект Post
	                        try {
	                            DAO.insertPost(post);
	                            DAO.getAllFromTable(selectedTable);
	                            JOptionPane.showMessageDialog(null, "Запись успешно добавлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
	                        } catch (SQLException ex) {
	                            JOptionPane.showMessageDialog(null, "Ошибка при добавлении записи: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
	                            ex.printStackTrace();
	                        }
	                    }
	                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String selectedTable = (String) tableSelector.getSelectedItem();
	            if (selectedTable == "Преподаватель") {
	            	// Получаем данные выбранной строки таблицы и открываем форму для редактирования
	                int selectedRow = table.getSelectedRow();
	                if (selectedRow != -1) {
	                    boolean validInput = false;

	                    // Создаем компоненты ввода данных вне цикла
	                    JPanel panel = new JPanel(new GridLayout(9, 2));
	                    JComboBox<Chair> chairComboBox = new JComboBox<>();
	                    JComboBox<Post> postComboBox = new JComboBox<>();
	                    JTextField surnameField = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());
	                    JTextField firstNameField = new JTextField(tableModel.getValueAt(selectedRow, 5).toString());
	                    JTextField patronymicField = new JTextField(tableModel.getValueAt(selectedRow, 6).toString());
	                    JTextField dateOfBirthField = new JTextField(tableModel.getValueAt(selectedRow, 7).toString());
	                    JTextField innField = new JTextField(tableModel.getValueAt(selectedRow, 8).toString());
	                    JTextField snilsField = new JTextField(tableModel.getValueAt(selectedRow, 9).toString());

	                    List<Chair> chairs = DAO.listChairs();
	                    List<Post> posts = DAO.listPosts();

	                    for (Chair chair : chairs) {
	                        chairComboBox.addItem(chair);
	                    }

	                    for (Post post : posts) {
	                        postComboBox.addItem(post);
	                    }
	                    // Устанавливаем выбранной кафедру, которая соответствует информации о кафедре из таблицы
	                    String shortNameChair = tableModel.getValueAt(selectedRow, 2).toString();
	                    for (Chair chair : chairs) {
	                        if (chair.getShortNameChair().equals(shortNameChair)) {
	                            chairComboBox.setSelectedItem(chair);
	                            break;
	                        }
	                    }
	                    // Устанавливаем выбранной должность, которая соответствует информации о должности из таблицы
	                    String postName = tableModel.getValueAt(selectedRow, 3).toString();
	                    for (Post post : posts) {
	                        if (post.getNamePost().equals(postName)) {
	                            postComboBox.setSelectedItem(post);
	                            break;
	                        }
	                    }

	                    panel.add(new JLabel("Кафедра:"));
	                    panel.add(chairComboBox);
	                    panel.add(new JLabel("Должность:"));
	                    panel.add(postComboBox);
	                    panel.add(new JLabel("Фамилия:"));
	                    panel.add(surnameField);
	                    panel.add(new JLabel("Имя:"));
	                    panel.add(firstNameField);
	                    panel.add(new JLabel("Отчество:"));
	                    panel.add(patronymicField);
	                    panel.add(new JLabel("Дата рождения (ГГГГ-ММ-ДД):"));
	                    panel.add(dateOfBirthField);
	                    panel.add(new JLabel("ИНН:"));
	                    panel.add(innField);
	                    panel.add(new JLabel("СНИЛС:"));
	                    panel.add(snilsField);

	                    while (!validInput) {
	                        // Отображаем диалоговое окно для ввода данных
	                        int result = JOptionPane.showConfirmDialog(null, panel, "Редактирование записи", JOptionPane.OK_CANCEL_OPTION);
	                        if (result == JOptionPane.OK_OPTION) {
	                            try {
	                                // Получаем введенные значения
	                                Chair selectedChair = (Chair) chairComboBox.getSelectedItem();
	                                Post selectedPost = (Post) postComboBox.getSelectedItem();
	                                int idChair = selectedChair.getId();
	                                int idPost = selectedPost.getId();
	                                String surname = surnameField.getText();
	                                String firstName = firstNameField.getText();
	                                String patronymic = patronymicField.getText();
	                                String dateOfBirth = dateOfBirthField.getText();
	                                String inn = innField.getText();
	                                String snils = snilsField.getText();

	                                // Проверка на пустые поля
	                                if (surname.isEmpty() || firstName.isEmpty() || patronymic.isEmpty() || dateOfBirth.isEmpty() || inn.isEmpty() || snils.isEmpty()) {
	                                    JOptionPane.showMessageDialog(null, "Все поля должны быть заполнены.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                                    continue;
	                                }

	                                // Проверка корректности даты
	                                java.sql.Date sqlDateOfBirth;
	                                try {
	                                    sqlDateOfBirth = java.sql.Date.valueOf(dateOfBirth);
	                                } catch (IllegalArgumentException ex) {
	                                    JOptionPane.showMessageDialog(null, "Некорректная дата. Формат должен быть ГГГГ-ММ-ДД.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                                    continue;
	                                }

	                                // Проверка ИНН и СНИЛС
	                                if (inn.length() != 12) {
	                                    JOptionPane.showMessageDialog(null, "ИНН должен содержать 12 цифр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                                    continue;
	                                }
	                                if (snils.length() != 11) {
	                                    JOptionPane.showMessageDialog(null, "СНИЛС должен содержать 11 цифр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                                    continue;
	                                }
	                                
	                                int currentTeacherId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());

	                                // Проверка на количество людей на должностях, исключая текущего преподавателя
	                                int currentCount = DAO.getCountOfTeachersByChairAndPostExcludingCurrent(selectedChair.getId(), selectedPost.getId(), currentTeacherId);
	                                int requiredCount = DAO.getRequiredCountByChairAndPost(selectedChair.getId(), selectedPost.getId());
	                                if (currentCount >= requiredCount) {
	                                    JOptionPane.showMessageDialog(null, "Невозможно добавить преподавателя. Достигнуто максимальное количество сотрудников на данной должности.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                                    continue;
	                                }

	                                // Проверка на дублирование ИНН, кроме текущего преподавателя
	                                if (DAO.isInnExistsExceptCurrent(inn, currentTeacherId)) {
	                                    JOptionPane.showMessageDialog(null, "Преподаватель с таким ИНН уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
	                                    continue;
	                                }

	                                // Создаем объект Teacher
	                                Teacher teacher = new Teacher();
	                                teacher.setId(currentTeacherId);
	                                teacher.setIdChair(idChair);
	                                teacher.setIdPost(idPost);
	                                teacher.setSurname(surname);
	                                teacher.setFirstName(firstName);
	                                teacher.setPatronymic(patronymic);
	                                teacher.setDateOfBirth(sqlDateOfBirth);
	                                teacher.setInn(Long.parseLong(inn));
	                                teacher.setSnils(Long.parseLong(snils));

	                                // Вызываем метод updateTeacher класса DAO, передавая объект Teacher
	                                DAO.updateTeacher(teacher);
	                                int chairId = selectedChair.getId();
	                                DAO.getTeachersFromChair(chairId);
	                                // Обновляем главный comboBox на выбранную кафедру
	                                for (int i = 0; i < mainChairComboBox.getItemCount(); i++) {
	                                    if (mainChairComboBox.getItemAt(i).getId() == chairId) {
	                                        mainChairComboBox.setSelectedIndex(i);
	                                        break;
	                                    }
	                                }
	                                JOptionPane.showMessageDialog(null, "Преподаватель успешно обновлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
	                                validInput = true;
	                            } catch (SQLException ex) {
	                                JOptionPane.showMessageDialog(null, "Ошибка при обновлении преподавателя: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
	                                ex.printStackTrace();
	                            }
	                        } else {
	                            validInput = true;  // Пользователь нажал отмену
	                        }
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(null, "Выберите запись для редактирования");
	                }
		                
	            }else if (selectedTable == "Факультет") {
	                // Получаем данные выбранной строки таблицы и открываем форму для редактирования
	                int selectedRow = table.getSelectedRow();
	                if (selectedRow != -1) {
	                	// Создаем панель для ввода данных
		                    JPanel panel = new JPanel(new GridLayout(2, 2));
		                    JTextField nameFacultyField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());
		                    JTextField shortNameFacultyField = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());
		                    panel.add(new JLabel("Название факультета:"));
		                    panel.add(nameFacultyField);
		                    panel.add(new JLabel("Сокращенное название факультета:"));
		                    panel.add(shortNameFacultyField);
		                    
		                    
		                    // Отображаем диалоговое окно для ввода данных
		                    int result = JOptionPane.showConfirmDialog(null, panel, "Редактирование записи", JOptionPane.OK_CANCEL_OPTION);
		                    if (result == JOptionPane.OK_OPTION) {
		                        String nameFaculty = nameFacultyField.getText();
								String shortNameFaculty = shortNameFacultyField.getText();
								

								// Создаем объект Faculty
								Faculty faculty = new Faculty();
								faculty.setId(Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString()));
								faculty.setNameFaculty(nameFaculty);
								faculty.setShortNameFaculty(shortNameFaculty);

								// Вызываем метод updateFaculty класса FacultyDAO, передавая объект Faculty
								DAO.updateFaculty(faculty);
								DAO.getAllFromTable(selectedTable);
								JOptionPane.showMessageDialog(null, "Запись успешно отредактирована!", "Успех", JOptionPane.INFORMATION_MESSAGE);
		                    }
	                } else {
	                    JOptionPane.showMessageDialog(null, "Выберите запись для редактирования");
	                }
	            } else if (selectedTable == "Штатное расписание") {
	                // Получаем данные выбранной строки таблицы и открываем форму для редактирования
	                int selectedRow = table.getSelectedRow();
	                if (selectedRow != -1) {
	                    // Создаем панель для ввода данных
	                    JPanel panel = new JPanel(new GridLayout(3, 2));
	                    JComboBox<Chair> chairComboBox = new JComboBox<>();
	                    JComboBox<Post> postComboBox = new JComboBox<>();
	                    JTextField requiredCountField = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());

	                    // Заполняем выпадающий список кафедр
	                    List<Chair> chairs = DAO.listChairs();
	                    for (Chair chair : chairs) {
	                        chairComboBox.addItem(chair);
	                    }

	                    // Заполняем выпадающий список должностей
	                    List<Post> posts = DAO.listPosts();
	                    for (Post post : posts) {
	                        postComboBox.addItem(post);
	                    }

	                    // Устанавливаем выбранные кафедру и должность, которые соответствуют информации из таблицы
	                    String chairName = tableModel.getValueAt(selectedRow, 2).toString();
	                    for (Chair chair : chairs) {
	                        if (chair.getShortNameChair().equals(chairName)) {
	                            chairComboBox.setSelectedItem(chair);
	                            break;
	                        }
	                    }

	                    String postName = tableModel.getValueAt(selectedRow, 3).toString();
	                    for (Post post : posts) {
	                        if (post.getNamePost().equals(postName)) {
	                            postComboBox.setSelectedItem(post);
	                            break;
	                        }
	                    }

	                    panel.add(new JLabel("Кафедра:"));
	                    panel.add(chairComboBox);
	                    panel.add(new JLabel("Должность:"));
	                    panel.add(postComboBox);
	                    panel.add(new JLabel("Необходимое количество:"));
	                    panel.add(requiredCountField);

	                    // Отображаем диалоговое окно для ввода данных
	                    int result = JOptionPane.showConfirmDialog(null, panel, "Редактирование записи", JOptionPane.OK_CANCEL_OPTION);
	                    if (result == JOptionPane.OK_OPTION) {
	                        // Получаем введенные значения
	                        Chair selectedChair = (Chair) chairComboBox.getSelectedItem();
	                        Post selectedPost = (Post) postComboBox.getSelectedItem();
	                        int requiredCount = Integer.parseInt(requiredCountField.getText());

	                        // Создаем объект StaffingSchedule
	                        StaffingSchedule staffingSchedule = new StaffingSchedule();
	                        staffingSchedule.setIdChair(selectedChair.getId());
	                        staffingSchedule.setIdPost(selectedPost.getId());
	                        staffingSchedule.setRequiredCount(requiredCount);
	                        staffingSchedule.setId(Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString()));

	                        // Вызываем метод updateStaffingSchedule класса DAO, передавая объект StaffingSchedule
	                        DAO.updateStaffingSchedule(staffingSchedule);
	                        int chairId = selectedChair.getId();
	                        DAO.getStaffingScheduleByChair(chairId);
		                    for (int i = 0; i < mainChairComboBox.getItemCount(); i++) {
		                        if (mainChairComboBox.getItemAt(i).getId() == chairId) {
		                            mainChairComboBox.setSelectedIndex(i);
		                            break;
		                        }
		                    }
		                    JOptionPane.showMessageDialog(null, "Запись успешно отредактирована!", "Успех", JOptionPane.INFORMATION_MESSAGE);
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(null, "Выберите запись для редактирования");
	                }
	            } else if (selectedTable == "Кафедра") {
	                // Получаем данные выбранной строки таблицы и открываем форму для редактирования
	                int selectedRow = table.getSelectedRow();
	                if (selectedRow != -1) {
	                    // Создаем панель для ввода данных
	                    JPanel panel = new JPanel(new GridLayout(3, 2));
	                    JComboBox<Faculty> facultyComboBox = new JComboBox<>();
	                    JTextField nameChairField = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());
	                    JTextField shortNameChairField = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());

	                    // Заполняем выпадающий список факультетов
	                    List<Faculty> faculties = DAO.listFaculty();
	                    for (Faculty faculty : faculties) {
	                        facultyComboBox.addItem(faculty);
	                    }

	                    // Устанавливаем выбранным факультет, который соответствует информации о факультете из таблицы
	                    String facultyName = tableModel.getValueAt(selectedRow, 2).toString();
	                    for (Faculty faculty : faculties) {
	                        if (faculty.getNameFaculty().equals(facultyName)) {
	                            facultyComboBox.setSelectedItem(faculty);
	                            break;
	                        }
	                    }
	                    
	                    panel.add(new JLabel("Факультет:"));
	                    panel.add(facultyComboBox);
	                    panel.add(new JLabel("Название кафедры:"));
	                    panel.add(nameChairField);
	                    panel.add(new JLabel("Сокращенное название кафедры:"));
	                    panel.add(shortNameChairField);
	                    

	                    // Отображаем диалоговое окно для ввода данных
	                    int result = JOptionPane.showConfirmDialog(null, panel, "Редактирование записи", JOptionPane.OK_CANCEL_OPTION);
	                    if (result == JOptionPane.OK_OPTION) {
	                    	try {
		                        // Получаем введенные значения
		                    	Faculty selectedFaculty = (Faculty) facultyComboBox.getSelectedItem();
		                        int idFaculty = selectedFaculty.getId();
	
		                        String nameChair = nameChairField.getText();
		                        String shortNameChair = shortNameChairField.getText();
		                        
		                        // Создаем объект Chair
		                        Chair chair = new Chair(idFaculty, nameChair, shortNameChair);
		                        chair.setId(Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString()));
	
		                        // Вызываем метод updateChair класса DAO, передавая объект Chair
		                        DAO.updateChair(chair);

	                            DAO.getChairByFaculty(idFaculty);
	                            // Обновляем главный comboBox на выбранную кафедру
	                            for (int i = 0; i < mainFacultyComboBox.getItemCount(); i++) {
	                                if (mainFacultyComboBox.getItemAt(i).getId() == idFaculty) {
	                                    mainFacultyComboBox.setSelectedIndex(i);
	                                    break;
	                                }
	                            }
	                            JOptionPane.showMessageDialog(null, "Запись успешно отредактирована!", "Успех", JOptionPane.INFORMATION_MESSAGE);
		                        
	                    	} catch (NumberFormatException ex) {
	                            JOptionPane.showMessageDialog(null, "Ошибка в формате числовых данных: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
	                        }
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(null, "Выберите запись для редактирования");
	                }
	            }
	            else {
	            	// Получаем данные выбранной строки таблицы и открываем форму для редактирования
	                int selectedRow = table.getSelectedRow();
	                if (selectedRow != -1) {
	                    // Создаем панель для ввода данных
	                    JPanel panel = new JPanel(new GridLayout(1, 2));
	                    JTextField namePostField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());

	                    panel.add(new JLabel("Название должности:"));
	                    panel.add(namePostField);

	                    // Отображаем диалоговое окно для ввода данных
	                    int result = JOptionPane.showConfirmDialog(null, panel, "Редактирование записи", JOptionPane.OK_CANCEL_OPTION);
	                    if (result == JOptionPane.OK_OPTION) {
	                        // Получаем введенные значения
	                        String namePost = namePostField.getText();

	                        // Создаем объект Post
	                        Post post = new Post(namePost);
	                        post.setId(Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()));

	                        // Вызываем метод updatePost класса PostDAO, передавая объект Post
	                        DAO.updatePost(post);
	                        DAO.getAllFromTable(selectedTable);
	                        JOptionPane.showMessageDialog(null, "Запись успешно отредактирована!", "Успех", JOptionPane.INFORMATION_MESSAGE);
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(null, "Выберите запись для редактирования");
	                }
	            }
	         }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTable = (String) tableSelector.getSelectedItem();
                Chair selectedChairComboBox = (Chair) mainChairComboBox.getSelectedItem();
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmation = JOptionPane.showConfirmDialog(null, "Вы уверены, что хотите удалить выбранную запись?", "Подтверждение удаления", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        // Определяем имя таблицы и ID для удаления
                        String tableName = "";
                        int id = 0;
                        if (selectedTable.equals("Преподаватель")) {
                            tableName = "Teacher";
                            id = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                        } else if (selectedTable.equals("Штатное расписание")) {
                            tableName = "StaffingSchedule";
                            id = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                        } else if (selectedTable.equals("Факультет")) {
                            tableName = "Faculty";
                            id = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                        } else if (selectedTable.equals("Кафедра")) {
                            tableName = "Chair";
                            id = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                        } else {
                            tableName = "Post";
                            id = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                        }

                        // Проверка на наличие зависимых записей
                        if (DAO.hasDependentRecords(id, tableName)) {
                            int cascadeConfirmation = JOptionPane.showConfirmDialog(null, "Эта запись имеет зависимые записи, которые будут также удалены. Вы уверены, что хотите продолжить?", "Подтверждение каскадного удаления", JOptionPane.YES_NO_OPTION);
                            if (cascadeConfirmation != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }

                        DAO.deleteButton(id, tableName);
                        int chairId = selectedChairComboBox.getId();

                        if (selectedTable.equals("Преподаватель")) {
                            if (selectedChairComboBox.getId() == 0) { // Если выбрано "ВСЕ"
                                DAO.getAllFromTable(selectedTable);
                            } else {
                                DAO.getTeachersFromChair(chairId);
                            }
                        } else if (selectedTable.equals("Штатное расписание")) {
                            DAO.getStaffingScheduleByChair(chairId);
                        } else {
                            DAO.getAllFromTable(selectedTable);
                        }
                        JOptionPane.showMessageDialog(null, "Запись успешно удалена");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите запись для удаления");
                }
            }
        });
        
        frame.setVisible(true);

    }
}

