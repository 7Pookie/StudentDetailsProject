package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.TableDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableDetailsRepository extends JpaRepository<TableDetails, Integer> {
    TableDetails findByTableName(String tableName);
}
