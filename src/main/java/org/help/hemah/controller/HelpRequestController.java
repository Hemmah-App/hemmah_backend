package org.help.hemah.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.help.hemah.helper.req_model.NewHelpRequestModel;
import org.help.hemah.helper.res_model.ResponseModel;
import org.help.hemah.service.help_request.HelpRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResponseModel> createHelpRequest(@RequestBody @Valid NewHelpRequestModel model) {

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Help Request Created Successfully")
                        .data(Map.of("requestId", helpRequestService.addNewHelpRequest(model)))
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
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Help Requests Retrieved Successfully")
                        .data(Map.of("requests", helpRequestService.getHelpRequestFeed()))
                        .build()
        );
    }


}
