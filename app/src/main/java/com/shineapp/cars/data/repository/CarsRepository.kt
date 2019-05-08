package com.shineapp.cars.data.repository

import com.shineapp.cars.data.model.Data

interface CarsRepository{
    fun getManufacturers(): Listing<Data>
    fun getModels(manufacturer: String): Listing<Data>
    fun getYears(manufacturer: String, model: String): Listing<Data>
}