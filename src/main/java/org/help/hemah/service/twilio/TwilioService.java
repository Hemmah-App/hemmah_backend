package org.help.hemah.service.twilio;

import com.twilio.Twilio;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;
import com.twilio.rest.video.v1.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class TwilioService {

    private String ACCOUNT_SID;
    private String API_SID;
    private String API_SECRET;

    @Autowired
    public TwilioService() {

    }

    @Autowired
    private void init(@Value("${twilio.ACCOUNT_SID}") String ACCOUNT_SID,
                      @Value("${twilio.API_SID}") String API_SID,
                      @Value("${twilio.API_SECRET}") String API_SECRET) {

        log.info("ACCOUNT_SID: {}, API_SID: {}, API_SECRET: {}", ACCOUNT_SID, API_SID, API_SECRET);
        Twilio.init(API_SID, API_SECRET);

        this.ACCOUNT_SID = ACCOUNT_SID;
        this.API_SID = API_SID;
        this.API_SECRET = API_SECRET;
    }

    public Room createVideoRoom(String roomName, Room.RoomType roomType) {
        return Room.creator()
                .setUniqueName(roomName)
                .setType(roomType)
                .create();
    }

    public void updateVideoRoomStatus(String roomName, Room.RoomStatus roomStatus) {
        for (Room room : Room.reader().setUniqueName(roomName).read()) {
            Room.updater(room.getSid(), roomStatus).update();
        }
    }

    public String generateVideoToken(String identity, String roomName) {
        AccessToken accessToken = new AccessToken.Builder(ACCOUNT_SID, API_SID, API_SECRET)
                .identity(identity)
                .grant(new VideoGrant().setRoom(roomName))
                .build();

        return accessToken.toJwt();
    }


}
