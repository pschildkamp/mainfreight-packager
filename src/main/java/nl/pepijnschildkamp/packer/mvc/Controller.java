package nl.pepijnschildkamp.packer.mvc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import nl.pepijnschildkamp.packer.packing.Dimension;
import nl.pepijnschildkamp.packer.packing.Item;
import nl.pepijnschildkamp.packer.packing.Level;
import nl.pepijnschildkamp.packer.packing.Packager;
import nl.pepijnschildkamp.packer.packing.Placement;
import nl.pepijnschildkamp.packer.packing.Wave;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        setUpViewEvents();
    }

    private void setUpViewEvents() {
        view.getWaveComboBox()
                .setModel(Model.getAvailablePackingWaves());
        view.getTimeOutComboBox()
                .setModel(Model.getTimeOuts());
        view.getBoxesTable()
                .setModel(model.getBoxes());

        view.getAddRowButton()
                .addActionListener(new AddBoxActionListener());
        view.getPackButton()
                .addActionListener(new PackageActionListener());
        view.getRemoveRowButton()
                .addActionListener(new RemoveBoxActionListener());
    }

    public void showInfoMessage(String message) {
        showModal(message, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        showModal(message, JOptionPane.ERROR_MESSAGE);
    }

    public void showWarningMessage(String message) {
        showModal(message, JOptionPane.WARNING_MESSAGE);
    }

    public void showModal(final String message, final int type) {
        String modalTitle;
        switch (type) {
            case JOptionPane.WARNING_MESSAGE:
                modalTitle = "Waarschuwing";
                break;
            case JOptionPane.ERROR_MESSAGE:
                modalTitle = "Error";
                break;
            default:
                modalTitle = "Informatie";
        }

        JOptionPane.showMessageDialog(view.getFrame(), message, modalTitle, type);
    }

    private class PackageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getBoxes()
                    .getRowCount() < 1) {
                showInfoMessage("Je moet minimaal 1 doos toevoegen");
                return;
            }

            Packager packager = new Packager((Dimension) view.getWaveComboBox()
                    .getSelectedItem());
            Long chosenTimeout = Long.valueOf((Integer) Objects.requireNonNull(view.getTimeOutComboBox()
                    .getSelectedItem()));

            final Enumeration productsVector = model.getBoxes()
                    .getDataVector()
                    .elements();
            final ArrayList<Item> products = new ArrayList<>();

            while (productsVector.hasMoreElements()) {
                Vector productVector = (Vector) productsVector.nextElement();
                products.add(new Item((String) productVector.get(0), (int) productVector.get(2), (int) productVector.get(3), (int) productVector.get(1)));
            }

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Json files", "json");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(filter);

            if (fileChooser.showSaveDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                CompletableFuture<Wave> findMatch = CompletableFuture.supplyAsync(() -> packager.pack(products))
                        .whenComplete((wave, throwable) -> {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(Placement.class, new PlacementSerializer());
                            Gson gson = gsonBuilder.create();
                            Type type = new TypeToken<Wave>() {
                            }.getType();

                            try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
                                writer.write(gson.toJson(wave, type));
                            } catch (IOException ioException) {
                                throw new CompletionException(ioException);
                            }
                        });

                try {
                    Wave future = findMatch.get(chosenTimeout, TimeUnit.SECONDS);
                    int totalAmountOfPackedBoxes = 0;
                    for (Level level: future.getLevels()) {
                        totalAmountOfPackedBoxes += level.getTotalAmountOfBoxes();
                    }
                    showInfoMessage("Succesvol " + totalAmountOfPackedBoxes + " dozen ingepakt.");
                } catch (InterruptedException | ExecutionException futureException) {
                    showErrorMessage("Posities bepalen van dozen is mislukt.");
                } catch (TimeoutException timeoutException) {
                    showWarningMessage("Helaas heeft het systeem niet binnen de gestelde deadline de dozen kunnen inpakken.");
                }
            }
        }
    }

    private class RemoveBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int selectedRow: view.getBoxesTable()
                    .getSelectedRows()) {
                model.getBoxes()
                        .removeRow(selectedRow);
            }

            view.getBoxesTable()
                    .clearSelection();
        }
    }

    private class AddBoxActionListener implements ActionListener {
        private final Integer MAX_BOXES = 50;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isMaximumBoxesReached()) {
                if (e.getActionCommand()
                        .equals("addBox")) {
                    model.getBoxes()
                            .addRow(new Object[]{"Nieuwe item", ((int) Math.floor(Math.random() * 100) + 1), ((int) Math.floor(Math.random() * 100) + 1),
                                    ((int) Math.floor(Math.random() * 100) + 1)});
                }
            } else {
                showWarningMessage("Maximaal aantal dozen (" + MAX_BOXES + ") bereikt. Applicatie ondersteunt niet meer dozen.");
            }
        }

        private Boolean isMaximumBoxesReached() {
            return model.getBoxes()
                    .getRowCount() >= MAX_BOXES;
        }
    }

    private class PlacementSerializer implements JsonSerializer<Placement> {
        @Override
        public JsonElement serialize(Placement placement, Type type, JsonSerializationContext context) {
            JsonObject root = new JsonObject();
            root.addProperty("item", placement.getItem()
                    .getName());
            root.addProperty("x", placement.getSpace()
                    .getX());
            root.addProperty("y", placement.getSpace()
                    .getY());
            root.addProperty("z", placement.getSpace()
                    .getZ());
            root.addProperty("w", placement.getItem()
                    .getWidth());
            root.addProperty("h", placement.getItem()
                    .getHeight());
            root.addProperty("d", placement.getItem()
                    .getDepth());

            return root;
        }
    }
}
