package org.help.hemah.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.help.hemah.domain.help_request.HelpRequest;
import org.help.hemah.helper.req_model.HelpRequestModel;
import org.help.hemah.helper.res_model.ResponseModel;
import org.help.hemah.service.help_request.HelpRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/help-request")
@RequiredArgsConstructor
public class HelpRequestController {

    private final HelpRequestService helpRequestService;

    // Disabled Methods
    @GetMapping
    @PreAuthorize("hasRole('DISABLED')")
    public ResponseEntity<ResponseModel> getHelpRequestForDisabled() {

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Your Help Requests Retrieved Successfully")
                        .data(Map.of("myRequests", helpRequestService.getDisabledHelpRequests()))
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('DISABLED')")
    public ResponseEntity<ResponseModel> createHelpRequest(@RequestBody @Valid HelpRequestModel model) {

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Help Request Created Successfully")
                        .data(Map.of("requestId", helpRequestService.addNewHelpRequest(model)))
                        .build()
        );
    }

    @PatchMapping
    @PreAuthorize("hasRole('DISABLED')")
    public ResponseEntity<ResponseModel> editHelpRequest(
            @RequestParam(value = "requestId") Long requestId,
            @RequestBody @Valid HelpRequestModel model) {

        helpRequestService.editHelpRequest(requestId, model);

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Help Request Updated Successfully")
                        .build()
        );
    }

    @DeleteMapping
    @PreAuthorize("hasRole('DISABLED')")
    public ResponseEntity<ResponseModel> removeHelpRequest(
            @RequestParam(value = "requestId") Long requestId) {

        // Doesn't actually delete, just change the status to CANCELLED for more monitoring
        helpRequestService.removeHelpRequest(requestId);

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Help Request Removed Successfully")
                        .build()
        );
    }

    @PatchMapping("/complete")
    @PreAuthorize("hasRole('DISABLED')")
    public ResponseEntity<ResponseModel> updateHelpRequestStatusToComplete(
            @RequestParam(value = "requestId") Long requestId) {

        helpRequestService.markHelpRequestAsComplete(requestId);

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Help Request Status Updated Successfully")
                        .build()
        );
    }

    // Volunteer Methods
    @GetMapping("/feed")
    public ResponseEntity<ResponseModel> getHelpRequestFeed() {

        List<HelpRequest> requests = helpRequestService.getHelpRequestFeed();

        List<Map<Object, Object>> requestsRes = new ArrayList<>();

        requests.forEach((req) -> {
            requestsRes.add(Map.of(
                    "id", req.getId(),
                    "title", req.getTitle(),
                    "description", req.getDescription(),
                    "status", req.getStatus(),
                    "createdAt", req.getCreatedAt(),
                    "helpDate", req.getDate(),
                    "helpLocation", req.getMeetingLocation(),
                    "disabled",
                    Map.of(
                            "name", req.getDisabled().getUserData().getFullName(),
                            "phone", req.getDisabled().getUserData().getPhoneNumber(),
                            "address", req.getDisabled().getUserData().getAddress())));
        });


        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Help Requests Retrieved Successfully")
                        .data(Map.of(
                                "requests", requestsRes
                        ))
                        .build()
        );
    }


}
