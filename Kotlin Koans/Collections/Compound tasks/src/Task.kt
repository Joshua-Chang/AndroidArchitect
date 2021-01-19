// Find the most expensive product among all the delivered products
// ordered by the customer. Use `Order.isDelivered` flag.
fun findMostExpensiveProductBy(customer: Customer): Product? {
   return customer.orders.filter { it.isDelivered }.flatMap { it.products }.maxBy { it.price }
}

// Count the amount of times a product was ordered.
// Note that a customer may order the same product several times.
fun Shop.getNumberOfTimesProductWasOrdered(product: Product): Int {
//    return customers.count { it.orders.flatMap { it.products }.contains(product) }
    return customers.flatMap { it.getOrderedProducts() }.count { it==product }
}

fun Customer.getOrderedProducts(): List<Product> =
        orders.flatMap { it.products }
