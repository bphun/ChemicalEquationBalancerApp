USE ChemicalEquationBalancerDatabase;

DROP TABLE IF EXISTS ImageProcessorRequestInfo;

CREATE TABLE ImageProcessorRequestInfo
(
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    s3ImageUrl VARCHAR(255),
    gcpRequestStartTimeMs BIGINT,
    gcpRequestEndTimeMs BIGINT,
    userInputtedChemicalEquationString VARCHAR(255),
    verifiedChemicalEquationString VARCHAR(255),
    gcpIdentifiedChemicalEquationString VARCHAR(255),
    onDeviceImageProcessStartTime INT,
    onDeviceImageProcessEndTime INT,
    onDeviceImageProcessDeviceName VARCHAR(255)
);

-- CREATE TABLE ImageProcessorRequestInfo(id VARCHAR(255) NOT NULL PRIMARY KEY, imageString TEXT, gcpRequestStartTimeMs BIGINT, gcpRequestEndTimeMs BIGINT, userInputtedChemicalEquationString VARCHAR(255), verifiedChemicalEquationString VARCHAR(255), gcpIdentifiedChemicalEquationString VARCHAR(255), onDeviceImageProcessStartTime INT, onDeviceImageProcessEndTime INT, onDeviceImageProcessDeviceName VARCHAR(255));