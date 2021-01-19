fun renderProductTable(): String {
    return html {
        table {
            tr(color = getTitleColor()) {
                td {
                    text("Product")
                }
                td {
                    text("Price")
                }
                td {
                    text("Popularity")
                }
            }
            val products = getProducts()
            for (product in products) {
                tr(color = getTitleColor()) {
                    td {
                        text(product.description)
                    }
                    td {
                        text(product.price)
                    }
                    td {
                        text(product.popularity)
                    }
                }
            }

        }
    }.toString()
}

fun getTitleColor() = "#b9c9fe"
fun getCellColor(index: Int, row: Int) = if ((index + row) % 2 == 0) "#dce4ff" else "#eff2ff"
