package com.app.domain.service;

import com.app.domain.model.InfoRequest;
import com.app.domain.model.InfoResponse;
import java.util.List;

public interface InfoService {
  List<InfoResponse> getInfos();
  InfoResponse getInfo(String id);
  InfoResponse updateInfo(String id, InfoRequest req);
  InfoResponse generateInfo(InfoRequest request);
  void deleteInfo(String id);
}
