package org.help.hemah.service.help_request;

import org.help.hemah.helper.req_model.NewHelpRequestModel;
import org.help.hemah.model.help_request.HelpRequest;

import java.util.List;

public interface HelpRequestService {
    List<HelpRequest> getHelpRequestFeed();

    List<HelpRequest> getDisabledHelpRequests();

    Long addNewHelpRequest(NewHelpRequestModel newHelpRequestModel);

    void removeHelpRequest(Long helpRequestId);

    void markHelpRequestAsComplete(Long helpRequestId);

}
