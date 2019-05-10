package com.shineapp.cars.system

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun createObjectMapper(): ObjectMapper {
    val objectMapper = ObjectMapper()
    objectMapper.registerKotlinModule()
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    // Set all class field serializable (public, private, protected)
//    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    // Set don't throw exception if found unknown properties in json
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    // Set wrap a single value in a list or array where it expected
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    return objectMapper
}