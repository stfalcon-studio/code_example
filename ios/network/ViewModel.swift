//
//  ViewModel.swift
//
//  Created by Victor Amelin on 9/20/16.
//

import RxSwift
import Moya
import Gloss
import SwiftyJSON

struct ViewModel {
    func getUserStatistics() -> Observable<([(String, String)], UserStatModel)> {
        return ProviderManager.sharedInstance.jsonProvider
            .request(Endpoints.getUserStatistic)
            .debug()
            .filterSuccessfulStatusAndRedirectCodes()
            .retryWithAuthIfNeeded()
            .mapJSON()
            .map { obj -> ([(String, String)], UserStatModel) in
                print(obj)
                
                let model = UserStatModel(json: obj as! Gloss.JSON)!
                
                var dataSource = [(String, String)]()
                
                ...
                
                return (dataSource, model)
        }
    }
    
    func getCategories() -> Observable<[CategoriesModel]?> {
        return ProviderManager.sharedInstance.jsonProvider
            .request(Endpoints.getCategories())
            .debug()
            .filterSuccessfulStatusAndRedirectCodes()
            .retryWithAuthIfNeeded()
            .observeOn(ConcurrentDispatchQueueScheduler(globalConcurrentQueueQOS: DispatchQueueSchedulerQOS.background))
            .mapJSON()
            .map { (obj) -> [CategoriesModel]? in
                print(obj)
                
                guard let dict = obj as? [AnyHashable:Any], let embedded = dict["_embedded"] as? [AnyHashable:Any], let items = embedded["items"] else { return nil }
                
                var categories = [CategoriesModel]()
                
                for item in (items as! NSArray) {
                    categories.append(CategoriesModel(json: item as! Gloss.JSON)!)
                }
                
                return categories
        }
    }
    
    func getAnswerStatistics(id: [String]) -> Observable<StatisticAnsModel> {
        return ProviderManager.sharedInstance.jsonProvider
            .request(Endpoints.getAnswerStatistics(categories: id))
            .debug()
            .filterSuccessfulStatusAndRedirectCodes()
            .retryWithAuthIfNeeded()
            .mapJSON()
            .map { obj -> StatisticAnsModel in
                print(obj)
                
                return StatisticAnsModel(json: obj as! Gloss.JSON)!
        }
    }
    
    func rechargePencil() -> Observable<Void> {
        return ProviderManager.sharedInstance.jsonProvider
            .request(Endpoints.putRechargeOnePencil)
            .debug()
            .filterSuccessfulStatusAndRedirectCodes()
            .retryWithAuthIfNeeded()
            .mapJSON()
            .map { obj -> Void in
                print(obj)
        }
    }
}















