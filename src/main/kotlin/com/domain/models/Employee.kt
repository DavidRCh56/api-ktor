package com.domain.models
import kotlinx.serialization.Serializable

enum class Salary {
    LOW, MEDIUM, HIGH
}

@Serializable
data class Employee(
    val name : String,
    val dni : String,
    val description: String,
    val salary: Salary
)


/*

Me daba un problema de incompatibilidad de librería y he tenido que cambiar la versión de kotlin a la 1.9.0

Necesitamos serializar los objetos kotlin en datos en JSON y viceversa.

Serializar:
    val employee = Employee("Alice", "Team leader", Salary.High)
    val json = Json.encodeToString(employee)
    println(json) // {"name":"Alice","description":"Team leader","salary":"High"}

Deserialización: Utilizamos """ para incluir caracteres especiales como la " comilla.
    val json = """{"name":"Alice","description":"Team leader","salary":"High"}"""
    val employee = Json.decodeFromString<Employee>(json)
    println(employee) // Employee(name=Alice, description=Team leader, salary=High)


 */
