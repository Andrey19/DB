package ru.effectivemobile.db

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.effectivemobile.db.data.database.AppDatabase
import ru.effectivemobile.db.data.repository.FlowerShopRepository
import ru.effectivemobile.db.network.NetworkClient
import ru.effectivemobile.db.patterns.Car
import ru.effectivemobile.db.patterns.SedanFactory
import ru.effectivemobile.db.patterns.SportsCarFactory

class MainActivity : ComponentActivity() {

    private lateinit var repository: FlowerShopRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = AppDatabase.getInstance(this)
        repository = FlowerShopRepository(
            database = database,
            flowerDao = database.flowerDao(),
            bouquetDao = database.bouquetDao(),
            bouquetFlowerDao = database.bouquetFlowerDao()
        )

        findViewById<Button>(R.id.btnMigration).setOnClickListener {
            lifecycleScope.launch {
                checkMigration()
            }
        }

        findViewById<Button>(R.id.btnInterceptor).setOnClickListener {
            lifecycleScope.launch {
                testInterceptor()
            }
        }

        findViewById<Button>(R.id.btnPatterns).setOnClickListener {
            testPatterns()
        }

        findViewById<Button>(R.id.btnTask1).setOnClickListener {
            lifecycleScope.launch {
                demoTask1()
            }
        }
    }

    private suspend fun demoTask1() {
        Log.d("Task1", "=== Задание 1: Доступные букеты ДО покупки ===")
        val before = repository.getBouquetsWithAvailability()
        before.forEach {
            Log.d("Task1", "${it.bouquet.name} - доступно: ${it.availableQuantity} шт., цена: ${it.bouquet.price}")
        }

        val success = repository.purchaseBouquet(1, count = 1)
        Log.d("Task1", "Покупка букета с id=1: ${if (success) "успешна" else "не удалась"}")

        Log.d("Task1", "=== Задание 1: Доступные букеты ПОСЛЕ покупки ===")
        val after = repository.getBouquetsWithAvailability()
        after.forEach {
            Log.d("Task1", "${it.bouquet.name} - доступно: ${it.availableQuantity} шт., цена: ${it.bouquet.price}")
        }
        Toast.makeText(this, "Задание 1 выполнено, смотри логи", Toast.LENGTH_LONG).show()
    }

    private suspend fun checkMigration() {
        val bouquets = repository.getBouquetsWithAvailability()
        val firstBouquet = bouquets.firstOrNull()?.bouquet
        val flowers = repository.getAllFlowers().firstOrNull()
        val firstFlower = flowers?.firstOrNull()

        Log.d("MigrationDemo", "=== Проверка миграции ===")
        Log.d("MigrationDemo", "Первый букет: ${firstBouquet?.name}")
        Log.d("MigrationDemo", "  packagingType = ${firstBouquet?.packagingType} (должен быть null или тестовое значение)")
        Log.d("MigrationDemo", "Первый цветок: ${firstFlower?.name}")
        Log.d("MigrationDemo", "  countryOfOrigin = ${firstFlower?.countryOfOrigin} (должен быть null или тестовое значение)")

        Toast.makeText(this, "Миграция выполнена. Новые поля добавлены. Смотрите логи.", Toast.LENGTH_LONG).show()
    }

    private suspend fun testInterceptor() {
        try {
            val response = NetworkClient.apiService.getPost()
            if (response.isSuccessful) {
                Log.d("InterceptorDemo", "Запрос успешен, код ${response.code()}")
                Log.d("InterceptorDemo", "Заголовок поста: ${response.body()?.title}")
                Toast.makeText(this, "Успешно! Код ответа: ${response.code()}", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("InterceptorDemo", "Ошибка, код ${response.code()}")
                Toast.makeText(this, "Ошибка! Код ответа: ${response.code()}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("InterceptorDemo", "Исключение: ${e.message}")
            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun testPatterns() {
        val car = Car.Builder()
            .setBrand("BMW")
            .setModel("X5")
            .setYear(2022)
            .setColor("Blue")
            .setEngineType("Diesel")
            .build()

        Log.d("PatternsDemo", "=== Паттерн Builder ===")
        Log.d("PatternsDemo", car.toString())


        val sedanFactory = SedanFactory()
        val sedan = sedanFactory.createCar()

        val sportsFactory = SportsCarFactory()
        val sportsCar = sportsFactory.createCar()

        Log.d("PatternsDemo", "=== Паттерн Abstract Factory ===")
        Log.d("PatternsDemo", "Седан: $sedan")
        Log.d("PatternsDemo", "Спорткар: $sportsCar")

        Toast.makeText(this, "Объекты Car созданы. Смотрите логи.", Toast.LENGTH_SHORT).show()
    }
}