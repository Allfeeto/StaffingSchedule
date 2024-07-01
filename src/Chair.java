

public class Chair {
    private int id;
    private int idFaculty;
    private String nameChair;
    private String shortNameChair;

    // Конструкторы
    public Chair() {
    }

    public Chair(int idFaculty, String nameChair, String shortNameChair) {
        this.idFaculty = idFaculty;
        this.nameChair = nameChair;
        this.shortNameChair = shortNameChair;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFaculty() {
        return idFaculty;
    }

    public void setIdFaculty(int idFaculty) {
        this.idFaculty = idFaculty;
    }

    public String getNameChair() {
        return nameChair;
    }

    public void setNameChair(String nameChair) {
        this.nameChair = nameChair;
    }

    public String getShortNameChair() {
        return shortNameChair;
    }

    public void setShortNameChair(String shortNameChair) {
        this.shortNameChair = shortNameChair;
    }
    @Override
    public String toString() {
        return nameChair;
    }
}


