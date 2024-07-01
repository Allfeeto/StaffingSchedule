public class Post {
    private int id;
    private String namePost;

    // Конструкторы
    public Post() {
    }

    public Post(String namePost) {
        this.namePost = namePost;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamePost() {
        return namePost;
    }

    public void setNamePost(String namePost) {
        this.namePost = namePost;
    }

    @Override
    public String toString() {
        return namePost;
    }
}
