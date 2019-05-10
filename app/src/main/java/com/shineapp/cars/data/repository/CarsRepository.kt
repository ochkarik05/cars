package com.shineapp.cars.data.repository

import com.shineapp.cars.data.model.Data

interface CarsRepository{
    fun getManufacturers(filter: String? = null): Listing<Data>
    fun getModels(manufacturer: String, filter: String? = null): Listing<Data>
    fun getYears(manufacturer: String, model: String, filter: String? = null): Listing<Data>
}