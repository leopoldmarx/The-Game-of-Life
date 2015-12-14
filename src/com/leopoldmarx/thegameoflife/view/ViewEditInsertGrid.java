package com.leopoldmarx.thegameoflife.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.leopoldmarx.thegameoflife.grid.Grid;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ViewEditInsertGrid {
	
	private Stage window;
	
	private BorderPane borderPane;
	private HBox topHBox;
	private HBox bottomHBox;
	
	private Canvas canvas;
	private GraphicsContext gc;
	
	private Grid grid;
	private Grid originalGrid;
	
	private boolean dragged = false;
	private int oldX, oldY;
	
	private static final Font COMMONFONT = new Font("Devanagari MT", 20);
	
	public ViewEditInsertGrid(Grid grid) {
		borderPane = new BorderPane();
		topHBox = new HBox();
		bottomHBox = new HBox();
		
		canvas = new Canvas();
		gc = canvas.getGraphicsContext2D();
		
		originalGrid = grid;
		this.grid = grid.clone();
	}
	
	public void display() {
		window = new Stage();
		
		JFXTextField nameField = new JFXTextField();
		JFXButton saveButton = new JFXButton("Save");
		
		//Top
		nameField.setFont(COMMONFONT);
		saveButton.setFont(COMMONFONT);
		
		nameField.setText(grid.getName());
		
		saveButton.setOnAction(e -> {
			originalGrid = grid;
			grid = originalGrid.clone();
		});
		
		topHBox.setSpacing(13);
		topHBox.setPadding(new Insets(0, 10, 10, 10));
		topHBox.getChildren().addAll(
				nameField,
				saveButton);
		
		//Bottom
		Label widthLabel = new Label("Width:");
		Spinner<Integer> widthSpinner = new Spinner<>();
		Label heightLabel = new Label("Height:");
		Spinner<Integer> heightSpinner = new Spinner<>();
		
		widthLabel.setFont(COMMONFONT);
		widthLabel.setPadding(new Insets(7,0,0,0));
		widthLabel.setPrefWidth(60);
		
		SpinnerValueFactory svfW = new SpinnerValueFactory
				.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		svfW.setValue(grid.getWidth());
		
		widthSpinner.setValueFactory(svfW);
		widthSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		widthSpinner.setPrefWidth(100);
		widthSpinner.setEditable(true);
		
		widthSpinner.valueProperty().addListener(e -> {
			grid.setWidth(widthSpinner.getValue());
			rePaint();
		});
		
		heightLabel.setFont(COMMONFONT);
		heightLabel.setPadding(new Insets(7,0,0,0));
		heightLabel.setPrefWidth(65);
		
		SpinnerValueFactory svfH = new SpinnerValueFactory
				.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		svfH.setValue(grid.getHeight());
		
		heightSpinner.setValueFactory(svfH);
		heightSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		heightSpinner.setPrefWidth(100);
		heightSpinner.setEditable(true);
		
		heightSpinner.valueProperty().addListener(e -> {
			grid.setHeight(heightSpinner.getValue());
			rePaint();
		});
		
		bottomHBox.setSpacing(38);
		bottomHBox.setPadding(new Insets(10));
		bottomHBox.getChildren().addAll(
				widthLabel,
				widthSpinner,
				heightLabel,
				heightSpinner);
		
		//Center
		rePaint();
		
		canvas.setOnMouseClicked(e -> {
			if (e.getButton().toString().equals("PRIMARY")) {
				if (!dragged){
					int x = (int) (e.getX() / grid.getResolution());
					int y = (int) (e.getY() / grid.getResolution());
					
					if (grid.getValue(x, y))
						grid.deleteSquare(x, y);
					else
						grid.addSquare(x, y);
				}
				dragged = false;
				rePaint();
			}
		});
		
		canvas.setOnMouseDragged(e -> {
			if (e.getButton().toString().equals("PRIMARY")) {
				dragged = true;
				
				int x = (int) (e.getX() / grid.getResolution());
				int y = (int) (e.getY() / grid.getResolution());
				
				if (x != oldX || y != oldY){
					if (grid.getValue(x, y))
						grid.deleteSquare(x, y);
					else
						grid.addSquare(x, y);
				}
				
				oldX = x;
				oldY = y;
				rePaint();
			}
		});
		
		borderPane.setTop(topHBox);
		borderPane.setCenter(canvas);
		borderPane.setBottom(bottomHBox);
		
		window.widthProperty().addListener(e -> {
			topHBox.setSpacing((window.getWidth() - 367)
					/ (topHBox.getChildren().size() - 1) + 10);
			
			bottomHBox.setSpacing((window.getWidth() - 475)
					/ (bottomHBox.getChildren().size() - 1) + 35);
		});
		
		window.setOnCloseRequest(e -> {
			if (!originalGrid.equals(grid)) {
				Alert alert = new Alert(
						AlertType.CONFIRMATION,
						"Would you like to save changes to " + grid.getName() + "?",
						javafx.scene.control.ButtonType.YES,
						javafx.scene.control.ButtonType.NO,
						javafx.scene.control.ButtonType.CANCEL);
				
				alert.showAndWait();
				
				if (alert.getResult() == javafx.scene.control.ButtonType.YES)
					grid = originalGrid.clone();
				else if (alert.getResult() == javafx.scene.control.ButtonType.CANCEL)
					e.consume();
			}
		});
		
		Scene scene = new Scene(borderPane);
		window.setScene(scene);
		window.showAndWait();
	}
	
	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	private void rePaint() {
		int resolution = grid.getResolution();
		
		canvas.setWidth (grid.getWidth()  * resolution);
		canvas.setHeight(grid.getHeight() * resolution);
//		window.setMinWidth ((grid.getWidth()  * resolution + 100) > 750 
//				? grid.getWidth() * resolution + 100 : 750);
//		window.setMinHeight(grid.getHeight() * resolution + 200);
		
//		topHBox   .setSpacing((window.getWidth() - 750) / 6 + 13);
//		bottomHBox.setSpacing((window.getWidth() - 750) / 7 + 10);
		
		gc.setFill(Color.WHITESMOKE);
		gc.fillRect(0, 0, grid.getWidth() * resolution, grid.getHeight() * resolution);
		
		//Squares
		gc.setFill(Color.BLACK);
		for (int y = 0; y < grid.getHeight(); y++){
			for (int x = 0; x < grid.getWidth(); x++){
				if (grid.getValue(x, y))
					gc.fillRect(
							x * resolution,
							y * resolution,
							resolution,
							resolution);
			}
		}
		
		//Grid
		gc.setFill(Color.LIGHTGREY);
		
		for (int i = 1; i < grid.getWidth(); i++)
			gc.fillRect(
					i * resolution - .5,
					0,
					1,
					grid.getHeight() * resolution);
		
		for (int i = 1; i < grid.getHeight(); i++)
			gc.fillRect(
					0,
					i * resolution - .5,
					grid.getWidth() * resolution,
					1);
		
		//Border
		gc.fillRect(0, 0, 2, canvas.getHeight());
		gc.fillRect(0, 0, canvas.getWidth(), 2);
		gc.fillRect(0, canvas.getHeight() - 2, canvas.getWidth(), 2);
		gc.fillRect(canvas.getWidth() - 2, 0, 2, canvas.getHeight());
	}
}
