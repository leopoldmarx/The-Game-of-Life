package com.leopoldmarx.thegameoflife.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import com.leopoldmarx.thegameoflife.grid.Grid;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ViewMain extends Application {

	private Stage window;
	private BorderPane mainBorderPane = new BorderPane();
	
	private HBox topHBox = new HBox();
	private HBox bottomHBox = new HBox();
	
	private Canvas canvas = new Canvas();
	private GraphicsContext gc;
	
	private Grid grid = new Grid(30, 20);
	
	private int oldX, oldY;
	private boolean dragged = false;
	
	private static final int  FONTSIZE = 20;
	private static final Font COMMONFONT = new  Font("Devanagari MT", FONTSIZE);
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		window = primaryStage;
		
		window.setHeight(600);
		window.setWidth(750);
		window.setMinHeight(600);
		window.setMinWidth(750);
		window.setTitle("The Game of Life");
		
		window.widthProperty().addListener(e -> {
			topHBox   .setSpacing((window.getWidth() - 750) / 5 + 38);
			bottomHBox.setSpacing((window.getWidth() - 750) / 6 + 13);
		});
		
		//Top
		JFXButton startButton = new JFXButton("Start");
		JFXButton stopButton  = new JFXButton("Stop");
		JFXButton stepButton  = new JFXButton("Step");
		
		startButton.setFont(COMMONFONT);
		startButton.setPadding(new Insets(10));
		startButton.setRipplerFill(Color.STEELBLUE);
		
		startButton.setOnAction(e -> {
			startButton.setDisable(true);
			stepButton .setDisable(true);
			stopButton .setDisable(false);
			//TODO Add timed looping mechanism.
		});
		
		stopButton.setFont(COMMONFONT);
		stopButton.setPadding(new Insets(10));
		stopButton.setRipplerFill(Color.STEELBLUE);
		stopButton.setDisable(true);
		
		stopButton.setOnAction(e -> {
			stopButton .setDisable(true);
			startButton.setDisable(false);
			stepButton .setDisable(false);
		});
		
		stepButton.setFont(COMMONFONT);
		stepButton.setPadding(new Insets(10));
		stepButton.setRipplerFill(Color.STEELBLUE);
		
		stepButton.setOnAction(e -> {
			grid.nextGeneration();
			rePaint();
		});
		
		Label fpsLabel = new Label("FPS:");
		fpsLabel.setFont(COMMONFONT);
		fpsLabel.setPadding(new Insets(10, -5, 0, 10));
		
		Spinner<Integer> fpsSpinner = new Spinner<>(1, Double.MAX_VALUE, 20, 1);
		fpsSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		fpsSpinner.setPrefWidth(100);
		fpsSpinner.setEditable(true);
		
		JFXCheckBox toroidalArrayCheckBox = new JFXCheckBox("Toroidal Array");
		toroidalArrayCheckBox.setFont(COMMONFONT);
		toroidalArrayCheckBox.setPadding(new Insets(10, 0, 0, 0));
		toroidalArrayCheckBox.setStyle("-fx-padding: 8;");
		
		toroidalArrayCheckBox.setOnAction(e -> 
				grid.setToroidalArray(
						grid.isToroidalArray() ? false : true));

		topHBox.setSpacing(38);
		topHBox.setPadding(new Insets(10));
		topHBox.getChildren().addAll(
				startButton,
				stopButton,
				stepButton,
				fpsLabel,
				fpsSpinner,
				toroidalArrayCheckBox);
		
		//Bottom
		Label resolutionLabel = new Label("Resolution:");
		resolutionLabel.setFont(COMMONFONT);
		resolutionLabel.setPadding(new Insets(7,0,0,10));
		resolutionLabel.setPrefWidth(110);
		
		JFXSlider resolutionSlider = new JFXSlider(10, 50, 25);
		resolutionSlider.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		resolutionSlider.setPrefWidth(100);
		resolutionSlider.setPadding(new Insets(17,0,0,0));
		
		Label widthLabel = new Label("Width:");
		widthLabel.setFont(COMMONFONT);
		widthLabel.setPadding(new Insets(7,0,0,0));
		
		SpinnerValueFactory svfW = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		svfW.setValue(20);
		
		Spinner<Integer> widthSpinner = new Spinner<>();
		widthSpinner.setValueFactory(svfW);
		widthSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		widthSpinner.setPrefWidth(100);
		widthSpinner.setEditable(true);
		
		widthSpinner.setOnMouseClicked(e -> {
			grid.setWidth(widthSpinner.getValue());
		});

		Label heightLabel = new Label("Height:");
		heightLabel.setFont(COMMONFONT);
		heightLabel.setPadding(new Insets(7,0,0,0));
		heightLabel.setPrefWidth(70);
		
		SpinnerValueFactory svfH = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		svfH.setValue(20);
		
		Spinner<Integer> heightSpinner = new Spinner<>();
		heightSpinner.setValueFactory(svfH);
		heightSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		heightSpinner.setPrefWidth(100);
		heightSpinner.setEditable(true);
		
		heightSpinner.setOnMouseClicked(e -> {
			grid.setHeight(heightSpinner.getValue());
		});
		
		JFXButton refreshButton = new JFXButton("Refresh");
		refreshButton.setFont(COMMONFONT);
		refreshButton.setPadding(new Insets(7));
		
		refreshButton.setOnAction(e -> {
			grid.setWidth(widthSpinner.getValue());
			grid.setHeight(heightSpinner.getValue());
			grid.setResolution((int)(resolutionSlider.getValue()));
			rePaint();
		});
		
		//Center
		grid.addSquare(1, 1);
		grid.addSquare(2, 2);
		grid.addSquare(2, 3);
		grid.addSquare(3, 1);
		grid.addSquare(3, 2);
		
		gc = canvas.getGraphicsContext2D();
		rePaint();
		
		canvas.setOnMouseClicked(e -> {
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
		});
		
		canvas.setOnMouseDragged(e -> {
			
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
		});
		
		bottomHBox.setSpacing(13);
		bottomHBox.setPadding(new Insets(10));
		bottomHBox.getChildren().addAll(
				resolutionLabel,
				resolutionSlider,
				widthLabel,
				widthSpinner,
				heightLabel,
				heightSpinner,
				refreshButton);
		
		mainBorderPane.setTop(topHBox);
		mainBorderPane.setCenter(canvas);
		mainBorderPane.setBottom(bottomHBox);
		
		Scene scene = new Scene(mainBorderPane);
		window.setScene(scene);
		window.show();
	}
	
	private void rePaint() {
		int resolution = grid.getResolution();
		
		canvas.setWidth (grid.getWidth()  * resolution);
		canvas.setHeight(grid.getHeight() * resolution);
		window.setMinWidth ((grid.getWidth()  * resolution + 100) > 750 
				? grid.getWidth() * resolution + 100 : 750);
		window.setMinHeight(grid.getHeight() * resolution + 200);
		
		for (int y = 0; y < grid.getHeight(); y++){
			for (int x = 0; x < grid.getWidth(); x++){
				if (grid.getValue(x, y)) gc.setFill(Color.BLACK);
				else                     gc.setFill(Color.WHITESMOKE);
				gc.fillRect(x * resolution, y * resolution, resolution, resolution);
			}
		}
		
		//Grid
		gc.setFill(Color.LIGHTGREY);
		
		for (int i = 1; i < grid.getWidth(); i++)
			gc.fillRect(i * resolution - .5, 0, 1, grid.getHeight() * resolution);
		
		for (int i = 1; i < grid.getHeight(); i++)
			gc.fillRect(0, i * resolution - .5, grid.getWidth() * resolution, 1);
		
		//Border
		gc.fillRect(0, 0, 2, canvas.getHeight());
		gc.fillRect(0, 0, canvas.getWidth(), 2);
		gc.fillRect(0, canvas.getHeight() - 2, canvas.getWidth(), 2);
		gc.fillRect(canvas.getWidth() - 2, 0, 2, canvas.getHeight());
	}
}
