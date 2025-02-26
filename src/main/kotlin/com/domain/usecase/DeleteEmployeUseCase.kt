package com.domain.usecase

import com.domain.repository.EmployeeInterface

class DeleteEmployeUseCase (val repository : EmployeeInterface){
    var dni : String? = null

    operator fun invoke() : Boolean {
        return if (dni == null) {
            false
        }else{
            return repository.deleteEmployee(dni!!)
        }

    }
}