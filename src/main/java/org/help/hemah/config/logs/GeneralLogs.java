package org.help.hemah.config.logs;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.service.volunteer.VolunteerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class GeneralLogs {

    private final VolunteerService volunteerService;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void activeVols() {
        log.info("Active Volunteers: {}", volunteerService.getActiveVolunteers().size());
    }
}
