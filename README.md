
# ![Kustomer SDK V2 React Native](https://i.imgur.com/mcHJ1s8.png)
This is a wrapper for Kustomer v2 Sdk for react native. This implements the kustomer v2 for android and ios.
Functions available for android and ios are available on react native.

## Getting started

`$ npm install react-native-kustomer-sdk-v2 --save`

or

`$ yarn add react-native-kustomer-sdk-v2`

## Requirements

 - Android minSdkVersion 21
  - iOS level 11


### Android ( Important )

For android to register the api key

Add the following dependancies in build.gradle (App)

```groovy
dependencies {
  ...
  implementation 'com.kustomer.chat:ui:2.9.2'
  ...
}
```

Add the code at last inside onCreate() in MainApplication.java
```java
//MainApplication.java
//Add these import files
import kotlin.jvm.functions.Function1;
import com.kustomer.ui.KustomerOptions;
import com.reactnativekustomersdk.KustomerSdkPackage;
import com.kustomer.ui.Kustomer;  // Added for customer
import android.widget.Toast;
import com.kustomer.core.models.KusResult;
import kotlin.Unit;

...

public  void  onCreate()  {
...
	try {
	    Kustomer.Companion.init(this, "Enter-api-key-here", (KustomerOptions) null, (result) -> {
		    return Unit.INSTANCE;
		});
	}
	catch (AssertionError e) {
		Toast toast= Toast.makeText(getApplicationContext(),"API KEY IS INVALID",Toast.LENGTH_SHORT);
	    toast.show();
	}
}// onCreate closing bracket
```
The above code will show the toast if the API key is invalid.

- In case of allowBackup error 
set `android:allowBackup="true"` in the AndroidManifest.xml file

Import the library to use its methods:
```javascript
import * as KustomerSDK from "react-native-kustomer-sdk-v2";
or
import { open, login, logout } from "react-native-kustomer-sdk-v2";
```

Call initialize function at start in react native with the api key to register the Api key for iOS.
```javascript
import { initialize } from "react-native-kustomer-sdk-v2";
...
initialize('Enter-API-Key'); // Add this when the app will mount
```
```javascript
import * as KustomerSDK from "react-native-kustomer-sdk-v2";
...
KustomerSDK.initialize('Enter-API-Key'); // Add this when the app will mount
```

### iOS 

After installing the react-native-kustomer-sdk-v2 do pod install inside the ios folder.

```
pod install 
```

## Methods

- #### `initialize(apiKey: String): Promise<number>`

Initializes the apiKey to customer sdk. It is only required for ios and would cause no side effects on android.

```javascript
KustomerSDK.initialize('Enter-API-Key').then(...);
```

- #### `isLoggedIn(email: String): Promise<number>`

Calls the isLoggedIn function in the kustomer sdk. It checks whether the use is logged in.

```javascript
KustomerSDK.isLoggedIn('email').then(...);
```

- #### `logIn(jwt: String): Promise<number>`

Calls the logIn function in the kustomer sdk. It checks whether the use is logged in.  To generate the jwt token [use this code](https://gist.github.com/ShivamRawat0l/6e30d7de7f4aa31c378201600f0321cc)

```javascript
KustomerSDK.logIn('Enter-JWT-token here').then(...);
```

- #### `logOut(): Promise<number>`

Calls the logOut function in the kustomer sdk. It checks whether the user is logged in.
```javascript
KustomerSDK.logOut().then(...);
```
- #### `open(type: openTypes): Promise<number>`

Calls the open function in the kustomer sdk. Open the chat with one of the following types.

```javascript
KustomerSDK.open('default').then(...);
```
```typescript
type  openTypes  =  'default'  |  'chat_kb'  |  'chat_only'  |  'kb_only';
```
- #### `openNewConversation(initialMessage: string, animated?:boolean): Promise<string>`

Calls the openNewConversation function in the kustomer sdk. Open the conversation with initial Message.
Animated param is only available for iOS.

```javascript
KustomerSDK.openNewConversation('intial_message').then(...); //Android
KustomerSDK.openNewConversation('intial_message',true).then(...); //iOS
```

- #### `openConversationByID(conversationID: string): Promise<string>`

Calls the openConversationByID function in the kustomer sdk. Open the conversaton by id.

```javascript
KustomerSDK.openConversationByID('id').then(...);
```

- #### `getUnreadCount(): Promise<number>`

Calls the getUnreadCount function in the kustomer sdk. Returns the number unreadMessages.

```javascript
KustomerSDK.getUnreadCount().then(...);
```
- #### `openKBbyID(kbID:string): Promise<boolean>`

Calls the openKBbyID function in the kustomer sdk. Opens the knowledge base by id.

```javascript
KustomerSDK.openKBbyID('kbid').then(...);
```
- #### `setActiveAssistant(assistantID:string): Promise<boolean>`

Calls the setActiveAssistant function in the kustomer sdk. Sets the active assistant to the assistantID.

```javascript
KustomerSDK.setActiveAssistant('assistantID').then(...);
```
- #### `getOpenConversationCount(): Promise<boolean>`

Calls the getOpenConversationCountfunction in the kustomer sdk. Check the kustomer documentation for more description.

```javascript
KustomerSDK.getOpenConversationCount().then(...);
```
- #### `closeChat(): Promise<boolean> |  void`

Calls the closeChat function in the kustomer sdk. Closes the chat. Only available for iOS.

```javascript
KustomerSDK.closeChat().then(...);
```
- #### `chatVisible(): Promise<boolean> | void`

Calls the chatVisiblefunction in the kustomer sdk. Returns if the chat is visible. Only available for iOS.

```javascript
KustomerSDK.chatVisible().then(...);
```

## Manual Installation

### Android

#### Gradle

Include the library in your `android/app/build.gradle`:

`implementation 'com.kustomer.chat:ui:2.9.+'`

### iOS

#### CocoaPods

The preferred installation method is with [CocoaPods](https://cocoapods.org). Add the following to your `Podfile`:

```ruby
pod 'KustomerChat'
```

##  Contributing
To contribute fork repository and make the nessecary changes.
Raise a PR for the same.
Mention iOS/Android/RN in the PR.
Mention fix/feature/refactor in the PR.


## Errors
In case of error please raise a [issue](https://github.com/ShivamRawat0l/react-native-kustomer-sdk-v2/issues).

While development do following if error occurs.

 - Set android:allowBackup="true" in the AndroidManifest.xml file (Android)
 - comment flipper in the Podfile (iOS)
 - add use_framework! in the Podfile (iOS)
```
