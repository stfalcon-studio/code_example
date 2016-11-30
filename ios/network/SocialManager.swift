//
//  SocialManager.swift
//
//  Created by Victor Amelin on 6/10/16.
//

import Foundation
import FBSDKLoginKit

class SocialManager: NSObject {
    
    typealias TokenHandler = (_ token: String?, _ error: NSError?) -> Void
    
    var handler: TokenHandler?
    
    fileprivate override init() {}
    static let sharedInstance = SocialManager()
    override func copy() -> Any {
        fatalError("You are not allowed to use copy method on singleton!")
    }
    
    //Facebook
    func requestFacebookReadToken(_ handler: @escaping TokenHandler) {
        let login = FBSDKLoginManager()
        login.loginBehavior = .systemAccount
        login.logIn(withReadPermissions: ["public_profile", "email", "user_friends"], from: nil) { (result, error) in
            if error != nil {
                handler(nil, error as NSError?)
            } else if (result?.isCancelled)! {
                handler(nil, NSError(domain: "com.company.application", code: 0, userInfo: [NSLocalizedDescriptionKey:"User pressed cancel!"]))
            } else {
                
                if let token = result?.token?.tokenString {
                    handler(token, nil)
                } else {
                    handler(nil, nil)
                }
            }
        }
        login.logOut()
    }
}















