package student;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


/**
 * Clean, simple GUI View for MVC.
 * No styling tricks, no extra complexity.
 */
public class GUIView extends JFrame {

    // Controller
    private GUIController controller;

    // Filter panel
    private JTextField filterField;
    private JComboBox<String> sortCombo;
    private JToggleButton ascBtn;
    private JToggleButton descBtn;
    private JButton applyBtn;
    private JButton resetBtn;
    private JComboBox<String> operatorCombo;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;

    // My List
    private JList<String> myList;
    private DefaultListModel<String> listModel;
    private JTextField addField;
    private JTextField removeField;
    private JButton addBtn;
    private JButton removeBtn;
    private JButton clearBtn;

    // Status
    private JLabel statusLabel;

    // ---------------------------
    // Constructor
    // ---------------------------
    public GUIView() {
        super("BGArena Planner");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        buildUI();
    }

    // ---------------------------
    // Build UI
    // ---------------------------
    private void buildUI() {
        setLayout(new BorderLayout());

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
    }
    public String getFilterField() {
        return sortCombo.getSelectedItem().toString();
    }

    public String getOperator() {
        return operatorCombo.getSelectedItem().toString();
    }

    public String getFilterValue() {
        return filterField.getText().trim();
    }
    // ---------------------------
    // Top (Filter)
    // ---------------------------
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        filterField = new JTextField(20);

        sortCombo = new JComboBox<>(new String[]{
                "Name", "Rating", "Difficulty", "Rank",
                "Min Players", "Max Players",
                "Min Time", "Max Time", "Year"
        });
        operatorCombo = new JComboBox<>(new String[]{
                ">", "<", ">=", "<=", "==", "!=", "~="
        });

        ascBtn = new JToggleButton("Asc", true);
        descBtn = new JToggleButton("Desc");

        ButtonGroup group = new ButtonGroup();
        group.add(ascBtn);
        group.add(descBtn);

        applyBtn = new JButton("Apply");
        resetBtn = new JButton("Reset");

        panel.add(new JLabel("Filter:"));
        panel.add(sortCombo);
        panel.add(operatorCombo);
        panel.add(filterField);
        panel.add(ascBtn);
        panel.add(descBtn);
        panel.add(applyBtn);
        panel.add(resetBtn);

        return panel;
    }

    // ---------------------------
    // Center (Table + My List)
    // ---------------------------
    private JPanel buildCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));

        // Table
        String[] cols = {"#", "Name", "Rating", "Difficulty", "Rank",
                "MinP", "MaxP", "MinT", "MaxT", "Year"};

        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);

        JScrollPane tableScroll = new JScrollPane(table);

        // My List
        listModel = new DefaultListModel<>();
        myList = new JList<>(listModel);

        JScrollPane listScroll = new JScrollPane(myList);

        panel.add(tableScroll);
        panel.add(listScroll);

        return panel;
    }

    // ---------------------------
    // Bottom (Controls + Status)
    // ---------------------------
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout());

        addField = new JTextField(10);
        removeField = new JTextField(10);

        addBtn = new JButton("Add");
        removeBtn = new JButton("Remove");
        clearBtn = new JButton("Clear");

        controls.add(new JLabel("Add:"));
        controls.add(addField);
        controls.add(addBtn);

        controls.add(new JLabel("Remove:"));
        controls.add(removeField);
        controls.add(removeBtn);

        controls.add(clearBtn);

        statusLabel = new JLabel("Ready");

        panel.add(controls, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------------------
    // Controller hookup
    // ---------------------------
    public void setController(GUIController c) {
        this.controller = c;
        wireEvents();
    }

    private void wireEvents() {
        applyBtn.addActionListener(e -> controller.handleApplyFilter());
        resetBtn.addActionListener(e -> controller.handleReset());

        filterField.addActionListener(e -> controller.handleApplyFilter());

        addBtn.addActionListener(e -> controller.handleAddToList());
        removeBtn.addActionListener(e -> controller.handleRemoveFromList());
        clearBtn.addActionListener(e -> controller.handleClearList());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    controller.handleDoubleClickAdd();
                }
            }
        });
    }

    // ---------------------------
    // Methods used by controller
    // ---------------------------

    public String getFilterText() {
        return filterField.getText().trim();
    }

    public GameData getSelectedSortColumn() {
        GameData[] mapping = {
                GameData.NAME, GameData.RATING, GameData.DIFFICULTY, GameData.RANK,
                GameData.MIN_PLAYERS, GameData.MAX_PLAYERS,
                GameData.MIN_TIME, GameData.MAX_TIME, GameData.YEAR
        };
        return mapping[sortCombo.getSelectedIndex()];
    }

    public boolean isAscending() {
        return ascBtn.isSelected();
    }

    public String getAddFieldText() {
        return addField.getText().trim().toLowerCase();
    }

    public String getRemoveFieldText() {
        return removeField.getText().trim().toLowerCase();
    }

    public int[] getSelectedTableRows() {
        return table.getSelectedRows();
    }

    public void clearAddField() {
        addField.setText("");
    }

    public void clearRemoveField() {
        removeField.setText("");
    }

    public void updateResultsTable(List<BoardGame> games, GameData sortCol) {
        tableModel.setRowCount(0);
        int i = 1;
        for (BoardGame g : games) {
            tableModel.addRow(new Object[]{
                    i++,
                    g.getName(),
                    g.getRating(),
                    g.getDifficulty(),
                    g.getRank(),
                    g.getMinPlayers(),
                    g.getMaxPlayers(),
                    g.getMinPlayTime(),
                    g.getMaxPlayTime(),
                    g.getYearPublished()
            });
        }
    }

    public void updateMyList(List<String> names) {
        listModel.clear();
        for (String n : names) listModel.addElement(n);
    }

    public void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

}