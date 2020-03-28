package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.jdbc;

import java.util.List;
import java.util.UUID;

import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.storedRequestInfoModels.RegionDiff;
import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.storedRequestInfoModels.RequestLabelingStatus;
import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.storedRequestInfoModels.StoredRequestInfo;
import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.storedRequestInfoModels.StoredRequestInfoId;
import com.bphan.ChemicalEquationBalancerApi.common.ResponseModels.ApiResponse;
import com.bphan.ChemicalEquationBalancerApi.common.amazon.AwsS3Client;
import com.bphan.ChemicalEquationBalancerApi.common.models.ImageRegion;

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

    @Autowired
    private AwsS3Client s3Client;

    public ApiResponse uploadRequestInfo(String requestId, String s3ImageUrl, long requestStartTime,
            long requestEndTime, String equationString) {

        ApiResponse response;

        if (requestId != null && requestId.trim().length() > 0) {
            try {
                int changedRows = jdbcTemplate.update(
                        "INSERT INTO ImageProcessorRequestInfo"
                                + "(id, s3ImageUrl, gcpRequestStartTimeMs, gcpRequestEndTimeMs, "
                                + "userInputtedChemicalEquationString)" + " VALUES (?, ?, ?, ?, ?)",
                        requestId, s3ImageUrl, requestStartTime, requestEndTime, equationString);
                if (changedRows > 0) {
                    response = new ApiResponse("success", "");
                } else {
                    response = new ApiResponse("error", "SQL error");
                }
            } catch (DataAccessException e) {
                response = new ApiResponse("error", e.getLocalizedMessage());
            }
        } else {
            response = new ApiResponse("error", "Invalid request ID");
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

    public List<String> getRequestIdList() {
        return jdbcTemplate.queryForList("select id from ImageProcessorRequestInfo", String.class);
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
        int numRegions = numRegionsForRequest(requestId);

        if (statusStr == null) {
            if (numRegions >= 1) {
                labelingStatus = RequestLabelingStatus.LABELED;
            }
        } else {
            labelingStatus = RequestLabelingStatus.valueOf(statusStr);
        }

        return labelingStatus;
    }

    public int numRegionsForRequest(String requestId) {
        RowCountCallbackHandler countCallback = new RowCountCallbackHandler();

        jdbcTemplate.query(
                "SELECT * FROM ChemicalEquationRegions WHERE imageProcessorRequestInfoId=\'" + requestId + "\';",
                countCallback);

        return countCallback.getRowCount();
    }

    public ApiResponse updateS3ImageUrlForRegion(String regionId, String url) {
        return doValueUpdate(regionId, "ChemicalEquationRegions", "s3ImageUrl", url);
    }

    public ApiResponse updateEquationStrForRegion(String regionId, String equationStr) {
        return doValueUpdate(regionId, "ChemicalEquationRegions", "equationStr", equationStr);
    }

    public ApiResponse updateLabelingStatusForRequest(String requestId, String status) {
        return doValueUpdate(requestId, "ImageProcessorRequestInfo", "labelingStatus", status);
    }

    public ApiResponse updateUserInputtedChemicalEquationString(String id, String value) {
        return doValueUpdate(id, "ImageProcessorRequestInfo", "userInputtedChemicalEquationString", value);
    }

    public ApiResponse updateVerifiedChemicalEquationString(String id, String value) {
        return doValueUpdate(id, "ImageProcessorRequestInfo", "verifiedChemicalEquationString", value);
    }

    public ApiResponse updateGcpIdentifiedChemicalEquationString(String id, String value) {
        return doValueUpdate(id, "ImageProcessorRequestInfo", "gcpIdentifiedChemicalEquationString", value);
    }

    public ApiResponse updateOnDeviceImageProcessStartTime(String id, String value) {
        return doValueUpdate(id, "ImageProcessorRequestInfo", "onDeviceImageProcessStartTime", value);
    }

    public ApiResponse updateOnDeviceImageProcessEndTime(String id, String value) {
        return doValueUpdate(id, "ImageProcessorRequestInfo", "onDeviceImageProcessEndTime", value);
    }

    public ApiResponse updateOnDeviceImageProcessDeviceName(String id, String value) {
        return doValueUpdate(id, "ImageProcessorRequestInfo", "onDeviceImageProcessDeviceName", value);
    }

    private ApiResponse deleteRegion(ImageRegion region) {
        ApiResponse response;

        try {
            String query = "DELETE FROM ChemicalEquationRegions WHERE id=?";
            int changedRows = jdbcTemplate.update(query, region.getId());

            if (changedRows > 0) {
                response = new ApiResponse("success", "");
            } else {
                response = new ApiResponse("error", "SQL error");
            }
        } catch (Exception e) {
            response = new ApiResponse("error", e.getLocalizedMessage());
        }

        return response;
    }

    private ApiResponse addRegion(ImageRegion region) {
        if (region.getId() == null || region.getId().equals("")) {
            region.setId(UUID.randomUUID().toString());
        }
        ApiResponse response;

        try {
            String query = "INSERT INTO ChemicalEquationRegions "
                    + "(id, imageProcessorRequestInfoId, regionClass, originX, originY, width, height, viewportWidth, viewportHeight, parentImageWidth, parentImageHeight, tags) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE regionClass=?, originX=?, "
                    + "originY=?, width=?, height=?, viewportWidth=?, viewportHeight=?, parentImageWidth=?, parentImageHeight=?, tags=?;";

            int changedRows = jdbcTemplate.update(query, region.getId(), region.getRequestInfoId(),
                    region.getRegionClass(), region.getOriginX(), region.getOriginY(), region.getWidth(),
                    region.getHeight(), region.getViewportWidth(), region.getViewportHeight(),
                    region.getParentImageWidth(), region.getParentImageHeight(), region.tagStr(),
                    region.getRegionClass(), region.getOriginX(), region.getOriginY(), region.getWidth(),
                    region.getHeight(), region.getViewportWidth(), region.getViewportHeight(),
                    region.getParentImageWidth(), region.getParentImageHeight(), region.tagStr());

            if (changedRows > 0) {
                response = new ApiResponse("success", "");
            } else {
                response = new ApiResponse("error", "SQL error");
            }

        } catch (Exception e) {
            response = new ApiResponse("error", e.getLocalizedMessage());
        }

        return response;
    }

    public ApiResponse updateRegions(RegionDiff regionDiff) {
        ApiResponse response = null;

        ImageRegion[] regionsToUpdate = regionDiff.getModified();
        ImageRegion[] regionsToRemove = regionDiff.getDeleted();

        if (regionsToUpdate != null) {
            for (ImageRegion region : regionDiff.getModified()) {
                response = addRegion(region);
                
                if (response.getStatus() == "error") {
                    break;
                }
            }
        }

        if (regionsToRemove != null) {
            for (ImageRegion region : regionDiff.getDeleted()) {
                response = deleteRegion(region);
                s3Client.deleteImageByName(region.getRequestInfoId() + "_" + region.getId());
                
                if (response.getStatus() == "error") {
                    break;
                }
            }
        }

        String requestId = regionDiff.getRequestId();
        updateLabelingStatusForRequest(requestId, numRegionsForRequest(requestId) > 0 ? "LABELED" : "INCOMPLETE");

        return response;
    }

    public List<ImageRegion> getRegionsForRequest(String requestId) {
        return jdbcTemplate.query(
                "SELECT * FROM ChemicalEquationRegions" + " WHERE imageProcessorRequestInfoId='" + requestId + "'",
                (resource, rowNum) -> new ImageRegion(resource.getString("id"),
                        resource.getString("imageProcessorRequestInfoId"), resource.getString("regionClass"),
                        resource.getString("s3ImageUrl"), resource.getString("equationStr"), resource.getInt("originX"),
                        resource.getInt("originY"), resource.getInt("width"), resource.getInt("height"),
                        resource.getInt("viewportWidth"), resource.getInt("viewportHeight"),
                        resource.getInt("parentImageWidth"), resource.getInt("parentImageHeight"),
                        resource.getString("tags").split(",")));
    }

    public List<ImageRegion> getAllRegions() {
        return jdbcTemplate.query("SELECT * FROM ChemicalEquationRegions",
                (resource, rowNum) -> new ImageRegion(resource.getString("id"),
                        resource.getString("imageProcessorRequestInfoId"), resource.getString("regionClass"),
                        resource.getString("s3ImageUrl"), resource.getString("equationStr"), resource.getInt("originX"),
                        resource.getInt("originY"), resource.getInt("width"), resource.getInt("height"),
                        resource.getInt("viewportWidth"), resource.getInt("viewportHeight"),
                        resource.getInt("parentImageWidth"), resource.getInt("parentImageHeight"),
                        resource.getString("tags").split(",")));
    }

    public ApiResponse getNextRequestIdAfterTimestamp(String timestamp) {
        ApiResponse response;

        try {
            response = jdbcTemplate.query(
                    "SELECT * FROM ImageProcessorRequestInfo WHERE " + "gcpRequestStartTimeMs > "
                            + Long.parseLong(timestamp) + " ORDER BY " + "gcpRequestStartTimeMs ASC LIMIT 0,1;",
                    (resource, rowNum) -> new StoredRequestInfoId(resource.getString("id"))).get(0);
        } catch (Exception e) {
            response = new ApiResponse("error", "No available requests");
        }

        return response;
    }

    public ApiResponse getPreviousRequestIdBeforeTimestamp(String timestamp) {
        ApiResponse response;

        try {
            response = jdbcTemplate.query(
                    "SELECT * FROM ImageProcessorRequestInfo WHERE " + "gcpRequestStartTimeMs < "
                            + Long.parseLong(timestamp) + " ORDER BY " + "gcpRequestStartTimeMs DESC LIMIT 0,1;",
                    (resource, rowNum) -> new StoredRequestInfoId(resource.getString("id"))).get(0);
        } catch (Exception e) {
            response = new ApiResponse("error", "No available requests");
        }

        return response;
    }

    public ApiResponse deleteRequest(String requestId) {
        ApiResponse response;

        if (requestId != null && requestId.trim().length() > 0) {
            try {
                int rowsAffected = jdbcTemplate.update("DELETE FROM ImageProcessorRequestInfo WHERE id = ?", requestId);
                if (rowsAffected > 0) {
                    response = new ApiResponse("success", "");
                } else {
                    response = new ApiResponse("error", "SQL error");
                }
            } catch (Exception e) {
                response = new ApiResponse("error", e.getLocalizedMessage());
            }
        } else {
            response = new ApiResponse("error", "Invalid request ID");
        }

        return response;
    }

    private ApiResponse doValueUpdate(String requestId, String tableName, String valueId, Object value) {
        ApiResponse response;

        if (requestId != null && requestId.trim().length() > 0) {
            try {
                int changedRows = jdbcTemplate.update(
                        "UPDATE " + tableName + " SET " + valueId.toString() + " = ? " + "WHERE id = ?", value,
                        requestId);
                if (changedRows > 0) {
                    response = new ApiResponse("success", "");
                } else {
                    response = new ApiResponse("error", "SQL error");
                }
            } catch (DataAccessException e) {
                response = new ApiResponse("error", e.getLocalizedMessage());
            }
        } else {
            response = new ApiResponse("error", "Invalid request ID");
        }

        return response;
    }
}