package net.pdmtrdv.sashimisake.model

data class CategoryUpdateRequest(val image: Image,
                                 val name: String) {

    data class Image(val id: Int)

}