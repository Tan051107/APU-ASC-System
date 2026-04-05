package mapper;

import java.time.LocalDateTime;
import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.Feedback;

public class FeedbackMapper implements Mapper<Feedback>{
    @Override
    public Feedback toObject(String row) throws FileCorruptedException {
        String[] data = row.split("\\|");
        
        if (data.length != 8) {
            throw new FileCorruptedException("Feedback file contains extra data or has missing data");
        }

        Feedback fb = new Feedback();
        fb.setId(data[0]);
        fb.setCreatedAt(LocalDateTime.parse(data[6]));
        fb.setUpdatedAt(LocalDateTime.parse(data[7]));
        fb.setAppointmentId(data[1]);
        fb.setTechnicianFeedback(data[2]);
        String staffRating = data[3].trim();
        if (!staffRating.isEmpty() && !"null".equalsIgnoreCase(staffRating)) {
            fb.setStaffRating(Integer.parseInt(staffRating));
        } else {
            fb.setStaffRating(null);
        }
        String technicianRating = data[4].trim();
        if (!technicianRating.isEmpty() && !"null".equalsIgnoreCase(technicianRating)) {
            fb.setTechnicianRating(Integer.parseInt(technicianRating));
        } else {
            fb.setTechnicianRating(null);
        }
        String comment = data[5].trim();
        fb.setComment(comment.isEmpty() || "null".equalsIgnoreCase(comment) ? null : comment);

        return fb;
    }

    @Override
    public String toString(Feedback fb) {
        String safeStaffRating = (fb.getStaffRating() == null) ? "" : String.valueOf(fb.getStaffRating());
        String safeTechRating = (fb.getTechnicianRating() == null) ? "" : String.valueOf(fb.getTechnicianRating());
        String safeComment = (fb.getComment() == null) ? "" : fb.getComment();
        
        return String.join("|", 
                fb.getId(), fb.getAppointmentId(), fb.getTechnicianFeedback(), safeStaffRating, safeTechRating,
                safeComment,fb.getCreatedAt().toString(), fb.getUpdatedAt().toString() 
        );
    }
}
