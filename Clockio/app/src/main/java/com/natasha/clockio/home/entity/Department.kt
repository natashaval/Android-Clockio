package com.natasha.clockio.home.entity

data class Department(
    val id: String?,
    val name: String,
    val branchId: String?
) {
  constructor(): this("", "" ,"")

    override fun toString(): String {
        return this.name
    }
}