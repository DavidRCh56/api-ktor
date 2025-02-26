package com.data.models

import com.domain.models.Employee
import com.domain.models.Salary


/*
Simulamos nuestro repositorio de datos. Aquí tendremos la lista de datos.
Lo tendremos para la incorporación de los test.
 */
object EmployeeData  {
    val listEmployee = mutableListOf<Employee> (
        Employee("Sonia", "23456789B", "A project manager", Salary.MEDIUM),
        Employee("Guille", "34567890C", "A designer", Salary.LOW),
        Employee("Diego", "45678901D", "A data analyst", Salary.MEDIUM),
        Employee("Santiago", "23344334Z", "A backend developer", Salary.HIGH),
        Employee("María", "67890123F", "A UX/UI designer", Salary.MEDIUM),
        Employee("Carlos", "78901234G", "A mobile developer", Salary.LOW),
        Employee("Laura", "89012345H", "A product owner", Salary.MEDIUM),
        Employee("Luis", "90123456I", "A DevOps engineer", Salary.HIGH),
        Employee("Ana", "01234567J", "A frontend developer", Salary.MEDIUM),
        Employee("Pablo", "11223344K", "A QA engineer", Salary.LOW),
        Employee("Carmen", "22334455L", "A scrum master", Salary.MEDIUM),
        Employee("Javier", "33445566M", "A technical writer", Salary.LOW),
        Employee("Marta", "44556677N", "An HR specialist", Salary.MEDIUM)
    )

}