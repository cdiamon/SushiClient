package net.pdmtrdv.sashimisake.model

data class ReportResponse(val count: Int,
                          val date: String,
                          val sum: Int) {
    override fun toString(): String {
        return "количество: $count, дата: $date, сумма: $sum"
    }
}