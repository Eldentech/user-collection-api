package com.eldentech.user.domain.enity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "userdata")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "country_code", nullable = false)
    private String country;

    @Column(name="date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    public User() {
    }

    public User(String name, Sex sex, int age, String country) {
        this(null, name, sex, age, country, null);
    }
    public User(String id, String name, Sex sex, int age, String country, LocalDateTime dateCreated) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.country = country;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getAge() == user.getAge()
                && Objects.equals(getId(), user.getId())
                && Objects.equals(getName(), user.getName())
                && getSex() == user.getSex()
                && Objects.equals(getCountry(), user.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSex(), getAge(), getCountry());
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", country='" + country + '\'' +
                ", created='" + dateCreated + '\'' +
                '}';
    }
}
