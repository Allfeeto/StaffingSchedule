import java.sql.Date;

public class Teacher {
    private int id;
    private int idChair;
    private int idPost;
    private String surname;
    private String firstName;
    private String patronymic;
    private Date dateOfBirth;
    private int passportSeries;
    private int passportNumber;
    private Date passportIssueDate;
    private String passportIssuedBy;
    private String passportDivisionCode;
    private String addressRegistration;
    private long inn;
    private long snils;
    private String educationDocument;
    private String academicTitleDocument;
    private String birthCertificatesOfChildren;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdChair() {
        return idChair;
    }

    public void setIdChair(int idChair) {
        this.idChair = idChair;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(int passportSeries) {
        this.passportSeries = passportSeries;
    }

    public int getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(int passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Date getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(Date passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public String getPassportIssuedBy() {
        return passportIssuedBy;
    }

    public void setPassportIssuedBy(String passportIssuedBy) {
        this.passportIssuedBy = passportIssuedBy;
    }

    public String getPassportDivisionCode() {
        return passportDivisionCode;
    }

    public void setPassportDivisionCode(String passportDivisionCode) {
        this.passportDivisionCode = passportDivisionCode;
    }

    public String getAddressRegistration() {
        return addressRegistration;
    }

    public void setAddressRegistration(String addressRegistration) {
        this.addressRegistration = addressRegistration;
    }

    public long getInn() {
        return inn;
    }

    public void setInn(long inn) {
        this.inn = inn;
    }

    public long getSnils() {
        return snils;
    }

    public void setSnils(long snils) {
        this.snils = snils;
    }

    public String getEducationDocument() {
        return educationDocument;
    }

    public void setEducationDocument(String educationDocument) {
        this.educationDocument = educationDocument;
    }

    public String getAcademicTitleDocument() {
        return academicTitleDocument;
    }

    public void setAcademicTitleDocument(String academicTitleDocument) {
        this.academicTitleDocument = academicTitleDocument;
    }

    public String getBirthCertificatesOfChildren() {
        return birthCertificatesOfChildren;
    }

    public void setBirthCertificatesOfChildren(String birthCertificatesOfChildren) {
        this.birthCertificatesOfChildren = birthCertificatesOfChildren;
    }
}
