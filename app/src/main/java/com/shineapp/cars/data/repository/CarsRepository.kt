package com.shineapp.cars.data.repository

import com.shineapp.cars.data.model.Data

interface CarsRepository{
    fun getManufacturers(): Listing<Data>
}