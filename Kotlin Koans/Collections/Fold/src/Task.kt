// Return the set of products that were ordered by all customers
fun Shop.getProductsOrderedByAll(): Set<Product> {
    val all=customers.flatMap { it.getOrderedProducts() }.toSet()
    return customers.fold(all){acc, customer -> acc.intersect(customer.getOrderedProducts()) }
}

fun Customer.getOrderedProducts(): List<Product> =orders.flatMap { it.products }