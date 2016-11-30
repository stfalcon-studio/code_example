//
//  ObservableHelper.swift
//
//  Created by Victor Amelin on 9/21/16.

import RxSwift
import Moya

extension ObservableType where E == Response {

    func retryWithAuthIfNeeded() -> Observable<E> {
        return self.retryWhen { (e: Observable<Moya.Error>) in
            
            return e.flatMap { (error) -> Observable<Any> in
                if case Moya.Error.statusCode(let resp) = error, resp.statusCode == 401 {
                    return self.updateToken()
                }
                return Observable.error(error)
            }
        }
    }
    
    fileprivate func updateToken() -> Observable<Any> {
        if let token = UserDefaults.standard.object(forKey: Constants.refreshTokenKey.rawValue) as? String {
            return ProviderManager.sharedInstance.defaultProvider
                .request(Endpoints.updateRefreshToken(client_id: "0000000000",
                                                      client_secret: "11111111",
                                                      grant_type: "refresh_token",
                                                      refresh_token: token,
                                                      device_type: "ios",
                                                      device_id: UserDefaults.standard.object(forKey: Constants.deviceTokenKey.rawValue) as! String))
                .filterSuccessfulStatusAndRedirectCodes()
                .debug()
                .mapJSON()
                .map { obj -> Any in
                    print(obj)
                    
                    //save new token
                    let dict = obj as! [AnyHashable:Any]
                    
                    if let _ = dict["error_description"] {
                        //logout in case of error
                        VCManager.sharedInstance.logout()
                        
                    } else {
                        
                        let token = dict["access_token"]
                        let refreshToken = dict["refresh_token"]
                        
                        UserDefaults.standard.setValue(token, forKey: Constants.tokenKey.rawValue)
                        UserDefaults.standard.setValue(refreshToken, forKey: Constants.refreshTokenKey.rawValue)
                        UserDefaults.standard.synchronize()
                    }
                    return obj
                }

        } else {
            VCManager.sharedInstance.logout()
            return Observable.just("exit")
        }
    }
}



