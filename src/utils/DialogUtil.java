package utils;

import javax.swing.*;

public class DialogUtil {

    public static void showInfoMessage(String title , String message){
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showErrorMessage(String title , String message){
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showWarningMessage(String title , String message){
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static boolean showConfirmationMessage(String title,String message){
        int result = JOptionPane.showConfirmDialog(
                null,
                message,
                title,
                JOptionPane.YES_NO_OPTION
        );

        return result == JOptionPane.YES_OPTION;
    }
}
