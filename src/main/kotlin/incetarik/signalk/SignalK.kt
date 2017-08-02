package incetarik.signalk

import com.google.gson.JsonElement
import microsoft.aspnet.signalr.client.hubs.HubConnection
import microsoft.aspnet.signalr.client.hubs.HubProxy
import microsoft.aspnet.signalr.client.hubs.Subscription
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler
import kotlin.reflect.KClass

/**
 * Project: SignalK
 *
 * Date: 29 Mar 2017
 * Author: Tarık İNCE <incetarik@hotmail.com>
 */

inline infix operator fun HubConnection.invoke(crossinline builder: HubConnection.() -> Unit): HubConnection {
    builder(this)
    return this
}

inline fun HubConnection.createHubProxy(name: String, crossinline builder: HubProxy.() -> Unit): HubProxy? {
    val proxy = createHubProxy(name)
    builder(proxy)
    return proxy
}

inline fun HubConnection.create(name: String, crossinline builder: HubProxy.() -> Unit): HubProxy? {
    val proxy = createHubProxy(name)
    builder(proxy)
    return proxy
}

inline fun HubProxy.on(eventName: String, noinline function: () -> Unit) = on<Nothing>(eventName, SubscriptionHandler(function))
inline fun HubProxy.at(eventName: String, noinline function: () -> Unit) = on<Nothing>(eventName, SubscriptionHandler(function))
inline fun <reified T> HubProxy.on(eventName: String, noinline function: (T?) -> Unit) = on(eventName, function, T::class.java)
inline fun <reified T1, reified T2> HubProxy.on(eventName: String, noinline function: (T1?, T2?) -> Unit) = on(eventName, function, T1::class.java, T2::class.java)
inline fun <reified T1, reified T2, reified T3> HubProxy.on(eventName: String, noinline function: (T1?, T2?, T3?) -> Unit) = on(eventName, function, T1::class.java, T2::class.java, T3::class.java)
inline fun <reified T1, reified T2, reified T3, reified T4> HubProxy.on(eventName: String, noinline function: (T1?, T2?, T3?, T4?) -> Unit) = on(eventName, function, T1::class.java, T2::class.java, T3::class.java, T4::class.java)
inline fun <reified T1, reified T2, reified T3, reified T4, reified T5> HubProxy.on(eventName: String, noinline function: (T1?, T2?, T3?, T4?, T5?) -> Unit) = on(eventName, function, T1::class.java, T2::class.java, T3::class.java, T4::class.java, T5::class.java)

operator fun HubProxy.get(key: String): JsonElement? = getState(key)
operator fun <T : Any> HubProxy.get(key: String, klass: Class<T>): T? = getValue(key, klass)
operator fun <T : Any> HubProxy.get(key: String, klass: KClass<T>): T? = getValue(key, klass.java)
operator fun HubProxy.set(key: String, value: JsonElement?) = setState(key, value)

inline operator fun <reified T : Any> HubProxy.get(key: String, defaultValue: T) = getValue(key, T::class.java) ?: defaultValue

fun HubProxy.handle(name: String, function: (Array<out JsonElement?>) -> Unit): Subscription {
    val subscription = subscribe(name)
    subscription.addReceivedHandler(function)
    return subscription
}

class CancelError() : Throwable()

inline operator fun HubProxy.invoke(method: String, vararg args: Any?, noinline function: ((Throwable?) -> Unit)? = null)
        = this.invoke(method).done { function?.invoke(null) }.onError(function)
        .apply { onCancelled { function?.invoke(CancelError()) } }

inline operator fun <reified R : Any?> HubProxy.invoke(
        method: String,
        vararg args: Any?,
        noinline onSuccess: (returnValue: R) -> Unit,
        noinline onError: ((Throwable?) -> Unit)? = null,
        noinline onCancel: (() -> Unit)? = null) = this.invoke(R::class.java, R::class.java.componentType, method, args)
        .apply { done(onSuccess).onError(onError).onCancelled(onCancel) }