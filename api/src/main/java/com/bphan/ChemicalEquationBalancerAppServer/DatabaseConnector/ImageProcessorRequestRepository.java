package com.bphan.ChemicalEquationBalancerAppServer.DatabaseConnector;

import java.util.List;

import com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels.StoredRequestInfo;
import com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels.StoredRequestInfoApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Configuration
public class ImageProcessorRequestRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void uploadRequestInfo(String requestId, String s3ImageUrl, long requestStartTime, long requestEndTime) {
        jdbcTemplate.update("INSERT INTO ImageProcessorRequestInfo"
        + "(id, s3ImageUrl, gcpRequestStartTimeMs, gcpRequestEndTimeMs)"
        + "VALUES (?, ?, ?, ?)",
        requestId, s3ImageUrl, requestStartTime, requestEndTime);
    }

    public List<StoredRequestInfo> getRequestList() {
        return jdbcTemplate.query("SELECT * FROM ImageProcessorRequestInfo",
            (resource, rowNum) ->
                                new StoredRequestInfo(
                                    resource.getString("id"),
                                    resource.getString("s3ImageUrl"),
                                    resource.getString("userInputtedChemicalEquationString"),
                                    resource.getLong("gcpRequestStartTimeMs"),
                                    resource.getLong("gcpRequestEndTimeMs"),
                                    resource.getString("verifiedChemicalEquationString"),
                                    resource.getString("gcpIdentifiedChemicalEquationString"),
                                    resource.getLong("onDeviceImageProcessStartTime"),
                                    resource.getLong("onDeviceImageProcessEndTime"),
                                    resource.getString("onDeviceImageProcessDeviceName"),
                                    resource.getBoolean("chemicalEquationStringVerified")
                                )
        );
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

    public StoredRequestInfoApiResponse updateVerificationStatus(String id, Boolean value) {
        return doValueUpdate(id, "chemicalEquationStringVerified", value);
    }

    private StoredRequestInfoApiResponse doValueUpdate(String id, String valueId, Object value) {
        StoredRequestInfoApiResponse response;
        try {
            int changedRows = jdbcTemplate.update("UPDATE ImageProcessorRequestInfo " +
                                "SET " + valueId.toString() + " = ? " +
                                "WHERE id = ?", value, id);
            if (changedRows > 0) {
                response = new StoredRequestInfoApiResponse("success", "");
            } else {
                response = new StoredRequestInfoApiResponse("error", "Invalid resource ID");
            }
        } catch (DataAccessException e) {
            response = new StoredRequestInfoApiResponse("error", e.getLocalizedMessage());
        }
        return response;
    }
}