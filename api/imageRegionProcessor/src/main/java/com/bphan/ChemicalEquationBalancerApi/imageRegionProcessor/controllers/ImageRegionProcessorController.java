package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.controllers;

import com.bphan.ChemicalEquationBalancerApi.common.amazon.AwsSqsClient;
import com.bphan.ChemicalEquationBalancerApi.common.models.ImageRegion;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageRegionProcessor.ImageRegionExtractor;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.apiInterfaces.ImageProcessorApiInterface;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.models.ImageTransformerResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("imageProcessor/extract")
public class ImageRegionProcessorController {

  @Autowired private ImageRegionExtractor imageRegionExtractor;

  @Autowired private ImageProcessorApiInterface imageProcessorApiInterface;

  @Autowired private TaskExecutor regionExtractorTaskExecutor;

  @Autowired private AwsSqsClient sqsClient;

  private final String frontendHostname = "*";

  private Logger logger = Logger.getLogger(ImageRegionProcessorController.class.getName());

  @PostConstruct
  public void init() {
    regionExtractorTaskExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            while (true) {
              String requestId = sqsClient.popMessageStr().replace("\"", "");
              if (requestId != null && requestId.trim() != "") {
                logger.log(Level.INFO, "Received region extraction request req_id=" + requestId);
                getCroppedRegionsForRequest(requestId, false);
              }
            }
          }
        });
  }

  @CrossOrigin(origins = frontendHostname)
  @GetMapping("/regions")
  public Map<String, List<ImageTransformerResponse>> getCroppedRegionsForRequest(
      @RequestParam(value = "rid", required = true) String requestId,
      @RequestParam(value = "ir", required = false) boolean includeRegionInResponse) {
    List<ImageRegion> regions = imageProcessorApiInterface.getRegionsForRequest(requestId);

    return extractRegionImagesFromRegionList(regions, includeRegionInResponse);
  }

  @CrossOrigin(origins = frontendHostname)
  @GetMapping("/regions/all")
  public Map<String, List<ImageTransformerResponse>> getAllCroppedRegions(
      @RequestParam(value = "ir", required = false) boolean includeRegionInResponse) {
    List<ImageRegion> regions = imageProcessorApiInterface.getAllRegions();

    return extractRegionImagesFromRegionList(regions, includeRegionInResponse);
  }

  @CrossOrigin(origins = frontendHostname)
  @GetMapping(value = "/regions/download", produces = "application/zip")
  public byte[] downloadCroppedRegionsForRequest(
      @RequestParam(value = "rid", required = true) String requestId,
      HttpServletResponse response) {
    Map<String, List<ImageTransformerResponse>> regions =
        getCroppedRegionsForRequest(requestId, true);

    response.addHeader(
        "Content-Disposition", "attachment; filename=\"" + requestId + "_regions.zip\"");

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
    ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

    List<File> files = new ArrayList<>();
    // populate files

    try {
      for (File file : files) {
        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
        FileInputStream fileInputStream = new FileInputStream(file);

        IOUtils.copy(fileInputStream, zipOutputStream);

        fileInputStream.close();
        zipOutputStream.closeEntry();
      }

      if (zipOutputStream != null) {
        zipOutputStream.finish();
        zipOutputStream.flush();
        IOUtils.closeQuietly(zipOutputStream);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    IOUtils.closeQuietly(bufferedOutputStream);
    IOUtils.closeQuietly(byteArrayOutputStream);

    return byteArrayOutputStream.toByteArray();
  }

  @CrossOrigin(origins = frontendHostname)
  @GetMapping(value = "/regions/download/all", produces = "application/zip")
  public void downloadAllCroppedRegions(HttpServletResponse response) {
    Map<String, List<ImageTransformerResponse>> regions = getAllCroppedRegions(true);

    response.addHeader("Content-Disposition", "attachment; filename=\"all_regions.zip\"");
  }

  private Map<String, List<ImageTransformerResponse>> extractRegionImagesFromRegionList(
      List<ImageRegion> regions, boolean includeRegionInResponse) {
    if (regions.size() == 0) {
      return null;
    }

    return imageRegionExtractor.createRegionImages(regions, includeRegionInResponse);
  }
}
