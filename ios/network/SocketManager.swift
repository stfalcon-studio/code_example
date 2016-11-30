//
//  SocketManager.swift
//
//  Created by Victor Amelin on 9/29/16.
//

import Foundation
import SocketRocket

class SocketManager: NSObject {

    fileprivate override init() {}
    static let sharedInstance = SocketManager()
    override func copy() -> Any {
        fatalError("You are not allowed to use copy method on singleton!")
    }
    
    //------------------------------
    private var webSocket: SRWebSocket?
    
    var connectionWasOpened = true
    var lastUserId = UserDefaults.standard.string(forKey: Constants.myIdKey.rawValue)
    
    func openNewConnection(userId: String) {
        if webSocket != nil {
            webSocket?.close()
            webSocket?.delegate = nil
            webSocket = nil
        }
        lastUserId = userId
        let urlStr = "ws://you_url/\(lastUserId ?? "")"
        webSocket = SRWebSocket(url: URL(string: urlStr))
        webSocket?.delegate = self
        webSocket?.open()
    }
    
    func closeConnection() {
        webSocket?.close()
    }
    
    func logoutFromWebSocket() {
        webSocket?.close()
        webSocket?.delegate = nil
        webSocket = nil
    }
    
    func reconnect() {
        if lastUserId != nil &&
            (webSocket?.readyState != SRReadyState.CONNECTING) &&
            (webSocket?.readyState != SRReadyState.OPEN) {
            
            openNewConnection(userId: lastUserId!)
        }
    }
    
    deinit {
        logoutFromWebSocket()
    }
}

extension SocketManager: SRWebSocketDelegate {
    func webSocketDidOpen(_ webSocket: SRWebSocket!) {
        webSocket?.sendPing(nil)
        connectionWasOpened = true
    }
    
    func webSocket(_ webSocket: SRWebSocket!, didFailWithError error: Swift.Error!) {
        if error != nil {
            print("SOCKET ERROR: \(error.localizedDescription)")
        }
        if connectionWasOpened == true {
            connectionWasOpened = false
            reconnect()
        } else {
            DispatchQueue.main.asyncAfter(deadline: .now() + 15, execute: {
                self.reconnect()
            })
        }
    }
    
    func webSocket(_ webSocket: SRWebSocket!, didCloseWithCode code: Int, reason: String!, wasClean: Bool) {
        print("SOCKET ERROR: \(reason)")
        reconnect()
    }
    
    func webSocket(_ webSocket: SRWebSocket!, didReceivePong pongPayload: Data!) {
        print("PONG PAYLOAD: \(pongPayload.description)")
        if webSocket.readyState == SRReadyState.OPEN {
            webSocket.sendPing(nil)
        }
    }
    
    func webSocket(_ webSocket: SRWebSocket!, didReceiveMessage message: Any!) {
        print("MESSAGE: \(message)")
        
        EventManager.sharedInstance.performEvent(data: message)
    }
}













