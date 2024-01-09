package ba.edu.ibu.javafx.models;

public class Student {
    private int id;
    private String name;
    private String surname;
    private String year;
    private String cycle;

    public Student(int id, String name, String surname, String year, String cycle) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.year = year;
        this.cycle = cycle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }
}
