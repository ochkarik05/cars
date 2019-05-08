package com.shineapp.cars.data.repository

import com.shineapp.cars.data.model.Data

interface CarsRepository{
    fun getManufacturers(): Listing<Data>
    fun getModels(): Listing<Data>
    fun getYears(): Listing<Data>
}