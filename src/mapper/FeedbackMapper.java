package mapper;

import java.time.LocalDateTime;
import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.Feedback;

public class FeedbackMapper implements Mapper<Feedback>{
    @Override
    public Feedback toObject(String row) throws FileCorruptedException {
        String[] data = row.split("\\|");
        
        if (data.length != 7) {
            throw new FileCorruptedException("Feedback file contains extra data or has missing data");
        }

        Feedback fb = new Feedback();
        fb.setId(data[0]);
        fb.setCreatedAt(LocalDateTime.parse(data[5]));
        fb.setUpdatedAt(LocalDateTime.parse(data[6]));
        fb.setAppointmentId(data[1]);
        fb.setStaffRating(Integer.parseInt(data[2]));
        fb.setTechnicianRating(Integer.parseInt(data[3]));
        fb.setComment(data[4]);

        return fb;
    }

    @Override
    public String toString(Feedback fb) {
        String safeComment = (fb.getComment() == null) ? "" : fb.getComment();
        return String.join("|", 
                fb.getId(), fb.getAppointmentId(), String.valueOf(fb.getStaffRating()), String.valueOf(fb.getTechnicianRating()),
                safeComment,fb.getCreatedAt().toString(), fb.getUpdatedAt().toString() 
        );
    }
}
