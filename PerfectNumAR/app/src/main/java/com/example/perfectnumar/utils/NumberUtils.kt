package com.example.perfectnumar.utils

object NumberUtils {
    // 100'e kadar olan mükemmel sayılar: 6 ve 28
    private val PERFECT_NUMBERS = listOf(6, 28)
    
    // 100'e kadar olan ve oyunda kullanılacak mükemmel olmayan sayılar
    private val NON_PERFECT_NUMBERS = listOf(
        12, 18, 20, 25, 30, 35, 40, 45, 50, 
        55, 60, 65, 70, 75, 80, 85, 90, 95, 100
    )

    fun generateRandomNumber(): Int {
        // Mükemmel ve mükemmel olmayan sayılar arasından rastgele seçim
        return if (Math.random() < 0.3) { // %30 ihtimalle mükemmel sayı gelecek
            PERFECT_NUMBERS.random()
        } else {
            NON_PERFECT_NUMBERS.random()
        }
    }

    fun isPerfectNumber(number: Int): Boolean {
        if (number > 100) return false // 100'den büyük sayıları kontrol etmeye gerek yok
        
        var sum = 0
        for (i in 1 until number) {
            if (number % i == 0) sum += i
        }
        return sum == number
    }

    fun getFactors(number: Int): List<Int> {
        if (number > 100) return emptyList()
        return (1 until number).filter { number % it == 0 }
    }

    fun isValidFactor(factor: Int, number: Int): Boolean {
        return factor > 0 && 
               factor < number && 
               number % factor == 0 && 
               number <= 100 && 
               factor <= 100
    }

    // Yardımcı fonksiyon: Sayının çarpanlarının toplamını hesapla
    fun calculateFactorsSum(factors: List<Int>): Int {
        return factors.sum()
    }

    // Yardımcı fonksiyon: Verilen sayının geçerli bir oyun sayısı olup olmadığını kontrol et
    fun isValidGameNumber(number: Int): Boolean {
        return number in 1..100 && 
               (PERFECT_NUMBERS + NON_PERFECT_NUMBERS).contains(number)
    }
}