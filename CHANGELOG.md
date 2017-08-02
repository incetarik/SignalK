# SignalK - A wrapper library for ASP.NET SignalR

SignalK is a wrapper library for Java client library of ASP.NET SignalR, for Kotlin.
This library contains some extension functions to enable developers type less. Also contains
builder functions.

# 1.1.0 - 02.08.2017
## Added
- `at` function added for no-parameter functions.
- `CancelError: Throwable` class added to indicate whether invoking server function is cancelled.
- `invoke(method: String, vararg args: Any?, [function: ((Throwable?) -> Unit)?])` method added, now you may invoke a 
function on the server and process the result here, the default parameter `it` is as Throwable to indicate status.
If `null` then successfully executed, if is `CancelError` then the future is cancelled. Otherwise is error.

- `invoke(method: String, vararg args: Any?, onSuccess: (<ReturnValue>) -> Unit [, onError: ((Throwable?) -> Unit)?] [, onCancel: (() -> Unit)?]` method 
is added that behaves in the same way as above one but separately.

# 1.0.0 - 29.03.2017
Release/Initial version