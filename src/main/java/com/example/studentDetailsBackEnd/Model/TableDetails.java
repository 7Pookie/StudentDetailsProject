package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;


@Entity
@Table(name = "table_details")
public class TableDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tableID;

    @Column(name = "table_name", nullable = false, unique = true)
    private String tableName;

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
