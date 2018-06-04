package net.pdmtrdv.sashimisake.model

data class OrdersResponse1(val ended: String,
                          val id: Int,
                          val number: Int,
                          val order_dish: List<Order>,
                          val started: String,
                          val status: String, //COOKING, READY, DONE
                          val sum: Int)
