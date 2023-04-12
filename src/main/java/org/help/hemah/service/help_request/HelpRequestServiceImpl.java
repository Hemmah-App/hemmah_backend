package org.help.hemah.service.help_request;

import lombok.RequiredArgsConstructor;
import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.help_request.HelpRequest;
import org.help.hemah.domain.help_request.RequestStatus;
import org.help.hemah.helper.req_model.NewHelpRequestModel;
import org.help.hemah.repository.help_requset.HelpRequestRepository;
import org.help.hemah.service.auth.AuthenticationFacade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpRequestServiceImpl implements HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public List<HelpRequest> getDisabledHelpRequests() {
        Disabled disabled = authenticationFacade.getAuthenticatedDisabled();

        return helpRequestRepository
                .findAllByDisabled(disabled)
                .stream().filter(helpRequest -> helpRequest.getStatus() != RequestStatus.CANCELED).toList();
    }

    @Override
    public List<HelpRequest> getHelpRequestFeed() {
        return helpRequestRepository.findAllByStatus(RequestStatus.NEEDS_HELP);
    }

    @Override
    public Long addNewHelpRequest(NewHelpRequestModel newHelpRequestModel) {
        Disabled disabled = authenticationFacade.getAuthenticatedDisabled();

        HelpRequest helpRequest = new HelpRequest(
                newHelpRequestModel.getTitle(),
                newHelpRequestModel.getDescription(),
                newHelpRequestModel.getDate(),
                disabled,
                RequestStatus.NEEDS_HELP,
                newHelpRequestModel.getLocation()
        );

        helpRequest.setLongitude(newHelpRequestModel.getLongitude());
        helpRequest.setLatitude(newHelpRequestModel.getLatitude());

        return helpRequestRepository.save(helpRequest).getId();
    }

    @Override
    public void removeHelpRequest(Long helpRequestId) {
        Disabled disabled = authenticationFacade.getAuthenticatedDisabled();
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId).orElseThrow();

        if (!helpRequest.getDisabled().equals(disabled))
            throw new RuntimeException("You are not the owner of this help request");

        helpRequest.setStatus(RequestStatus.CANCELED);
        helpRequestRepository.save(helpRequest);
    }

    @Override
    public void markHelpRequestAsComplete(Long helpRequestId) {
        Disabled disabled = authenticationFacade.getAuthenticatedDisabled();
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId).orElseThrow();

        if (!helpRequest.getDisabled().equals(disabled))
            throw new RuntimeException("You are not the owner of this help request");

        helpRequest.setStatus(RequestStatus.COMPLETED);
        helpRequestRepository.save(helpRequest);
    }
}
