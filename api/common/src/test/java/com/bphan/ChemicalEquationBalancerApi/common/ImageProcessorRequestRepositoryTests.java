package com.bphan.ChemicalEquationBalancerApi.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bphan.ChemicalEquationBalancerApi.common.jdbc.ImageProcessorRequestRepository;
import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.BoundingBox;
import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.BoundingBoxDiff;
import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.StoredRequestInfo;
import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.StoredRequestInfoApiResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = ImageProcessorRequestRepository.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.bphan.ChemicalEquationBalancerApi")
class ImageProcessorRequestRepositoryTests {

    String requestId = "testId";
    String requestUrl = "testUrl";
    long requestStartTime = 1582606167042L;
    long requestEndTime = 1582606167042L;
    String requestEquationString = "testEquationString";
    BoundingBox boundingBox = new BoundingBox("testId", requestId, 0, 0, 0, 0, "TEST_REGION");
    BoundingBox[] deletions = {boundingBox};
    BoundingBoxDiff boundingBoxDeletionDiff = new BoundingBoxDiff(requestId, null, deletions);

    @Autowired
    ImageProcessorRequestRepository requestRepository;

    @BeforeEach
    public void before() throws Exception {
        requestRepository.deleteRequest(requestId);
        requestRepository.updateBoundingBoxes(boundingBoxDeletionDiff);
    }

    @AfterEach
    public void after() throws Exception {
        requestRepository.deleteRequest(requestId);
        requestRepository.updateBoundingBoxes(boundingBoxDeletionDiff);
    }

    @Test
    public void testCrudOperations() {
        //  Test request upload
        StoredRequestInfoApiResponse requestUploadResponse = requestRepository.uploadRequestInfo(requestId, requestUrl,
                requestStartTime, requestEndTime, requestEquationString);

        assertEquals("success", requestUploadResponse.getStatus());
        assertEquals("", requestUploadResponse.getDescription());

        //  Test request fetch 
        StoredRequestInfo request = requestRepository.getRequestWithId(requestId);

        assertEquals(requestId, request.getId());
        assertEquals(requestUrl, request.gets3ImageUrl());
        assertEquals(requestStartTime, request.getGcpRequestStartTimeMs());
        assertEquals(requestEndTime, request.getGcpRequestEndTimeMs());
        assertEquals(requestEquationString, request.getUserInputtedChemicalEquationString());

        //  Test fetching the labeling status of a request
        requestRepository.updateLabelingStatusForRequest(requestId, "TEST_STATUS");
        assertEquals("TEST_STATUS", requestRepository.getLabelingStatusForRequest(requestId, "TEST_STATUS").toStr() );

        // Check that there are no bounding boxes for this request yet
        assertEquals(0, (requestRepository.numBoundingBoxesForRequest(requestId)));

        // Test adding a new bounding box and removing a bounding box
        BoundingBox boundingBox = new BoundingBox("testId", requestId, 0, 0, 0, 0, "TEST_REGION");
        BoundingBox[] insertions = {boundingBox};
        BoundingBoxDiff boundingBoxInsertDiff = new BoundingBoxDiff(requestId, insertions, null);

        requestRepository.updateBoundingBoxes(boundingBoxInsertDiff);
        assertEquals(1, requestRepository.numBoundingBoxesForRequest(requestId));

        requestRepository.updateBoundingBoxes(boundingBoxDeletionDiff);
        assertEquals(0, requestRepository.numBoundingBoxesForRequest(requestId));

        // Delete the request we just added
        StoredRequestInfoApiResponse requestDeletionResponse = requestRepository.deleteRequest(requestId);

        assertEquals("success", requestDeletionResponse.getStatus());
        assertEquals("", requestDeletionResponse.getDescription());
    }
}
