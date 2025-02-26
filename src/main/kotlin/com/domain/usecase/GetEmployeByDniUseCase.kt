package com.domain.usecase

import com.domain.models.Employee
import com.domain.repository.EmployeeInterface

class GetEmployeByDniUseCase (val repository : EmployeeInterface) {
    var dni : String? = null


    operator fun invoke() : Employee? {
        return if (dni?.isNullOrBlank() == true)
                null
            else{
                repository.getEmployeeByDni(dni!!)
        }
    }
}