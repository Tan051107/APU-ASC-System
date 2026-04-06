package services;

import java.util.List;

import exceptions.DeleteException;
import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import mapper.FeedbackMapper;
import models.Feedback;
import repositories.CrudRepository;
import utils.RandomIdGenerator;

public class FeedbackService {
    private final String FEEDBACK_FILE = "txt_files/Feedback.txt";
    private final FeedbackMapper feedbackMapper = new FeedbackMapper();
    private final CrudRepository<Feedback> feedbackRepository = new CrudRepository<>(FEEDBACK_FILE , feedbackMapper);
    /* private final Logger logger = Logger.getLogger(UserService.class.getName()); */

    public List<Feedback> getFeedbacks() throws GetEntityListException {
        try{
            return feedbackRepository.getAll();
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public Feedback getFeedbackById(String id) throws GetEntityListException {
        try{
            return feedbackRepository.getOne(id);
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public Feedback getFeedbackByAppointmentId(String appointmentId) throws GetEntityListException {
        try{
            List<Feedback> feedbacks = feedbackRepository.getAll(fb -> fb.getAppointmentId().equalsIgnoreCase(appointmentId));
            if(feedbacks.isEmpty()){
                return null;
            }
            return feedbacks.get(0); 
        }
        catch (FileCorruptedException e){
            throw new GetEntityListException(e.getMessage());
        }
    }

    public void createFeedback(Feedback feedbackToAdd) throws Exception {
        try {
            boolean feedbackHasExisted = getFeedbackByAppointmentId(feedbackToAdd.getAppointmentId()) != null;
            if(feedbackHasExisted){
                throw new Exception("Feedback for this appointment already exists.");
            }
            
            String feedbackId = generateId();
            feedbackToAdd.setId(feedbackId);
            feedbackRepository.create(feedbackToAdd);
            
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void updateFeedback(Feedback feedbackToUpdate) throws FileCorruptedException, NotFoundException, UpdateException {
        feedbackRepository.update(feedbackToUpdate);
    }

    public void deleteFeedback(String feedbackId) throws DeleteException {
        feedbackRepository.delete(feedbackId);
    }

    private String generateId(){
        while(true){
            String feedbackId = RandomIdGenerator.generateId("FB-" , 5); 
            try {
                if(getFeedbackById(feedbackId) == null){
                    return feedbackId;
                }
            } catch (GetEntityListException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
