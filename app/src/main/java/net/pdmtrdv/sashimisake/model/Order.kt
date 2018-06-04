package net.pdmtrdv.sashimisake.model

data class Order(val count: Int,
                 val dish: MenuCategoryResponse.Dish) {

        data class Dish(val id: Int,
                        val image: Image,
                        val ingredients: String,
                        val name: String,
                        val price: Int,
                        val weight: String) {

            data class Image(val id: Int? = null,
                             val original_name: String? = null,
                             val path: String? = null)

        }

    }