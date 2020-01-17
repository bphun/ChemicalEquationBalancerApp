DROP TABLE IF EXISTS ImageProcessorRequestInfo;

CREATE TABLE ImageProcessorRequestInfo(
    id                                      VARCHAR(255) NOT NULL PRIMARY KEY,
    imageString                             TEXT,
    gcpRequestStartTimeMs                   INT,
    gcpRequestEndTimeMs                     INT,
    userInputtedChemicalEquationString      VARCHAR(255),
    verifiedChemicalEquationString          VARCHAR(255),
    gcpIdentifiedChemicalEquationString     VARCHAR(255),
    onDeviceImageProcessStartTime           INT,
    onDeviceImageProcessEndTime             INT,
    onDeviceImageProcessDeviceName          VARCHAR(255)
);