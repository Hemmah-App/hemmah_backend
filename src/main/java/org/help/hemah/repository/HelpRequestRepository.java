package org.help.hemah.repository;

import org.help.hemah.model.disabled.Disabled;
import org.help.hemah.model.help_request.HelpRequest;
import org.help.hemah.model.help_request.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    List<HelpRequest> findAllByDisabled(Disabled disabled);

    List<HelpRequest> findAllByStatus(RequestStatus status);
}
