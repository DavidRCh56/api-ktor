package com.data.repository

import com.domain.models.Employee
import com.domain.models.Salary
import com.domain.models.UpdateEmployee
import com.domain.repository.EmployeeInterface


class DatabaseEmployeeRepository : EmployeeInterface {
    override fun getAllEmployee(): List<Employee> {
        TODO("Not yet implemented")
    }

    override fun getEmployeeBySalary(salary: Salary): List< Employee >{
        TODO("Not yet implemented")
    }

    override fun getEmployeeByName(name: String): List<Employee> {
        TODO("Not yet implemented")
    }

    override fun getEmployeeByDni(id: String): Employee ? {
        TODO("Not yet implemented")
    }

    override fun postEmployee(employee: Employee) : Boolean {
        TODO("Not yet implemented")
    }

    override fun updateEmployee(employee: UpdateEmployee, dni:String) : Boolean{
        TODO("Not yet implemented")
    }

    override fun deleteEmployee(dni: String): Boolean {
        TODO("Not yet implemented")
    }
}