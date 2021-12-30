package com.reactnativekustomersdk

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.kustomer.ui.Kustomer  // Added for customer
import android.util.Log;
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kustomer.core.models.KusResult
import com.kustomer.core.models.chat.KusConversation
import com.kustomer.core.models.KusWidgetType
import com.kustomer.core.models.chat.KusEmail
import com.kustomer.core.models.chat.KusPhone
import com.kustomer.core.models.chat.KusCustomerDescribeAttributes
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.*; // WritableMap,  Arguments ,ReadableMap
import kotlinx.coroutines.runBlocking

class KustomerSdkModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "KustomerSdk"
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    fun multiply(a: Int, b: Int, promise: Promise) {
      promise.resolve(a * b)
    }

    @ReactMethod
    fun subtract( promise: Promise) {
      promise.resolve(14)
    }

    @ReactMethod
    fun initialize(apiKey : String,promise : Promise) {
        //Kustomer.init(application = this, apiKey = apiKey) {
        //  Log.i("Kustomer:","Kustomer is initialized ${it.dataOrNull}")
        //}
        //For android this is initialized directly from the mainapplication.java

        promise.resolve(true);
    }

    @ReactMethod
    fun isLoggedIn (email: String, promise: Promise) {
      var status = Kustomer.getInstance().isLoggedIn(email)
      promise.resolve(status);
    }

    @ReactMethod
    fun logIn (jwt: String, promise: Promise) {
      Kustomer.getInstance().logIn(jwt){
        when (it) {
          is KusResult.Success -> {
            promise.resolve(it.data);
          }
          is KusResult.Error -> {
            promise.reject(it.exception.localizedMessage);
          }
        }
      }
    }

    @ReactMethod
    fun logOut(promise : Promise){
      Kustomer.getInstance().logOut()
      promise.resolve(true);
    }

    @ReactMethod
    fun open (type : String,promise: Promise ) {
      try {
        when(type){
          "default" -> {
            Kustomer.getInstance().open(KusWidgetType.DEFAULT);
            promise.resolve(true);
          }
          "chat_kb" -> {
            Kustomer.getInstance().open(KusWidgetType.CHAT_KB);
            promise.resolve(true);
          }
          "chat_only" -> {
            Kustomer.getInstance().open(KusWidgetType.CHAT_ONLY);
            promise.resolve(true);
          }
          "kb_only" -> {
            Kustomer.getInstance().open(KusWidgetType.KB_ONLY);
            promise.resolve(true);
          }
          else -> {
            promise.reject("Unknown type passed");
          }
        }
      }
      catch(e:Exception){
        promise.reject("Make sure API key is valid");
      }
    }

    @ReactMethod
    fun openNewConversation(initialMessage: String,promise : Promise){
      Kustomer.getInstance()
      .openNewConversation(initialMessage){ result: KusResult<KusConversation> ->
            when (result) {
                is KusResult.Success -> {
                  promise.resolve(result.data.id)
                }
                else -> {
                  promise.reject("An error occurred")
                }
            }
        }
    }

    @ReactMethod
    fun openConversationByID(conversationID: String,promise : Promise){
      Kustomer.getInstance()
      .openConversationWithId(conversationID) { result: KusResult<KusConversation> ->
        when (result) {
          is KusResult.Success -> {
            promise.resolve("success")
          }
          is KusResult.Error -> {
            promise.reject("error")
          }
        }
      }
    }

    @ReactMethod
    fun getUnreadCount(promise:Promise){
      val unreadCount = liveData {
        emitSource(Kustomer.getInstance().observeUnreadCount())
      }
      promise.resolve(unreadCount);
    }

    @ReactMethod
    fun openKBbyID(kbID:String, promise:Promise){
      Kustomer.getInstance().openKbArticle(kbID) {
        if (it is KusResult.Success) {
            promise.resolve("true");
        } else {
            promise.reject("false")
        }
      }
    }

    @ReactMethod
    fun setActiveAssistant(assistantID:String, promise:Promise){
      Kustomer.getInstance().overrideAssistant(assistantID) {
        when (it) {
            is KusResult.Success -> {
              promise.resolve(it.data)
            }
            is KusResult.Error -> {
              promise.reject(it.exception.localizedMessage)
            }
        }
      }
    }

    @ReactMethod
    fun getOpenConversationCount(promise:Promise){
      val activeConversationIds: LiveData<Set<String>> = liveData {
        emitSource(Kustomer.getInstance().observeActiveConversationIds())
      }
      promise.resolve(activeConversationIds);
    }

    @ReactMethod
    fun emitEvent(promise:Promise){
      var result :WritableMap =  Arguments.createMap();
      result.putString("message", "memory");
      var reactContext :ReactApplicationContext= getReactApplicationContext();
      reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit("customEvent",result)
      promise.resolve(true);
    }

    @ReactMethod
    fun describeCustomer(data: ReadableMap) {
        val email = data.getString("email")
        val phone = data.getString("phone")
        val custom = data.getMap("custom")

        val attributes = KusCustomerDescribeAttributes(
                emails = listOf(KusEmail(email!!)),
                phones = listOf(KusPhone(phone!!)),
                custom = toMap(custom!!)
        )

        runBlocking {
            Kustomer.getInstance().describeCustomer(attributes)
        }
    }

    private fun toMap(readableMap: ReadableMap): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        val iterator: ReadableMapKeySetIterator = readableMap.keySetIterator()

        while (iterator.hasNextKey()) {
            val key: String = iterator.nextKey()
            val type: ReadableType = readableMap.getType(key)

            when (type) {
                ReadableType.Boolean -> map[key] = readableMap.getBoolean(key).toString()
                ReadableType.Number -> map[key] = readableMap.getDouble(key).toString()
                ReadableType.String -> {
                    val value: String? = readableMap.getString(key)
                    if (value != null && !value.isEmpty()) {
                        map[key] = value
                    }
                }
            }
        }
        return map
    }
}
