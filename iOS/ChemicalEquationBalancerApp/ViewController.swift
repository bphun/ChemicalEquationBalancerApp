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
    
    var frameExtractor: FrameExtractor!
    var shouldCaptureFrame = false
    var requestInProgress = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        frameExtractor = FrameExtractor()
        frameExtractor.delegate = self
        activityIndicator.isHidden = true
    }
    
    @IBAction func imageCaptureButtonAction(_ sender: Any) {
        self.shouldCaptureFrame = true
    }
    
    func captured(image: UIImage) {
        self.imageView.image = image
        
        if shouldCaptureFrame && !requestInProgress {
            shouldCaptureFrame = false
            
            displayActivityIndicator()
            let shouldUploadImage = self.uploadImageSwitch.isOn
            DispatchQueue.global().async {
                print("Request start")
                self.processImage(image, shouldUploadImage: shouldUploadImage)
            }
        }
    }
    
    func processImage(_ image: UIImage, shouldUploadImage: Bool) {
        guard let base64EncodedImageString = image.toBase64() else { return }
        imageRequest(base64EncodedImage: base64EncodedImageString, shouldUploadImage: shouldUploadImage)
    }
    
    func imageRequest(base64EncodedImage: String, shouldUploadImage: Bool) {
        let feature = ImageprocessorFeature(maxResults: 20, type: FeatureType.DocumentTextDetection.rawValue)
        let image = ImageProcessorImage(base64EncodedString: base64EncodedImage)
        let imageProcessorRequest = ImageProcessorRequest(image: image, feature: feature)
        let imageProcessorRequestBody = ImageProcessorRequestBody(request: imageProcessorRequest)
        let requestBodyJson = try! JSONEncoder().encode(imageProcessorRequestBody)
        let imageProcessingRequest = ApiRequest(resource: ImageProcessResource(data: requestBodyJson, shouldUploadImage: shouldUploadImage))
        
        requestInProgress = true
//        print(requestBodyJson.prettyPrintedJSONString)
        imageProcessingRequest.run(withCompletionHandler: { (processorOutput) in
//            print(processorOutput)
            if let processorOutput = processorOutput {
                if let error = processorOutput.responses[0].error {
                    print("API Bad Response: \(error.message!) (\(error.code!))")
                } else {
                    let topResponse = processorOutput.responses[0]
                    if let equationMatrix = self.parseChemicalEquation(topResponse.fullTextAnnotation?.text ?? nil) {
                        let solution = solve(matrix: equationMatrix, for: self.zeroVectorWithSize(equationMatrix.rows))
                        print(solution)
                    }
                }
            } else {
                print("Received no API response")
            }
            self.requestInProgress = false
            print("Request finished. Capture available")
            DispatchQueue.main.async {
                self.hideActivityIndicator()
            }
        })
    }
    
    func parseChemicalEquation(_ input: String?) -> Matrix<Double>? {
        guard input != nil else { return nil }
        guard isValidChemicalEquation(input!) else { return nil }
        
        var matrix: Matrix<Double>?
        
        return nil
    }
    
    func isValidChemicalEquation(_ input: String) -> Bool {
        return true
    }

    func zeroVectorWithSize(_ n: Int) -> Vector<Double> {
        return Vector<Double>.init(repeating: 0.0, count: n)
    }
    
    func computeMatrix() {
        let system: [[Double]] = [
            [1, 0, 1, 4],
            [0, 0, 1, 2]
        ]
        let augmentVector: Vector<Double> = [1, 0]
        
        let systemMatrix = Matrix<Double>(twoDimArr: system)
        
        //print(invert(systemMatrix))
        print("Matrix:\n\(systemMatrix)")
        print("Augmented column: \(augmentVector)")
        
        print("Solution: \(solve(matrix: systemMatrix, for: augmentVector))")
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
        return imageData.base64EncodedString()/*.replacingOccurrences(of: "/", with: "")*/
    }
}