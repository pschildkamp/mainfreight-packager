package mvc;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import packing.*;
import packing.Box;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controller implements PropertyChangeListener {
    private View view;
    private Model model;
    private Stopwatch stopwatch;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        this.model.addListener(this);

        this.stopwatch = new Stopwatch();

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

                Packager packager = new Packager((Dimension) view.getContainerComboBox().getSelectedItem());
                Integer chosenTimeout = 10; //Default 10 seconds timeout.
                chosenTimeout = (Integer) view.getTimeOutComboBox().getSelectedItem();

                List<Box> products = new ArrayList<>();
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("74808",100,120,75));
                products.add(new Box("73770",20,30,15));
                products.add(new Box("74844_1",60,40,28));
                products.add(new Box("74844_2",60,40,28));
                products.add(new Box("74844_3",60,40,28));
                products.add(new Box("74846",30,40,15));
                products.add(new Box("73767_1",60,50,15));
                products.add(new Box("73767_2",60,50,15));
                products.add(new Box("74848",20,30,15));
                products.add(new Box("74850",20,30,15));
                products.add(new Box("74852",20,30,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("74808",100,120,75));
                products.add(new Box("73770",20,30,15));
                products.add(new Box("74844_1",60,40,28));
                products.add(new Box("74844_2",60,40,28));
                products.add(new Box("74844_3",60,40,28));
                products.add(new Box("74846",30,40,15));
                products.add(new Box("73767_1",60,50,15));
                products.add(new Box("73767_2",60,50,15));
                products.add(new Box("74848",20,30,15));
                products.add(new Box("74850",20,30,15));
                products.add(new Box("74852",20,30,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("74808",100,120,75));
                products.add(new Box("73770",20,30,15));
                products.add(new Box("74844_1",60,40,28));
                products.add(new Box("74844_2",60,40,28));
                products.add(new Box("74844_3",60,40,28));
                products.add(new Box("74846",30,40,15));
                products.add(new Box("73767_1",60,50,15));
                products.add(new Box("73767_2",60,50,15));
                products.add(new Box("74848",20,30,15));
                products.add(new Box("74850",20,30,15));
                products.add(new Box("74852",20,30,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("74808",100,120,75));
                products.add(new Box("73770",20,30,15));
                products.add(new Box("74844_1",60,40,28));
                products.add(new Box("74844_2",60,40,28));
                products.add(new Box("74844_3",60,40,28));
                products.add(new Box("74846",30,40,15));
                products.add(new Box("73767_1",60,50,15));
                products.add(new Box("73767_2",60,50,15));
                products.add(new Box("74848",20,30,15));
                products.add(new Box("74850",20,30,15));
                products.add(new Box("74852",20,30,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("74808",100,120,75));
                products.add(new Box("73770",20,30,15));
                products.add(new Box("74844_1",60,40,28));
                products.add(new Box("74844_2",60,40,28));
                products.add(new Box("74844_3",60,40,28));
                products.add(new Box("74846",30,40,15));
                products.add(new Box("73767_1",60,50,15));
                products.add(new Box("73767_2",60,50,15));
                products.add(new Box("74848",20,30,15));
                products.add(new Box("74850",20,30,15));
                products.add(new Box("74852",20,30,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));
                products.add(new Box("74760_2",30,20,15));
                products.add(new Box("74757",30,40,15));
                products.add(new Box("74808",100,120,75));
                products.add(new Box("73770",20,30,15));
                products.add(new Box("74844_1",60,40,28));
                products.add(new Box("74844_2",60,40,28));
                products.add(new Box("74844_3",60,40,28));
                products.add(new Box("74846",30,40,15));
                products.add(new Box("73767_1",60,50,15));
                products.add(new Box("73767_2",60,50,15));
                products.add(new Box("74848",20,30,15));
                products.add(new Box("74850",20,30,15));
                products.add(new Box("74852",20,30,15));
                products.add(new Box("72407",20,30,15));
                products.add(new Box("74809",100,120,52));
                products.add(new Box("71535",30,40,15));
                products.add(new Box("74780",20,30,15));
                products.add(new Box("74760_1",30,20,15));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Json files", "json");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(filter);

                if (fileChooser.showSaveDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    long deadline = System.currentTimeMillis() + (chosenTimeout * 1000); //to seconds
                    Container match = null;
//                    AtomicInteger atomicTimeout = new AtomicInteger(chosenTimeout);
//
//                    stopwatch.start(() -> {
//                        System.out.println("LAMBDA");
//                        Integer remainingTimeout = atomicTimeout.getAndDecrement();
//                        this.view.getTimeoutCounter().setText(String.format("Resterende tijd: %d", remainingTimeout));
//                        this.view.repaint();
//                    });

                    final ExecutorService executor = Executors.newSingleThreadExecutor();
                    Future<Container> future = executor.submit(() -> packager.pack(products, deadline));

                    try {
                        match = future.get();
                    } catch(ContainerTooSmallException ctse) {
                        JOptionPane.showMessageDialog(view.getFrame(),
                                ctse.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch(InterruptedException | ExecutionException futureException) {
                        throw new RuntimeException(futureException);
                    } finally {
                        executor.shutdownNow();
                    }

                    if(match == null) {
                        JOptionPane.showMessageDialog(view.getFrame(),
                                "Geen doos pas in deze container",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Placement.class, new PlacementSerializer());
                        Gson gson = gsonBuilder.create();
                        Type type = new TypeToken<Container>() {}.getType();

                        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
                            writer.write(gson.toJson(match, type));
                            int totalAmountOfPackedBoxes = 0;
                            for(Level level : match.getLevels()) {
                                totalAmountOfPackedBoxes += level.getTotalAmountOfBoxes();
                            }
                            JOptionPane.showMessageDialog(view.getFrame(), "Inpak bestand " + file.getName() + " succesvol gevuld met " + totalAmountOfPackedBoxes + " dozen.", "Succes", JOptionPane.INFORMATION_MESSAGE);
                        }  catch (IOException ioException) {
                            JOptionPane.showMessageDialog(view.getFrame(),
                                    "Opslaan mislukt, probeer het opnieuw.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch(RuntimeException ex) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        ex.getMessage(),
                        "Waarschuwing",
                        JOptionPane.WARNING_MESSAGE);
            } finally {
                this.stopwatch.stop();
            }
        });

        view.getRemoveBoxBtn().addActionListener(e -> {
            System.out.println(e);
        });

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public class Stopwatch {
        private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        public void start(Runnable task) {
            executor.scheduleWithFixedDelay(task, 0L, 1L, TimeUnit.SECONDS);
        }

        public void stop() {
            executor.shutdownNow();
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
