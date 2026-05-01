package ui.controller.Manager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Appointment;
import models.Feedback;
import models.User;
import services.AppointmentService;
import services.FeedbackService;
import services.UserService;
import ui.pages.Manager.RatingReports;
import utils.DialogUtil;



public class RatingReportController {
    private final RatingReports panel;

    private final FeedbackService feedbackService = new FeedbackService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final UserService userService = new UserService();

    public RatingReportController(RatingReports panel){
        this.panel = panel;
        initListeners();
        updatePanelInfo();

    }

    private void initListeners() {
        panel.roleComboBox.addActionListener(e -> updatePanelInfo());
        panel.closeButton.addActionListener(e -> panel.dispose());
    }

    public void updatePanelInfo(){
        try {
            int role = panel.roleComboBox.getSelectedIndex() + 1; 
        
            List<Feedback> allFeedbackRecords = feedbackService.getFeedbacks();
            Map<String, List<Integer>> userRatingsMap = new HashMap<>();

            for (Feedback feedback : allFeedbackRecords) {
                Appointment appointment = appointmentService.getAppointmentById(feedback.getAppointmentId());
                
                if (appointment == null) continue; // Skip if appointment data is missing

                String userId = null;
                Integer rating = null;

                if (role == 1) {
                    userId = appointment.getTechnicianId();
                    rating = feedback.getTechnicianRating();
                } else if (role == 2) {
                    userId = appointment.getStaffId();
                    rating = feedback.getStaffRating();
                }

                if (userId != null && rating != null) {
                    userRatingsMap.putIfAbsent(userId, new ArrayList<>());
                    userRatingsMap.get(userId).add(rating);
                }
            }

            String bestUserId = null;
            String worstUserId = null;
            double highestAvg = -1.0; 
            double lowestAvg = Double.MAX_VALUE;
            
            for (Map.Entry<String, List<Integer>> entry : userRatingsMap.entrySet()) {
                String currentUserId = entry.getKey();
                List<Integer> ratings = entry.getValue();
                
                double averageRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);

                if (averageRating > highestAvg) {
                    highestAvg = averageRating;
                    bestUserId = currentUserId;
                }
                if (averageRating < lowestAvg) {
                    lowestAvg = averageRating;
                    worstUserId = currentUserId;
                }
            }

            String bestUserName = "N/A";
            String worstUserName = "N/A";

            if (bestUserId != null) {
                User bestUser = userService.getUserById(bestUserId);
                if (bestUser != null) bestUserName = bestUser.getName();
            }

            if (worstUserId != null) {
                User worstUser = userService.getUserById(worstUserId);
                if (worstUser != null) worstUserName = worstUser.getName();
            }

            panel.bestRatingName.setText(bestUserName);
            panel.worstRatingName.setText(worstUserName);

            if (highestAvg >= 0) {
                panel.bestRating.setText(String.format("%.2f / 5.00", highestAvg));
            } else {
                panel.bestRating.setText("-");
            }

            if (lowestAvg != Double.MAX_VALUE) {
                panel.worstRating.setText(String.format("%.2f / 5.00", lowestAvg));
            } else {
                panel.worstRating.setText("-");
            }

        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Calculating Ratings: " + e.getMessage());
        }
    }

}
