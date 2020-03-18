//
//  ViewController.swift
//  ChemicalEquationBalancerApp
//
//  Created by Brandon Phan on 12/12/19.
//  Copyright © 2019 Brandon Phan. All rights reserved.
//

import UIKit

class ViewController: UIViewController, FrameExtractorDelegate {
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var imageCaptureButton: UIButton!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    @IBOutlet weak var uploadImageSwitch: UISwitch!
    @IBOutlet weak var shouldUseProdApiSwitch: UISwitch!
    @IBOutlet weak var rapidCaptureSwitch: UISwitch!
    @IBOutlet weak var activeUploadsLabel: UILabel!
    @IBOutlet weak var queueDepthLabel: UILabel!
    
    var frameExtractor: FrameExtractor!
    var shouldCaptureFrame = false
    var requestInProgress = false
    var hasEquationStr = false
    var equationStr: String?

    var enqueuedCaptures = 0 {
        didSet {
            DispatchQueue.main.async {
                self.queueDepthLabel.text = "Queue depth: \(self.enqueuedCaptures)"
            }
        }
    }
    
    var activeUploadSessions = 0 {
        didSet {
            DispatchQueue.main.async {
                self.activeUploadsLabel.text = "Upload sessions: \(self.activeUploadSessions)"
            }
        }
    }
        
    override func viewDidLoad() {
        super.viewDidLoad()
        frameExtractor = FrameExtractor()
        frameExtractor.delegate = self
        activityIndicator.isHidden = true
    }
    
    @IBAction func imageCaptureButtonAction(_ sender: Any) {
        let rapidCaptureEnabled = self.rapidCaptureSwitch.isOn

        self.shouldCaptureFrame = rapidCaptureEnabled || (!rapidCaptureEnabled && !requestInProgress)
    }
    
    @IBAction func manualEntryAction(_ sender: Any) {
        let alert = UIAlertController(title: "Enter chemical equation", message: "Seperate reactants and products with \"->\"", preferredStyle: .alert)
        
        alert.addTextField { (textField) in
            textField.placeholder = "Equation"
        }
        
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel ,handler: nil))
        alert.addAction(UIAlertAction(title: "Done", style: .default, handler: { [weak alert] (_) in
            if let textField = alert?.textFields![0] {
                let result = self.balanceEquation(textField.text!)
             
                let resultAlert = UIAlertController(title: "Results", message: result, preferredStyle: .alert)
                resultAlert.addAction(UIAlertAction(title: "Done", style: .cancel ,handler: nil))
                resultAlert.addAction(UIAlertAction(title: "Upload Image", style: .default, handler: { (_) in
                    self.equationStr = textField.text!
                    DispatchQueue.main.async {
                        resultAlert.resignFirstResponder()
                    }
                }))

                DispatchQueue.main.async {
                    alert?.resignFirstResponder()
                    self.present(resultAlert, animated: true, completion: nil)
                }
            }
        }))

        DispatchQueue.main.async {
            self.present(alert, animated: true, completion: nil)
        }
    }
    
    func captured(image: UIImage) {
        self.imageView.image = image
        
        if shouldCaptureFrame {
            let shouldUploadImage = self.uploadImageSwitch.isOn
            let useProdApi = self.shouldUseProdApiSwitch.isOn
            
            shouldCaptureFrame = false
            
            displayActivityIndicator()
            
            DispatchQueue.global().async {
                print("Request start")
                self.processImage(image, shouldUploadImage: shouldUploadImage, useProdApi: useProdApi)
            }
            
            enqueuedCaptures += 1
        }
    }
    
    func processImage(_ image: UIImage, shouldUploadImage: Bool, useProdApi: Bool) {
        guard let base64EncodedImageString = image.toBase64() else { return }
        imageRequest(base64EncodedImage: base64EncodedImageString, shouldUploadImage: shouldUploadImage, useProdApi: useProdApi)
        enqueuedCaptures -= 1
    }
    
    func imageRequest(base64EncodedImage: String, shouldUploadImage: Bool, useProdApi: Bool) {
        let feature = ImageprocessorFeature(maxResults: 20, type: FeatureType.DocumentTextDetection.rawValue)
        let image = ImageProcessorImage(base64EncodedString: base64EncodedImage)
        let imageProcessorRequest = ImageProcessorRequest(image: image, feature: feature)
        let imageProcessorRequestBody = ImageProcessorRequestBody(request: imageProcessorRequest)
        let requestBodyJson = try! JSONEncoder().encode(imageProcessorRequestBody)
        let imageProcessingRequest = ApiRequest(resource: ImageProcessResource(data: requestBodyJson, shouldUploadImage: shouldUploadImage, equationStr: equationStr, useProdApi: useProdApi))
        
        requestInProgress = true
        activeUploadSessions += 1
//        print(requestBodyJson.prettyPrintedJSONString)
        imageProcessingRequest.run(withCompletionHandler: { (processorOutput) in
//            print(processorOutput)
            if let processorOutput = processorOutput {
                if let error = processorOutput.responses[0].error {
                    print("API Bad Response: \(error.message!) (\(error.code!))")
                } else {
//                    let topResponse = processorOutput.responses[0]
//                    if let equationMatrix = self.parseChemicalEquation(topResponse.fullTextAnnotation?.text ?? nil) {
//                        let solution = solve(matrix: equationMatrix, for: self.zeroVectorWithSize(equationMatrix.rows))
//                        print(solution)
//                    }
                }
            } else {
                print("Received no API response")
            }
            self.requestInProgress = false
            self.equationStr = nil
            self.activeUploadSessions -= 1
            print("Request finished.")
            DispatchQueue.main.async {
                self.hideActivityIndicator()
            }
        })
    }
    
    private func balanceEquation(_ equationStr: String) -> String {
        let equation = ChemicalEquation(equationStr: equationStr)
        let solution = equation.balance();
        
        var result = ""
        
        result += "Equation string: \(equationStr)\n"
        result += "Equation matrix:\n\(equation.matrix())\n"
        result += "LHS elements: \(equation.getLhsElements())\n"
        result += "RHS elements: \(equation.getRhsElements())\n"
        result += "Element set: \(equation.elements())\n"
        result += "Solution vector(Unprocessed): \(equation.getUnProcessedSolution())\n"
        result += "Solution vector(Processed): \(solution!)\n"
        
        return result
    }
    
    private func displayActivityIndicator() {
        activityIndicator.isHidden = false
        activityIndicator.startAnimating()
    }
    
    private func hideActivityIndicator() {
        activityIndicator.isHidden = true
        activityIndicator.stopAnimating()
    }
}

extension UIImage {
    func toBase64() -> String? {
        guard let imageData = self.pngData() else { return nil }
        return imageData.base64EncodedString()
    }
}
