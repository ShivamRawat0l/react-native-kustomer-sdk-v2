import KustomerChat

@objc(KustomerSdk)
class KustomerSdk: RCTEventEmitter{// NSObject {

    @objc(multiply:withB:withResolver:withRejecter:)
    func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve(a*b)
    }

    @objc(subtract:withRejecter:)
    func subtract(resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve(14)
    }
    @objc(emitEvent:withRejecter:)
    func emitEvent(resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        self.bridge.eventDispatcher().sendAppEvent(withName: "customEvent", body: nil)
        resolve(14)
    }
     @objc open override func supportedEvents() -> [String] {
         return ["customEvent"];
    }
    @objc(initialize:withResolver:withRejecter:)
    func initialize(apiKey: String, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        Kustomer.configure(apiKey: apiKey, options: nil, launchOptions: nil)
    }

    @objc(open:withResolver:withRejecter:)
    func open(type: String, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
      switch(type){
        case "default":
          Kustomer.show();
          break;
        case "chat_kb":
          Kustomer.show(preferredView: .activeChat);
          break;
        case "chat_only":
          Kustomer.show(preferredView: .onlyChat);
          break;
        case "kb_only":
          Kustomer.show(preferredView: .onlyKnowledgeBase);
          break;
        default :
          print("Correct option not specified");
      }
    }

    @objc(logIn:withResolver:withRejecter:)
    func logIn(jwt:String, resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void{
      Kustomer.logIn(jwt: jwt) { result in
      switch result {
        case .success:
          resolve(true)
        case .failure(let error):
          reject("Error",error.localizedDescription,error)
        }
      }
    }

    @objc(isLoggedIn:withResolver:withRejecter:)
    func isLoggedIn(email:String, resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void{
      Kustomer.isLoggedIn(userEmail:email, userId:nil) { (result: Result<Bool, KError>) in
         switch result {
          case .success(let successResult):
              let userIsLoggedIn = successResult
              resolve(userIsLoggedIn);
              break
          case .failure(let errResult):
              reject("Error","An error occured while logging in",errResult)
          }
      }
    }

    @objc(logOut:withRejecter:)
    func logOut(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void{
      Kustomer.logOut({ error in
        if error != nil {
          print("there was a problem \(error?.localizedDescription ?? "")")
        }
      })
    }
    // Not avaialble in android
    @objc(chatVisible:withRejecter:)
    func chatVisible(resolve: RCTPromiseResolveBlock , reject :RCTPromiseRejectBlock)->Void {
      resolve(Kustomer.isVisible);
    }

    @objc(openNewConversation:withAnimated:withResolver:withRejecter:)
    func openNewConversation(initialMessage:String,animated: Bool ,resolve:@escaping RCTPromiseResolveBlock , reject :@escaping RCTPromiseRejectBlock)->Void {
      Kustomer.openNewConversation(
        initialMessages: [initialMessage],
        afterCreateConversation: { conversation in
          resolve(conversation.id)
        }, animated: animated)
    }

    @objc(openConversationByID:withResolver:withRejecter:)
    func openConversationByID(conversationID: String,resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void{
      Kustomer.openConversation(id: conversationID, completion: { result in
        switch result {
        case .success(let conversation):
          resolve(conversation.id)
        case .failure(let error):
          reject("Error","An error occured while opening the conversation",error)
        }
      })
    }

    @objc(setActiveAssistant:withResolver:withRejecter:)
    func setActiveAssistant(assistantID: String,resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void{
      Kustomer.options.activeAssistant = .withId(assistantID)
      resolve(true)
    }
    //here kb is knowledge base
    @objc(openKBbyID:withResolver:withRejecter:)
    func openKBbyID(kbID: String,resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void{
        Kustomer.showKbArticle(id:kbID)
      resolve(true)
    }

    @objc(getUnreadCount:withRejecter:)
    func getUnreadCount(resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void{
          let count = Kustomer.getUnreadCount() //TODO check if this method is working fine because this is not how it is implemented in the kustomer website but it is not working that way.
        resolve(count)
    }

    @objc(getOpenConversationCount:withRejecter:)
    func getOpenConversationCount(resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void{
      let count = Kustomer.chatProvider.openConversationCount()
      resolve(count)
    }
    // Not available in android
    @objc(closeChat:withResolver:withRejecter:)
    func closeChat(animated : Bool,resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void{
        Kustomer.close(animated :animated, completion:{
            //statemetns
        })
    }

    @objc(describeCustomer:)
    func describeCustomer(data: [AnyHashable : Any]) -> Void {
      var emails = [String]()
      let email = data["email"] as? String
      if email?.count != 0 {
        emails.append(email!)
      }
      var phones = [String]()
      let phone = data["phone"] as? String
      if phone?.count != 0 {
        phones.append(phone!)
      }
      var customs = [String : Any]()
      let custom = data["custom"] as? [String: Any]
      if custom != nil {
        customs = custom!
      }
      Kustomer.chatProvider.describeCurrentCustomer(phones: phones, emails: emails, custom: customs) { result in
        switch result {
        case .success:
          print("ok")
        case .failure(let error):
          print(error)
        }
      }
    }
}
