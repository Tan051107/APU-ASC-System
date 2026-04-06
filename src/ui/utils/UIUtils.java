package ui.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import java.awt.*;
import java.util.Vector;

public class UIUtils {
    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(Color.BLACK);

        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(Color.BLACK);

        return field;
    }

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        Color base = new Color(37, 99, 235);
        Color hover = new Color(29, 78, 216);

        btn.setBackground(base);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(base);
            }
        });

        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        Color base = new Color(245, 245, 245);
        Color hover = new Color(230, 230, 230);

        btn.setFocusPainted(false);
        btn.setBackground(base);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(base);
            }
        });

        return btn;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(50, 50, 50));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static <T> JComboBox<T> createJComboBox(T[] options) {
        return styleJComboBox(new JComboBox<>(options));
    }

    public static <T> JComboBox<T> createJComboBox() {
        return styleJComboBox(new JComboBox<>());
    }

    public static <T> JComboBox<T> createJComboBox(ComboBoxModel<T> model) {
        return styleJComboBox(new JComboBox<>(model));
    }

    public static <T> JComboBox<T> createJComboBox(Vector<T> options) {
        return styleJComboBox(new JComboBox<>(options));
    }

    private static <T> JComboBox<T> styleJComboBox(JComboBox<T> comboBox) {
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setOpaque(true);

        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        return comboBox;
    }

    public static JLabel createMenuTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }

    public static JButton createCRUDButton(String text) {
        JButton button = new JButton(text);
        Dimension buttonSize = new Dimension(150, 50);
        button.setPreferredSize(buttonSize);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(99, 110, 114));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        return button;
    }

    public static JButton createIconButton(String icon, Color color) {
        JButton btn = new JButton(icon);
        btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        btn.setForeground(new Color(150, 150, 150));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(icon.equals("✎") ? "Edit" : "Delete");
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(color);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(150, 150, 150));
            }
        });
        return btn;
    }

    public static JButton createLinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(37, 99, 235));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JFormattedTextField createDateField() {
        try {
            javax.swing.text.MaskFormatter dateMask = new javax.swing.text.MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            JFormattedTextField field = new JFormattedTextField(dateMask);
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            return field;
        } catch (java.text.ParseException e) {
            return new JFormattedTextField();
        }
    }

    public static JFormattedTextField createTimeField() {
        try {
            javax.swing.text.MaskFormatter timeMask = new javax.swing.text.MaskFormatter("##:##");
            timeMask.setPlaceholderCharacter('_');
            JFormattedTextField field = new JFormattedTextField(timeMask);
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            return field;
        } catch (java.text.ParseException e) {
            return new JFormattedTextField();
        }
    }

    public static JPanel createBadge(String text) {
        RoundedPanel badge = new RoundedPanel(10);
        badge.setBackground(new Color(243, 244, 246));
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 2));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(new Color(107, 114, 128));
        badge.add(label);
        return badge;
    }

    public static JTable createTable(TableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFocusable(false);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(212, 230, 241));
        table.setSelectionForeground(Color.BLACK);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(37, 99, 235));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        DefaultTableCellRenderer modernRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(244, 248, 251));
                }
                return this;
            }
        };

        table.setDefaultRenderer(Object.class, modernRenderer);

        return table;
    }
}
