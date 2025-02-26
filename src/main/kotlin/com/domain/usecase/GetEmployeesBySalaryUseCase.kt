package com.domain.usecase

import com.domain.models.Employee
import com.domain.models.Salary
import com.domain.repository.EmployeeInterface

class GetEmployeesBySalaryUseCase (val repository : EmployeeInterface) {
    var filter : Salary? =null

    operator fun invoke() : List<Employee> {
        return filter?.let {
             repository.getEmployeeBySalary(it)
        }?:run{
             emptyList()  //lista vacía
        }
    }
}