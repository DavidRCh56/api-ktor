package com.domain.usecase

import com.domain.models.Employee
import com.domain.models.UpdateEmployee
import com.domain.repository.EmployeeInterface

class UpdateEmployeeUseCase (val repository : EmployeeInterface){
    var updateEmployee: UpdateEmployee? = null
    var dni: String? = null

    operator fun invoke() : Boolean {
        return if (updateEmployee == null || dni == null) {
            false
        }else{
            return repository.updateEmployee(updateEmployee!!, dni!!)
        }

    }
}