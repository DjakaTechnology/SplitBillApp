package id.djaka.splitbillapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform