package net.pdmtrdv.sashimisake.model

data class MenuCategoryResponse(
        var dishes: List<Dish>,
        var id: Int,
        var image: Image,
        var name: String) {


    data class Dish(
            var id: Int,
            var image: Image,
            var ingredients: String,
            var name: String,
            var price: Int,
            var weight: String)

    data class Image(
            var id: Int,
            var originalName: String,
            var path: String)

}