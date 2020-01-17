// package com.bphan.ChemicalEquationBalancerAppServer.DatabaseConnector;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Repository;

// @Repository
// public class JdbcImageProcessorRequestRepository implements ImageProcessorRequestRepository {

//     @Autowired
//     private JdbcTemplate jdbcTemplate;

//     @Override
//     public void uploadRequestInfo(String requestId, String base64EncodedImage, long requestStartTime,
//             long requestEndTime) {
//         jdbcTemplate.update(
//                 "INSERT INTO ImageProcessorRequestInfo (id, imageString, gcpRequestStartTimeMs, gcpRequestEndTimeMs)"
//                         + "VALUES (?, ?, ?, ?)",
//                 requestId, base64EncodedImage, requestStartTime, requestEndTime);

//     }

// }