package com.data.models

import com.domain.models.recetas.Receta

object RecetaData {
    val listaReceta = mutableListOf<Receta>(
        Receta(
            id = 1,
            userId = 1,
            name = "Paella",
            description = "Paella con mariscos y vegetales.",
            ingredients = "Arroz, mariscos, vegetales",
            calories = "450 kcal",
            imageUrl = null
        ),
        Receta(
            id = 13,
            userId = 1,
            name = "Risotto de Setas",
            description = "Arroz cremoso con setas y queso parmesano.",
            ingredients = "Arroz, setas, caldo, parmesano, vino blanco",
            calories = "500 kcal",
            imageUrl = null
        )
    )
}
