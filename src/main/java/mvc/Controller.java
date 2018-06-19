package mvc;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import packing.*;
import packing.Box;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controller implements PropertyChangeListener {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        this.model.addListener(this);

        setUpViewEvents();
    }

    private void setUpViewEvents() {
        view.getContainerComboBox().setModel(Model.getAvailablePackingContainers());
        view.getTimeOutComboBox().setModel(Model.getTimeOuts());
        view.getBoxesTable().setModel(model.getBoxes());

        view.getAddBoxBtn().addActionListener(new AddBoxActionListener());
        view.getPackBtn().addActionListener(new PackageActionListener());
        view.getRemoveBoxBtn().addActionListener(new RemoveBoxActionListener());
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
        switch(type) {
            case JOptionPane.WARNING_MESSAGE:
                modalTitle = "Waarschuwing";
                break;
            case JOptionPane.ERROR_MESSAGE:
                modalTitle = "Error";
                break;
            default:
                modalTitle = "Informatie";
        }

        JOptionPane.showMessageDialog(view.getFrame(),
                message,
                modalTitle,
                type);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    private class PackageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getBoxes().getRowCount() < 1) {
                showInfoMessage("Je moet minimaal 1 doos toevoegen"); return;
            }

            Packager packager = new Packager((Dimension) view.getContainerComboBox().getSelectedItem());
            Long chosenTimeout = (Long) view.getTimeOutComboBox().getSelectedItem();

            final Enumeration productsVector = model.getBoxes().getDataVector().elements();
            final ArrayList<Box> products = new ArrayList<>();

            while(productsVector.hasMoreElements()) {
                Vector productVector = (Vector) productsVector.nextElement();
                products.add(new Box((String) productVector.get(0), (int) productVector.get(2),  (int) productVector.get(3),  (int) productVector.get(1)));
            }

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Json files", "json");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(filter);

            if (fileChooser.showSaveDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                CompletableFuture<Container> findMatch = CompletableFuture.supplyAsync(() -> packager.pack(products))
                        .whenComplete((container, throwable) -> {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(Placement.class, new PlacementSerializer());
                            Gson gson = gsonBuilder.create();
                            Type type = new TypeToken<Container>() {}.getType();

                            try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
                                writer.write(gson.toJson(container, type));
                            }  catch (IOException ioException) {
                                throw new CompletionException(ioException);
                            }
                        });

                try {
                    Container future = findMatch.get(chosenTimeout, TimeUnit.SECONDS);
                    int totalAmountOfPackedBoxes = 0;
                    for(Level level : future.getLevels()) {
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
            for (int selectedRow : view.getBoxesTable().getSelectedRows()) {
                model.getBoxes().removeRow(selectedRow);
            }

            view.getBoxesTable().clearSelection();
        }
    }

    private class AddBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("addBox")) {
                model.getBoxes().addRow(new Object[] { "Nieuwe box", ((int) Math.floor(Math.random() * 100) + 1),  ((int) Math.floor(Math.random() * 100) + 1),  ((int) Math.floor(Math.random() * 100) + 1) });
            }
        }
    }

    private class PlacementSerializer implements JsonSerializer<Placement> {

        @Override
        public JsonElement serialize(Placement placement, Type type, JsonSerializationContext context) {
            JsonObject root = new JsonObject();
            root.addProperty("box", placement.getBox().getName());
            root.addProperty("x", placement.getSpace().getX());
            root.addProperty("y", placement.getSpace().getY());
            root.addProperty("z", placement.getSpace().getZ());
            root.addProperty("w", placement.getBox().getWidth());
            root.addProperty("h", placement.getBox().getHeight());
            root.addProperty("d", placement.getBox().getDepth());

            return root;
        }

    }
}
