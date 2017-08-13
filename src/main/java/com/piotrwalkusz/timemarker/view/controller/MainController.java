package com.piotrwalkusz.timemarker.view.controller;

import com.piotrwalkusz.timemarker.Activity;
import com.piotrwalkusz.timemarker.PersistentTimeline;
import com.piotrwalkusz.timemarker.TimeMarker;
import com.piotrwalkusz.timemarker.Timeline;
import com.piotrwalkusz.timemarker.time.ClockUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainController {

    @FXML private Canvas timelineCanvas;
    @FXML private Text totalTime;

    private final Timeline timeline = new PersistentTimeline();
    private final Thread refreshThread;

    private boolean isTimeMarkStarted;

    public MainController() {
        refreshThread = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    updateLastTimeMarkerIfNeeded();
                    redraw();
                });
                try {
                    Thread.sleep(10_000);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });
        refreshThread.setDaemon(true);
    }

    @FXML private void initialize() {
        refreshThread.start();
    }

    @FXML private void startButton() {
        if (timeline.isAnyActivity()) {
            ChoiceDialog<Activity> dialog = new ChoiceDialog<>(null, timeline.getActivities());
            dialog.setGraphic(null);
            dialog.setHeaderText("Choose activity");
            dialog.showAndWait().ifPresent(activity -> {
                updateLastTimeMarkerIfNeeded();
                timeline.createTimeMarker(activity);
                isTimeMarkStarted = true;
                redraw();
            });
        }
    }

    @FXML private void endButton() {
        updateLastTimeMarkerIfNeeded();
        isTimeMarkStarted = false;
        redraw();
    }

    @FXML private void addButton() {
        TextField activityNameField = new TextField();
        ColorPicker colorPicker = new ColorPicker();
        VBox dialogContent = new VBox();
        dialogContent.getChildren().addAll(activityNameField, colorPicker);

        Dialog<Pair<String, Color>> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(dialogContent);
        dialog.setResultConverter(button -> button == ButtonType.OK ?
                new Pair<>(activityNameField.getText(), colorPicker.getValue()) : null);

        dialog.showAndWait().ifPresent(x -> timeline.createActivity(x.getKey(), x.getValue()));
    }

    private void redraw() {
        GraphicsContext gc = timelineCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, timelineCanvas.getWidth(), timelineCanvas.getHeight());

        for (TimeMarker timeMarker : timeline.getSeenTimeMarkers()) {
            double pixelPerMin = timelineCanvas.getWidth() / 1440;
            int start = (int) (Duration.between(getStartOfDay(), timeMarker.getStartTime()).toMinutes() * pixelPerMin);
            int width = (int) (Duration.between(timeMarker.getStartTime(), timeMarker.getEndTime()).toMinutes() * pixelPerMin);
            width = Math.max(1, width);
            gc.setFill(timeMarker.getActivity().getColor());
            gc.fillRect(start, 0, width + 1, timelineCanvas.getHeight());
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRect(0, 0, timelineCanvas.getWidth(), timelineCanvas.getHeight());

        long totalMinutes = timeline.getTotalActivitiesTimeFrom(getStartOfDay()).toMinutes();
        totalTime.setText(String.format("%d:%d", totalMinutes / 60, totalMinutes % 60));
    }

    private void updateLastTimeMarkerIfNeeded() {
        if (isTimeMarkStarted) {
            timeline.updateLastTimeMarker();
        }
    }

    private LocalDateTime getStartOfDay() {
        return LocalDate.now(ClockUtil.getClock()).atStartOfDay();
    }
}
