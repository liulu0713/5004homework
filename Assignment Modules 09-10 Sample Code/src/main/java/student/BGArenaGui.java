package student;


import javax.swing.*;

/**
 * Main entry point for the program.
 */
public final class BGArenaGui {
    /** default location of collection - relative to the resources directory. */
    private static final String DEFAULT_COLLECTION = "/collection.csv";

    /** private constructor as static class. */
    private BGArenaGui() {

    }

    /**
     * Main entry point for the program.
     * 
     * @param args command line arguments - not used at this time.
     */
    public static void main(String[] args) {
        launchGui();
    }


    /**
     * launches GUI
     */
    private static void launchGui() {
        // Set system look-and-feel for native decorations
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fall back to default Swing L&F — not critical
        }

        // Build model objects once (shared between controller and view)
        IPlanner  planner  = new Planner(GamesLoader.loadGamesFile(DEFAULT_COLLECTION));
        IGameList gameList = new GameList();

        // All Swing work must be done on the EDT
        SwingUtilities.invokeLater(() -> {
            GUIView view = new GUIView();
            // Controller wires itself to the view and performs initial filter
            new GUIController(planner, gameList, view);
            view.setVisible(true);
        });
    }


}
