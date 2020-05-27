package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.apiInterfaces;

import com.bphan.ChemicalEquationBalancerApi.common.ResponseModels.ApiResponse;
import com.bphan.ChemicalEquationBalancerApi.common.models.ImageRegion;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class ImageProcessorApiInterface {

  private Logger logger = Logger.getLogger(ImageProcessorApiInterface.class.getName());

  @Value("${requestsApi.hostname}")
  private String requestsApiHostname;

  @Value("${requestsApi.allRegionsEndpoint}")
  private String allRegionsEndpoint;

  @Value("${requestsApi.selectRegionsEndpoint}")
  private String selectRegionsEndpoint;

  @Value("${requestsApi.updateValueEndpoint}")
  private String updateValueEndpoint;

  private final RestTemplate restTemplate = new RestTemplate();

  private String selectRegionFetchUrl = "";
  private String allRegionsFetchUrl = "";
  private String updateValueUrl = "";

  @PostConstruct
  public void init() {
    selectRegionFetchUrl = requestsApiHostname + selectRegionsEndpoint;
    allRegionsFetchUrl = requestsApiHostname + allRegionsEndpoint;
    updateValueUrl = requestsApiHostname + updateValueEndpoint;
  }

  public List<ImageRegion> getRegionsForRequest(String requestId) {
    String url = selectRegionFetchUrl + "?rid=" + requestId;
    return fetchRegionsFromUrl(url);
  }

  public List<ImageRegion> getAllRegions() {
    return fetchRegionsFromUrl(allRegionsFetchUrl);
  }

  public void setImageUrlForRegion(String regionId, String imageUrl) {
    String requestUrl = updateValueUrl + "?rid=" + regionId + "&vid=imgUrl&v=" + imageUrl;
    this.restTemplate.getForObject(requestUrl, ApiResponse.class);
  }

  private List<ImageRegion> fetchRegionsFromUrl(String url) {
    return Arrays.asList(this.restTemplate.getForObject(url, ImageRegion[].class));
  }

  public <T> T getForObject(String url, Class<T> responseType) {
    T response = null;

    try {
      response = this.restTemplate.getForObject(url, responseType);
      logger.log(Level.INFO, "Remote REST request to " + url + " 200");
    } catch (Exception e) {
      logger.warning(e.getLocalizedMessage());
      e.printStackTrace();
    }

    return response;
  }
}
