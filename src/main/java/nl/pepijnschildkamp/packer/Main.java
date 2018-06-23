package nl.pepijnschildkamp.packer;

import nl.pepijnschildkamp.packer.mvc.Controller;
import nl.pepijnschildkamp.packer.mvc.Model;
import nl.pepijnschildkamp.packer.mvc.View;

import javax.swing.*;

public class Main implements Runnable {
    @Override
    public void run() {
        View view = new View();
        Model model = new Model();
        new Controller(view, model);
    }

    public static void main(String[] args) {
        Main se = new Main();
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(se);
    }
}
