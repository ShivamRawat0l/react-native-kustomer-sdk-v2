import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import {
  multiply,
  subtract,
  addEvent,
  emitEvent,
  removeListener,
  initialize,
  open,
  closeChat,
} from 'react-native-kustomer-sdk-v2';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    subtract().then(setResult);
    // addEvent();
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <Text>If result is 14 it is able to call native module.</Text>
      <Button
        onPress={() => {
          initialize('Enter-API-Key');
        }}
        title="Init"
      />
      <Button
        onPress={() => {
          open('chat_only');
        }}
        title="Open Chat"
      />
      <Button
        onPress={() => {
          closeChat();
        }}
        title="Close Chat"
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
