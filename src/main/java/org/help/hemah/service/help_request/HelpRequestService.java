package org.help.hemah.service.help_request;

import org.help.hemah.domain.help_request.HelpRequest;
import org.help.hemah.helper.req_model.HelpRequestModel;

import java.util.List;

public interface HelpRequestService {
    List<HelpRequest> getHelpRequestFeed();

    List<HelpRequest> getDisabledHelpRequests();

    Long addNewHelpRequest(HelpRequestModel helpRequestModel);

    void editHelpRequest(Long helpRequestId, HelpRequestModel helpRequestModel);

    void removeHelpRequest(Long helpRequestId);

    void markHelpRequestAsComplete(Long helpRequestId);

}
