package com.domain.repository

import com.domain.models.Employee
import com.domain.models.Salary
import com.domain.models.UpdateEmployee


interface EmployeeInterface {
    fun getAllEmployee () : List <Employee>

    fun getEmployeeBySalary ( salary: Salary) : List<Employee>

    fun getEmployeeByName ( name : String) : List<Employee>

    //Debe ser nullable, por si no existe.
    fun getEmployeeByDni (dni: String) : Employee ?

    fun postEmployee(employee: Employee) : Boolean

    fun updateEmployee(employee: UpdateEmployee, dni:String) : Boolean

    fun deleteEmployee(dni : String) : Boolean
}