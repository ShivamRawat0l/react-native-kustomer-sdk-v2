import {
  NativeModules,
  Platform,
  DeviceEventEmitter,
  NativeEventEmitter,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-kustomer-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const KustomerSdk = NativeModules.KustomerSdk
  ? NativeModules.KustomerSdk
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

type openTypes = 'default' | 'chat_kb' | 'chat_only' | 'kb_only';
var listener: any = null;
const eventEmitter = new NativeEventEmitter(NativeModules.KustomerSdk);
var subscription: any = null;

export function removeListener() {
  if (Platform.OS === 'ios' && subscription !== null) {
    subscription.remove();
    subscription = null;
  }
  if (Platform.OS === 'android' && listener !== null) {
    listener.remove();
    listener = null;
  }
}
export function addEvent(): void {
  if (Platform.OS === 'ios' && subscription === null) {
    subscription = eventEmitter.addListener('customEvent', () => {
      console.log('Event Captured IOS ');
    });
  }
  if (Platform.OS === 'android' && listener === null) {
    listener = DeviceEventEmitter.addListener('customEvent', () => {
      console.log('Event Captured');
    });
  }
}

export function emitEvent(): Promise<boolean> | number {
  return KustomerSdk.emitEvent();
}

export function multiply(a: number, b: number): Promise<number> {
  return KustomerSdk.multiply(a, b);
}

export function subtract(): Promise<number> {
  return KustomerSdk.subtract();
}

export function initialize(apiKey: string): Promise<number> {
  return KustomerSdk.initialize(apiKey);
}

export function isLoggedIn(email: string): Promise<number> {
  return KustomerSdk.isLoggedIn(email);
}

export function logIn(jwt: string): Promise<number> {
  return KustomerSdk.logIn(jwt);
}

export function logOut(): Promise<number> {
  return KustomerSdk.logOut();
}

export function open(type: openTypes): Promise<number> {
  return KustomerSdk.open(type);
}

export function openNewConversation(
  initialMessage: string,
  animated?: boolean //Only to be used in IOS
): Promise<string> {
  if (Platform.OS === 'ios')
    return KustomerSdk.openNewConversation(initialMessage, animated);
  else return KustomerSdk.openNewConversation(initialMessage);
}

export function openConversationByID(conversationID: String): Promise<String> {
  return KustomerSdk.openConversationByID(conversationID);
}

export function getUnreadCount(): Promise<number> {
  return KustomerSdk.getUnreadCount();
}

export function openKBbyID(kbID: string): Promise<boolean> {
  return KustomerSdk.openKBbyID(kbID);
}

export function setActiveAssistant(assistantID: string): Promise<boolean> {
  return KustomerSdk.setActiveAssistant(assistantID);
}

export function getOpenConversationCount(): Promise<boolean> {
  return KustomerSdk.getOpenConversationCount();
}

export function closeChat(animated:boolean): Promise<boolean> | void {
  if (Platform.OS === 'ios') return KustomerSdk.closeChat(animated);
  // Close Chat function not available in androi
}

export function chatVisible(): Promise<boolean> | void {
  if (Platform.OS === 'ios') return KustomerSdk.chatVisible();
  // Close Chat function not available in androi
}
