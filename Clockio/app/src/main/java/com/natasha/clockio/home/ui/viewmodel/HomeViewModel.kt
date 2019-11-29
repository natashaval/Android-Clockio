package com.natasha.clockio.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.natasha.clockio.home.repository.EmployeeRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val employeeRepository: EmployeeRepository): ViewModel() {

}