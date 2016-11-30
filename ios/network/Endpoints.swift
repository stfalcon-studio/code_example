//
//  Endpoints.swift
//
//  Created by Victor Amelin on 8/30/16.

import Foundation
import Moya

enum Endpoints {
    case authorization(client_id: String, client_secret: String, grant_type: String, fb_access_token: String, refresh_token: String?, device_type: String, device_id: String)
    case updateRefreshToken(client_id: String, client_secret: String, grant_type: String, refresh_token: String, device_type: String, device_id: String)
    case updateUserName(name: String)
    case updatePhoto(photo: UIImage)
    case getCategories()
    case startQuestions(categories: [String], search_type: String)
    case postAnswer(started_at: UInt, answered_at: UInt, questionnaire: String, question: String, answer: String)
    ...
}

private extension String {
    var URLEscapedString: String {
        return addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
    }
}

extension Endpoints: TargetType {
    
    var baseURL: URL { return URL(string: "http://you_url.com")! }
    
    var path: String {
        switch self {
        case .authorization, .updateRefreshToken: return "/you_url"
        case .updateUserName: return "/you_url"
        case .updatePhoto: return "/you_url"
        case .getCategories: return "/you_url"
        case .startQuestions: return "/you_url"
        case .postAnswer: return "/you_url"
        case .getUserFriends: return "/you_url"
        case .getFacebookFriends: return "/you_url"
        case .getTopFriends: return "/you_url"
        ...
        }
    }
    
    var method: Moya.Method {
        switch self {
        case .authorization,
             .updateRefreshToken,
             .getCategories,
             .getUserFriends,
             .getFacebookFriends,
             ...
             .getIsFriend: return .GET
            
        case .updateUserName,
             .putExplanationLast,
             .acceptFriendRequest,
             .declineFriendRequest,
             .putPracticeFinish,
             .declineQuestionnarie,
             .putRechargeOnePencil: return .PUT
            
        case .updatePhoto,
             .startQuestions,
             .postAnswer,
             ...
             .postPurchases: return .POST
            
        case .unfriendUser: return .DELETE
        }
    }
    
    var parameters:  [String: Any]? {
        switch self {
        case .authorization(let client_id, let client_secret, let grant_type, let fb_access_token, let refresh_token, let device_type, let device_id):
            return ["client_id":client_id,
                    "client_secret":client_secret,
                    "grant_type":grant_type,
                    "fb_access_token":fb_access_token,
                    "refresh_token":refresh_token as Any,
                    "device_type":device_type,
                    "device_id":device_id]
            
        case .updateRefreshToken(let client_id, let client_secret, let grant_type, let refresh_token, let device_type, let device_id):
            return ["client_id":client_id,
                    "client_secret":client_secret,
                    "grant_type":grant_type,
                    "refresh_token":refresh_token,
                    "device_type":device_type,
                    "device_id":device_id]
         
        case .getTopFriends(let page, let limit):
            return ["page":page, "limit":limit]
            
        case .getFacebookFriends(let page, let limit):
            return ["page":page, "limit":limit]
            
        case .getRecentOponents(let page, let limit):
            return ["page":page, "limit":limit]
            
        case .getUserFriends(let facebook_friends, let page):
            return ["page":page, "facebook_friends":facebook_friends]
            
        case .getExplanationList(let page):
            return ["page":page]
         
        case .getAnswerStatistics(let categories):
            return ["categories":categories]
            
        case .getQuestionnariesFinishStatus(let id):
            return ["id":id]
            
        case .getUserNotifications(let page):
            return ["page":page]

        case .declineQuestionnarie(let id):
            return ["id":id]
         
        default: return nil
        }
    }
    
    var sampleData: Data {
        return "".data(using: .utf8)!
    }
    
    var task: Task {
        switch self {
        case .updatePhoto(let photo):
            let imData = UIImagePNGRepresentation(photo)
            let multipartFormData = Moya.MultipartFormData.init(provider: Moya.MultipartFormData.FormDataProvider.data(imData!),
                                                                name: "photo",
                                                                fileName: "file.png",
                                                                mimeType: "image/png")
            return Task.upload(UploadType.multipart([multipartFormData]))
            
        default: return Task.request
        }
    }
}









