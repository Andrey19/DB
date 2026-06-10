package ru.effectivemobile.db.patterns

class Car private constructor(
    val brand: String,
    val model: String,
    val year: Int,
    val color: String,
    val engineType: String
) {
    override fun toString(): String {
        return "Car(brand='$brand', model='$model', year=$year, color='$color', engineType='$engineType')"
    }
    class Builder {
        private var brand: String = "Unknown"
        private var model: String = "Unknown"
        private var year: Int = 2000
        private var color: String = "White"
        private var engineType: String = "Petrol"

        fun setBrand(brand: String) = apply { this.brand = brand }
        fun setModel(model: String) = apply { this.model = model }
        fun setYear(year: Int) = apply { this.year = year }
        fun setColor(color: String) = apply { this.color = color }
        fun setEngineType(engineType: String) = apply { this.engineType = engineType }
        fun build() = Car(brand, model, year, color, engineType)
    }
}


interface CarFactory {
    fun createCar(): Car
}

class SedanFactory : CarFactory {
    override fun createCar(): Car = Car.Builder()
        .setBrand("Toyota")
        .setModel("Camry")
        .setYear(2023)
        .setColor("Black")
        .setEngineType("Hybrid")
        .build()
}

class SportsCarFactory : CarFactory {
    override fun createCar(): Car = Car.Builder()
        .setBrand("Ferrari")
        .setModel("F8 Tributo")
        .setYear(2024)
        .setColor("Red")
        .setEngineType("V8")
        .build()
}