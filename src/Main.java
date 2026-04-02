import ui.pages.Login;
import ui.pages.managermenu;

import javax.swing.*;

void main() {
    /* SwingUtilities.invokeLater(()->new Login().createUI()); */
    SwingUtilities.invokeLater(() -> {
            new managermenu().setVisible(true);
    });
}

