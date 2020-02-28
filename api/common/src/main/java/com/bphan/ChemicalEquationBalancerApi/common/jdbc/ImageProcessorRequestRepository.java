package com.bphan.ChemicalEquationBalancerApi.common.jdbc;

import java.util.List;
import java.util.UUID;

import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.BoundingBox;
import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.BoundingBoxDiff;
import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.RequestLabelingStatus;
import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.StoredRequestInfo;
import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.StoredRequestInfoApiResponse;
import com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels.StoredRequestInfoId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.stereotype.Repository;

@Repository
@Configuration
public class ImageProcessorRequestRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public StoredRequestInfoApiResponse uploadRequestInfo(String requestId, String s3ImageUrl, long requestStartTime,
            long requestEndTime, String equationString) {

        StoredRequestInfoApiResponse response;

        if (requestId != null && requestId.trim().length() > 0) {
            try {
                int changedRows = jdbcTemplate.update(
                        "INSERT INTO ImageProcessorRequestInfo"
                                + "(id, s3ImageUrl, gcpRequestStartTimeMs, gcpRequestEndTimeMs, "
                                + "userInputtedChemicalEquationString)" + " VALUES (?, ?, ?, ?, ?)",
                        requestId, s3ImageUrl, requestStartTime, requestEndTime, equationString);
                if (changedRows > 0) {
                    response = new StoredRequestInfoApiResponse("success", "");
                } else {
                    response = new StoredRequestInfoApiResponse("error", "SQL error");
                }
            } catch (DataAccessException e) {
                response = new StoredRequestInfoApiResponse("error", e.getLocalizedMessage());
            }
        } else {
            response = new StoredRequestInfoApiResponse("error", "Invalid request ID");
        }

        return response;
    }

    public List<StoredRequestInfo> getRequestList() {
        return jdbcTemplate.query("SELECT * FROM ImageProcessorRequestInfo ORDER BY gcpRequestStartTimeMs ASC",
                (resource, rowNum) -> new StoredRequestInfo(resource.getString("id"), resource.getString("s3ImageUrl"),
                        resource.getString("userInputtedChemicalEquationString"),
                        resource.getLong("gcpRequestStartTimeMs"), resource.getLong("gcpRequestEndTimeMs"),
                        resource.getString("verifiedChemicalEquationString"),
                        resource.getString("gcpIdentifiedChemicalEquationString"),
                        resource.getLong("onDeviceImageProcessStartTime"),
                        resource.getLong("onDeviceImageProcessEndTime"),
                        resource.getString("onDeviceImageProcessDeviceName"),
                        getLabelingStatusForRequest(resource.getString("id"), resource.getString("labelingStatus"))));
    }

    public StoredRequestInfo getRequestWithId(String requestId) {
        return jdbcTemplate.query("SELECT * FROM ImageProcessorRequestInfo " + "WHERE id='" + requestId + "'",
                (resource, rowNum) -> new StoredRequestInfo(resource.getString("id"), resource.getString("s3ImageUrl"),
                        resource.getString("userInputtedChemicalEquationString"),
                        resource.getLong("gcpRequestStartTimeMs"), resource.getLong("gcpRequestEndTimeMs"),
                        resource.getString("verifiedChemicalEquationString"),
                        resource.getString("gcpIdentifiedChemicalEquationString"),
                        resource.getLong("onDeviceImageProcessStartTime"),
                        resource.getLong("onDeviceImageProcessEndTime"),
                        resource.getString("onDeviceImageProcessDeviceName"),
                        getLabelingStatusForRequest(resource.getString("id"), resource.getString("labelingStatus"))))
                .get(0);
    }

    public RequestLabelingStatus getLabelingStatusForRequest(String requestId, String statusStr) {
        RequestLabelingStatus labelingStatus = RequestLabelingStatus.INCOMPLETE;
        int numBoundingBoxes = numBoundingBoxesForRequest(requestId);

        if (statusStr == null) {
            if (numBoundingBoxes >= 1) {
                labelingStatus = RequestLabelingStatus.LABELED;
            }
        } else {
            labelingStatus = RequestLabelingStatus.valueOf(statusStr);
        }

        return labelingStatus;
    }

    public int numBoundingBoxesForRequest(String requestId) {
        RowCountCallbackHandler countCallback = new RowCountCallbackHandler();

        jdbcTemplate.query(
                "SELECT * FROM ChemicalEquationBoundingBox WHERE imageProcessorRequestInfoId=\'" + requestId + "\';",
                countCallback);

        return countCallback.getRowCount();
    }

    public StoredRequestInfoApiResponse updateLabelingStatusForRequest(String requestId, String status) {
        return doValueUpdate(requestId, "labelingStatus", status);
    }

    public StoredRequestInfoApiResponse updateUserInputtedChemicalEquationString(String id, String value) {
        return doValueUpdate(id, "userInputtedChemicalEquationString", value);
    }

    public StoredRequestInfoApiResponse updateVerifiedChemicalEquationString(String id, String value) {
        return doValueUpdate(id, "verifiedChemicalEquationString", value);
    }

    public StoredRequestInfoApiResponse updateGcpIdentifiedChemicalEquationString(String id, String value) {
        return doValueUpdate(id, "gcpIdentifiedChemicalEquationString", value);
    }

    public StoredRequestInfoApiResponse updateOnDeviceImageProcessStartTime(String id, String value) {
        return doValueUpdate(id, "onDeviceImageProcessStartTime", value);
    }

    public StoredRequestInfoApiResponse updateOnDeviceImageProcessEndTime(String id, String value) {
        return doValueUpdate(id, "onDeviceImageProcessEndTime", value);
    }

    public StoredRequestInfoApiResponse updateOnDeviceImageProcessDeviceName(String id, String value) {
        return doValueUpdate(id, "onDeviceImageProcessDeviceName", value);
    }

    private StoredRequestInfoApiResponse deleteBoundingBox(BoundingBox boundingBox) {
        StoredRequestInfoApiResponse response;

        try {
            String query = "DELETE FROM ChemicalEquationBoundingBox WHERE id=?";
            int changedRows = jdbcTemplate.update(query, boundingBox.getId());

            if (changedRows > 0) {
                response = new StoredRequestInfoApiResponse("success", "");
            } else {
                response = new StoredRequestInfoApiResponse("error", "SQL error");
            }
        } catch (Exception e) {
            response = new StoredRequestInfoApiResponse("error", e.getLocalizedMessage());
        }

        return response;
    }

    private StoredRequestInfoApiResponse addBoundingBox(BoundingBox boundingBox) {
        if (boundingBox.getId() == null || boundingBox.getId().equals("")) {
            boundingBox.setId(UUID.randomUUID().toString());
        }
        StoredRequestInfoApiResponse response;

        try {
            String query = "INSERT INTO ChemicalEquationBoundingBox"
                    + "(id, imageProcessorRequestInfoId, originX, originY, width, height, tags)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE originX=?,"
                    + "originY=?, width=?, height=?, tags=?";

            int changedRows = jdbcTemplate.update(query, boundingBox.getId(), boundingBox.getRequestInfoId(),
                    boundingBox.getOriginX(), boundingBox.getOriginY(), boundingBox.getWidth(), boundingBox.getHeight(),
                    boundingBox.tagStr(), boundingBox.getOriginX(), boundingBox.getOriginX(), boundingBox.getWidth(),
                    boundingBox.getHeight(), boundingBox.tagStr());

            if (changedRows > 0) {
                response = new StoredRequestInfoApiResponse("success", "");
            } else {
                response = new StoredRequestInfoApiResponse("error", "SQL error");
            }

        } catch (Exception e) {
            response = new StoredRequestInfoApiResponse("error", e.getLocalizedMessage());
        }

        return response;
    }

    public StoredRequestInfoApiResponse updateBoundingBoxes(BoundingBoxDiff boundingBoxDiff) {
        StoredRequestInfoApiResponse response = null;

        BoundingBox[] boundingBoxesToUpdate = boundingBoxDiff.getModified();
        BoundingBox[] boundingBoxesToRemove = boundingBoxDiff.getDeleted();

        if (boundingBoxesToUpdate != null) {
            for (BoundingBox boundingBox : boundingBoxDiff.getModified()) {
                response = addBoundingBox(boundingBox);

                if (response.getStatus() == "error") {
                    break;
                }
            }
        }

        if (boundingBoxesToRemove != null) {
            for (BoundingBox boundingBox : boundingBoxDiff.getDeleted()) {
                response = deleteBoundingBox(boundingBox);

                if (response.getStatus() == "error") {
                    break;
                }
            }
        }

        String requestId = boundingBoxDiff.getRequestId();
        updateLabelingStatusForRequest(requestId, numBoundingBoxesForRequest(requestId) > 0 ? "LABELED" : "INCOMPLETE");

        return response;
    }

    public List<BoundingBox> getBoundingBoxesForRequest(String requestId) {
        return jdbcTemplate.query(
                "SELECT * FROM ChemicalEquationBoundingBox" + " WHERE imageProcessorRequestInfoId='" + requestId + "'",
                (resource, rowNum) -> new BoundingBox(resource.getString("id"),
                        resource.getString("imageProcessorRequestInfoId"), resource.getInt("originX"),
                        resource.getInt("originY"), resource.getInt("width"), resource.getInt("height"),
                        resource.getString("tags").split(",")));
    }

    public StoredRequestInfoApiResponse getNextRequestIdAfterTimestamp(String timestamp) {
        StoredRequestInfoApiResponse response;

        try {
            response = jdbcTemplate.query(
                    "SELECT * FROM ImageProcessorRequestInfo WHERE " + "gcpRequestStartTimeMs > "
                            + Long.parseLong(timestamp) + " ORDER BY " + "gcpRequestStartTimeMs ASC LIMIT 0,1;",
                    (resource, rowNum) -> new StoredRequestInfoId(resource.getString("id"))).get(0);
        } catch (Exception e) {
            response = new StoredRequestInfoApiResponse("error", "No available requests");
        }

        return response;
    }

    public StoredRequestInfoApiResponse getPreviousRequestIdBeforeTimestamp(String timestamp) {
        StoredRequestInfoApiResponse response;

        try {
            response = jdbcTemplate.query(
                    "SELECT * FROM ImageProcessorRequestInfo WHERE " + "gcpRequestStartTimeMs < "
                            + Long.parseLong(timestamp) + " ORDER BY " + "gcpRequestStartTimeMs DESC LIMIT 0,1;",
                    (resource, rowNum) -> new StoredRequestInfoId(resource.getString("id"))).get(0);
        } catch (Exception e) {
            response = new StoredRequestInfoApiResponse("error", "No available requests");
        }

        return response;
    }

    public StoredRequestInfoApiResponse deleteRequest(String requestId) {
        StoredRequestInfoApiResponse response;

        if (requestId != null && requestId.trim().length() > 0) {
            try {
                int rowsAffected = jdbcTemplate.update("DELETE FROM ImageProcessorRequestInfo WHERE id = ?", requestId);
                if (rowsAffected > 0) {
                    response = new StoredRequestInfoApiResponse("success", "");
                } else {
                    response = new StoredRequestInfoApiResponse("error", "SQL error");
                }
            } catch (Exception e) {
                response = new StoredRequestInfoApiResponse("error", e.getLocalizedMessage());
            }
        } else {
            response = new StoredRequestInfoApiResponse("error", "Invalid request ID");
        }

        return response;
    }

    private StoredRequestInfoApiResponse doValueUpdate(String requestId, String valueId, Object value) {
        StoredRequestInfoApiResponse response;

        if (requestId != null && requestId.trim().length() > 0) {
            try {
                int changedRows = jdbcTemplate.update(
                        "UPDATE ImageProcessorRequestInfo " + "SET " + valueId.toString() + " = ? " + "WHERE id = ?",
                        value, requestId);
                if (changedRows > 0) {
                    response = new StoredRequestInfoApiResponse("success", "");
                } else {
                    response = new StoredRequestInfoApiResponse("error", "SQL error");
                }
            } catch (DataAccessException e) {
                response = new StoredRequestInfoApiResponse("error", e.getLocalizedMessage());
            }
        } else {
            response = new StoredRequestInfoApiResponse("error", "Invalid request ID");
        }

        return response;
    }
}