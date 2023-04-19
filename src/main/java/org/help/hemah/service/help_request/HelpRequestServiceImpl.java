package org.help.hemah.service.help_request;

import lombok.RequiredArgsConstructor;
import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.help_request.HelpRequest;
import org.help.hemah.domain.help_request.RequestStatus;
import org.help.hemah.helper.req_model.HelpRequestModel;
import org.help.hemah.repository.help_requset.HelpRequestRepository;
import org.help.hemah.service.auth.AuthenticationFacade;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpRequestServiceImpl implements HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final AuthenticationFacade authenticationFacade;

    private boolean requestBelongsToDisabled(HelpRequest helpRequest) {
        Disabled disabled = authenticationFacade.getAuthenticatedDisabled();
        return helpRequest.getDisabled().getId().equals(disabled.getId());
    }

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

    @PreAuthorize("hasRole('DISABLED')")
    @Override
    public Long addNewHelpRequest(HelpRequestModel helpRequestModel) {
        Disabled disabled = authenticationFacade.getAuthenticatedDisabled();

        HelpRequest helpRequest = new HelpRequest(
                helpRequestModel.getTitle(),
                helpRequestModel.getDescription(),
                helpRequestModel.getDate(),
                disabled,
                RequestStatus.NEEDS_HELP,
                helpRequestModel.getLocation()
        );

        helpRequest.setLongitude(helpRequestModel.getLongitude());
        helpRequest.setLatitude(helpRequestModel.getLatitude());

        return helpRequestRepository.save(helpRequest).getId();
    }

    @PreAuthorize("hasRole('DISABLED')")
    @Override
    public void editHelpRequest(Long helpRequestId, HelpRequestModel helpRequestModel) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId).orElseThrow();

        if (!requestBelongsToDisabled(helpRequest))
            throw new RuntimeException("You are not the owner of this help request");


        helpRequest.setTitle(helpRequestModel.getTitle());
        helpRequest.setDescription(helpRequestModel.getDescription());
        helpRequest.setDate(helpRequestModel.getDate());
        helpRequest.setMeetingLocation(helpRequestModel.getLocation());
        helpRequest.setLongitude(helpRequestModel.getLongitude());
        helpRequest.setLatitude(helpRequestModel.getLatitude());

        helpRequestRepository.save(helpRequest);
    }

    @PreAuthorize("hasRole('DISABLED')")
    @Override
    public void removeHelpRequest(Long helpRequestId) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId).orElseThrow();

        if (!requestBelongsToDisabled(helpRequest))
            throw new RuntimeException("You are not the owner of this help request");

        helpRequest.setStatus(RequestStatus.CANCELED);
        helpRequestRepository.save(helpRequest);
    }

    @PreAuthorize("hasRole('DISABLED')")
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
