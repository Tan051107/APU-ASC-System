package ui.controller.Manager;


import ui.pages.Manager.RatingReports;



public class RatingReportController {
    private final RatingReports panel;

    public RatingReportController(RatingReports panel){
        this.panel = panel;
        initListeners();

    }

    private void initListeners() {
        panel.roleComboBox.addActionListener(e -> selectedRole());
        panel.closeButton.addActionListener(e -> panel.dispose());
    }

    public void selectedRole(){
        int selectedRole = panel.roleComboBox.getSelectedIndex() + 1;

    }

    public void getRatingInfo(){

    }
}
