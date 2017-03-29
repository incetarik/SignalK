# SignalK - A wrapper library for ASP.NET SignalR

SignalK is a wrapper library for Java client library of ASP.NET SignalR, for Kotlin.
This library contains some extension functions to enable developers type less. Also contains
builder functions.

### Examples:
With default Java library when writing in Kotlin
```kotlin
val connection = HubConnection("")
val proxy = connection.createHubProxy("proxy")
    
proxy.on<String>("event", SubscriptionHandler1<String> { 
    println(it)
}, String::class.java)

proxy.on<String, String>("second-event", { first, second ->
    println("Pair: ${first to second}")
    proxy.setState("JSON-State", JsonObject())

    val state = proxy.getState("JSON-State")
    val obj = state.asJsonObject

    val subscription = proxy.subscribe("subscribe-event")
    subscription.addReceivedHandler { println("Receive handler: $it") }
}, String::class.java, String::class.java)
    
proxy.on<String, Int, Boolean>("third-event", { first, second, third ->
        
})
```

After this small library used
```kotlin
val connection = HubConnection("")

// Direct usage
(HubConnection("")) { 
    // ...
}

// or with assigned value
connection {
    createHubProxy("proxy") {
        on("event") { it: String? ->
            println(it)
        }
        
        on<String, String>("second-event") { first, second ->
            println("Pair: ${first to second}")
            this["JSON-State"] = JsonObject()
            
            val state = this["JSON-State"]
            val obj = state?.asJsonObject
            
            handle("subscribe-event") {
                println("Receive handler: $it")
            }
        }
        
        on("third-event") { first: String?, second: Int?, third: Boolean? ->
            
        }
        
        // Or
        
        on<String, Int, Boolean>("third-event") { first, second, third ->
            
        }
    }
}
```