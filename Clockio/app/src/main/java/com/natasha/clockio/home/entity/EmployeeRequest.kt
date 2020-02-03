package com.natasha.clockio.home.entity

data class EmployeeRequest (
    var firstName: String,
    var lastName: String?,
    var phone: String,
    var email: String,
    var profileUrl: String?,
    var departmentId: String,
    var userId: String?
)