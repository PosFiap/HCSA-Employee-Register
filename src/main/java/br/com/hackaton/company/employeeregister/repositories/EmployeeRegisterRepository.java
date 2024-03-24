package br.com.hackaton.company.employeeregister.repositories;

import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRegisterRepository extends JpaRepository<EmployeeRegisterModel, Long> {

    @Query("SELECT e FROM EmployeeRegisterModel e WHERE e.registracionCode = :registracionCode AND MONTH(e.timeRegister) = :previousMonth")
    List<EmployeeRegisterModel> findByRegistracionCode(String registracionCode, int previousMonth);

    @Query("SELECT e FROM EmployeeRegisterModel e WHERE e.registracionCode = :registracionCode AND YEAR(e.timeRegister) = :year AND MONTH(e.timeRegister) = :month AND DAY(e.timeRegister) = :day")
    List<EmployeeRegisterModel> findByRegistracionCodeAndDate(String registracionCode, int year, int month, int day);

}
