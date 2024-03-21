package br.com.hackaton.company.employeeregister.repositories;

import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRegisterRepository extends JpaRepository<EmployeeRegisterModel, Long> {

    @Query("SELECT e FROM EmployeeRegisterModel e WHERE e.registracionCode = :registracionCode")
    List<EmployeeRegisterModel> findByRegistracionCode(String registracionCode);

}
