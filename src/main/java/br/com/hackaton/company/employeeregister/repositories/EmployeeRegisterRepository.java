package br.com.hackaton.company.employeeregister.repositories;

import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRegisterRepository extends JpaRepository<EmployeeRegisterModel, Long> {


}
