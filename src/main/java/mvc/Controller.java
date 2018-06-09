package mvc;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import packing.*;
import packing.Box;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.Pack200;


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
        view.getAddBoxBtn().addActionListener(actionEvent -> {
            if (actionEvent.getActionCommand().equals("addBox")) {
                model.getBoxes().addRow(new Object[] { "Nieuwe box", 5, 5, 5 });
                /** When no boxes added */

            }
        });

        view.getPackBtn().addActionListener(e -> {
            try {
                if (model.getBoxes().getRowCount() < 1)
                    throw new RuntimeException("Je moet minimaal 1 doos toevoegen");

                BruteForcePackager packager = new BruteForcePackager(Collections.singletonList((Dimension) view.getContainerComboBox().getSelectedItem()), false, false);
                long deadline = System.currentTimeMillis() + 10000;

                List<BoxItem> products = new ArrayList<>();
                products.add(new BoxItem(new Box("72407",20,30,15), 1));
                products.add(new BoxItem(new Box("74809",100,120,52), 1));
                products.add(new BoxItem(new Box("71535",30,40,15), 1));
                products.add(new BoxItem(new Box("74780",20,30,15), 1));
                products.add(new BoxItem(new Box("74760_1",30,20,15), 1));
                products.add(new BoxItem(new Box("74760_2",30,20,15), 1));
                products.add(new BoxItem(new Box("74757",30,40,15), 1));
                products.add(new BoxItem(new Box("74808",100,120,75), 1));
                products.add(new BoxItem(new Box("73770",20,30,15), 1));
                products.add(new BoxItem(new Box("74844_1",60,40,28), 1));
                products.add(new BoxItem(new Box("74844_2",60,40,28), 1));
                products.add(new BoxItem(new Box("74844_3",60,40,28), 1));
                products.add(new BoxItem(new Box("74846",30,40,15), 1));
                products.add(new BoxItem(new Box("73767_1",60,50,15), 1));
                products.add(new BoxItem(new Box("73767_2",60,50,15), 1));
                products.add(new BoxItem(new Box("74848",20,30,15), 1));
                products.add(new BoxItem(new Box("74850",20,30,15), 1));
                products.add(new BoxItem(new Box("74852",20,30,15), 1));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Json files", "json");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(filter);

                if (fileChooser.showSaveDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    Container match = packager.pack(products, deadline);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Placement.class, new PlacementSerializer());
                    Gson gson = gsonBuilder.create();
                    Type type = new TypeToken<Container>() {}.getType();

                    try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
                        writer.write(gson.toJson(match, type));
                    }

                    if(match == null)
                        JOptionPane.showMessageDialog(view.getFrame(),
                                "Geen doos pas in deze container",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                }
            } catch(RuntimeException ex) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        ex.getMessage(),
                        "Waarschuwing",
                        JOptionPane.WARNING_MESSAGE);
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Opslaan mislukt, probeer het opnieuw.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        Object newValue = propertyChangeEvent.getNewValue();
    }

    public class PlacementSerializer implements JsonSerializer<Placement> {

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
