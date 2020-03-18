USE ChemicalEquationBalancerDatabase;

DROP TABLE IF EXISTS ChemicalEquationRegion;
DROP TABLE IF EXISTS ImageProcessorRequestInfo;
CREATE TABLE ImageProcessorRequestInfo(
    id                                      						CHAR(36) NOT NULL PRIMARY KEY,
    s3ImageUrl 												CHAR(120),
    gcpRequestStartTimeMs                   		BIGINT,
    gcpRequestEndTimeMs                     		BIGINT,
    userInputtedChemicalEquationString     VARCHAR(255),
    verifiedChemicalEquationString				VARCHAR(255),
    gcpIdentifiedChemicalEquationString     VARCHAR(255),
    onDeviceImageProcessStartTime			LONG,
    onDeviceImageProcessEndTime            LONG,
    onDeviceImageProcessDeviceName      VARCHAR(255)
) ENGINE=INNODB;

CREATE TABLE ChemicalEquationRegion(
	id                                      						CHAR(36) NOT NULL PRIMARY KEY,
	imageProcessorRequestInfoId				CHAR(36),
	originX														INT,
    originY														INT,
    width														INT,
    height														INT,
    
    INDEX imageProcessorRequestInfoIndex(imageProcessorRequestInfoId),
    FOREIGN KEY (imageProcessorRequestInfoId)
    REFERENCES ImageProcessorRequestInfo(id)
    ON DELETE CASCADE
    
) ENGINE=INNODB;

-- CREATE TABLE ImageProcessorRequestInfo(id VARCHAR(255) NOT NULL PRIMARY KEY, imageString TEXT, gcpRequestStartTimeMs BIGINT, gcpRequestEndTimeMs BIGINT, userInputtedChemicalEquationString VARCHAR(255), verifiedChemicalEquationString VARCHAR(255), gcpIdentifiedChemicalEquationString VARCHAR(255), onDeviceImageProcessStartTime INT, onDeviceImageProcessEndTime INT, onDeviceImageProcessDeviceName VARCHAR(255));