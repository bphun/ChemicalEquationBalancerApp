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
    
    var frameExtractor: FrameExtractor!
    var shouldCaptureFrame = false
    var requestInProgress = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        frameExtractor = FrameExtractor()
        frameExtractor.delegate = self
    }
    
    @IBAction func imageCaptureButtonAction(_ sender: Any) {
        self.shouldCaptureFrame = true
    }
    
    func captured(image: UIImage) {
        self.imageView.image = image
        
        if shouldCaptureFrame && !requestInProgress {
            shouldCaptureFrame = false
            
            DispatchQueue.global().async {
                self.processImage(image)
            }
        }
    }
    
    func processImage(_ image: UIImage) {
        guard let base64EncodedImageString = image.toBase64() else { return }
        imageRequest(base64EncodedImage: base64EncodedImageString)
    }
    
    func imageRequest(base64EncodedImage: String) {
        let feature = ImageprocessorFeature(maxResults: 20, type: FeatureType.DocumentTextDetection.rawValue)
        let image = ImageProcessorImage(base64EncodedString: base64EncodedImage)
        let imageProcessorRequest = ImageProcessorRequest(image: image, feature: feature)
        let imageProcessorRequestBody = ImageProcessorRequestBody(request: imageProcessorRequest)
        let requestBodyJson = try! JSONEncoder().encode(imageProcessorRequestBody)
        let imageProcessingRequest = ApiRequest(resource: ImageProcessResource(data: requestBodyJson))
        
        requestInProgress = true
        
        imageProcessingRequest.run(withCompletionHandler: { (processorOutput) in
            print(processorOutput)
            if let processorOutput = processorOutput {
                if let error = processorOutput.responses[0].error {
                    print("API Bad Response: \(error.message) (\(error.code))")
                } else {
                    print("Received successful response")
                }
            } else {
                print("Received no API response")
            }
            self.requestInProgress = false
            print("Request finished. Capture available")
        })
        
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
}

extension UIImage {
    func toBase64() -> String? {
        guard let imageData = self.pngData() else { return nil }
        return imageData.base64EncodedString()
    }
}
