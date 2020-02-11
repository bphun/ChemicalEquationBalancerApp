//
//  Environment.swift
//  ChemicalEquationBalancerApp
//
//  Created by Brandon Phan on 1/27/20.
//  Copyright © 2020 Brandon Phan. All rights reserved.
//

import Foundation

public enum Environment {
    enum Keys {
        enum Plist {
            static let productionApiHostname = "PRODUCTION_API_HOSTNAME"
            static let localTestingHostname = "LOCAL_TEST_API_HOSTNAME"
        }
    }
    
    private static let infoDictionary: [String: Any] = {
        guard let dict = Bundle.main.infoDictionary else {
            fatalError("Plist file not found")
        }
        return dict
    }()
    
    static let productionApiHostname: String = {
        guard let productionApiHostname = Environment.infoDictionary[Keys.Plist.productionApiHostname] as? String else {
            fatalError("Production API hostname not set in plist for this environment")
        }
        return productionApiHostname
    }()
    
    static let localTestingHostname: String = {
        guard let localTestingHostname = Environment.infoDictionary[Keys.Plist.localTestingHostname] as? String else {
            fatalError("Local testing API hostname not set in plist for this environment")
        }
        return localTestingHostname
    }()
}
