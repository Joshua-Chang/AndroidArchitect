package hi.kotlin_dmeo

fun main() {
fire(ApiGetArticles())
}

annotation class ApiDoc(val value: String)

@ApiDoc("modifier class")
class Box {
    @ApiDoc("modifier field")
    var size = 6

    @ApiDoc("modifier func")
    fun test() {

    }
}

public enum class Method {
    GET, POST
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpMethod(val method: Method)

interface Api {
    val name: String
    val version: String
        get() = "1.0"
}
@HttpMethod(Method.GET)
class ApiGetArticles:Api{
    override val name: String
        get() = "/api.articles"
}
fun fire(api: Api){
    val annotations = api.javaClass.annotations
    val httpMethod = annotations.find { it is HttpMethod } as? HttpMethod
    println(httpMethod?.method)
}
