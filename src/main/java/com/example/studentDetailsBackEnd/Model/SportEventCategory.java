package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "sports_category")
public class SportEventCategory {

    @Id
    @Column(name = "sport_categoryid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sportCategoryID;

    @Column(name = "sport_category_name",nullable = false, unique = true)
    private String sportCategoryName;


    public SportEventCategory() {
        
    }

    public SportEventCategory(String category) {
        this.sportCategoryName = category;
    }

    public String getSportEventCategoryName() {
        return sportCategoryName;
    }

    public void setSportEventCategoryName(String name) {
        this.sportCategoryName = name;
    }

    public Object getSportEventCategoryID() {
       return sportCategoryID;
    }

}
