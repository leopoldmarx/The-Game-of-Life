package com.leopoldmarx.thegameoflife.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSlider.IndicatorPosition;
import com.leopoldmarx.thegameoflife.grid.Grid;
import com.leopoldmarx.thegameoflife.grid.Square;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Is the main application of the entire program.
 * 
 * @author Leopold Marx
 */
public class ViewMain extends Application {

	private Stage window;
	private BorderPane borderPane = new BorderPane();
	private BorderPane mainBorderPane = new BorderPane();
	
	private HBox topHBox = new HBox();
	private HBox bottomHBox = new HBox();
	
	private Canvas canvas = new Canvas();
	private GraphicsContext gc;
	
	private Grid grid = new Grid(50, 25);
	private Grid hoverGrid = null;
	private double mouseX, mouseY;
	
	private int oldX, oldY;
	private boolean dragged = false;
	
	private Timeline timeline;
	private Integer totalGenerations = 0;
	
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
			topHBox   .setSpacing((window.getWidth() - 750) / 6 + 13);
			bottomHBox.setSpacing((window.getWidth() - 750) / 7 + 10);
		});

		
		Label resolutionLabel = new Label("Resolution:");
		JFXSlider resolutionSlider = new JFXSlider(10, 50, 20);
		Label widthLabel = new Label("Width:");
		Spinner<Integer> widthSpinner = new Spinner<>();
		Label heightLabel = new Label("Height:");
		Spinner<Integer> heightSpinner = new Spinner<>();
		JFXButton refreshButton = new JFXButton("Refresh");

		JFXButton startButton = new JFXButton("Start");
		JFXButton stopButton  = new JFXButton("Stop");
		JFXButton stepButton  = new JFXButton("Step");
		JFXButton clearButton = new JFXButton("Clear");
		Label fpsLabel = new Label("FPS:");
		Spinner<Integer> fpsSpinner = new Spinner<>();
		Label totalGenerationsLabel = new Label (totalGenerations.toString());
		JFXCheckBox toroidalArrayCheckBox = new JFXCheckBox("Toroidal Array");
		
		//Top
		resolutionLabel.setFont(COMMONFONT);
		resolutionLabel.setPadding(new Insets(7,0,0,10));
		resolutionLabel.setPrefWidth(110);
		
		resolutionSlider.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		resolutionSlider.setPrefWidth(100);
		resolutionSlider.setPadding(new Insets(17,0,0,0));
		resolutionSlider.setIndicatorPosition(IndicatorPosition.RIGHT);
		
		widthLabel.setFont(COMMONFONT);
		widthLabel.setPadding(new Insets(7,0,0,0));
		
		SpinnerValueFactory svfW = new SpinnerValueFactory
				.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		svfW.setValue(30);
		
		widthSpinner.setValueFactory(svfW);
		widthSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		widthSpinner.setPrefWidth(100);
		widthSpinner.setEditable(true);
		
		widthSpinner.setOnMouseClicked(e -> {
			grid.setWidth(widthSpinner.getValue());
		});
		
		heightLabel.setFont(COMMONFONT);
		heightLabel.setPadding(new Insets(7,0,0,0));
		heightLabel.setPrefWidth(70);
		
		SpinnerValueFactory svfH = new SpinnerValueFactory
				.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		svfH.setValue(20);
		
		heightSpinner.setValueFactory(svfH);
		heightSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		heightSpinner.setPrefWidth(100);
		heightSpinner.setEditable(true);
		
		heightSpinner.setOnMouseClicked(e -> {
			grid.setHeight(heightSpinner.getValue());
		});
		
		refreshButton.setFont(COMMONFONT);
		refreshButton.setPadding(new Insets(7));
		
		refreshButton.setOnAction(e -> {
			grid.setWidth(widthSpinner.getValue());
			grid.setHeight(heightSpinner.getValue());
			grid.setResolution((int)(resolutionSlider.getValue()));
			rePaint();
		});
		
		topHBox.setSpacing(13);
		topHBox.setPadding(new Insets(10));
		topHBox.getChildren().addAll(
				resolutionLabel,
				resolutionSlider,
				widthLabel,
				widthSpinner,
				heightLabel,
				heightSpinner,
				refreshButton);
		
		//Bottom
		startButton.setFont(COMMONFONT);
		startButton.setPadding(new Insets(10));
		startButton.setRipplerFill(Color.STEELBLUE);
		
		startButton.setOnAction(e -> {
			startButton          .setDisable(true);
			stepButton           .setDisable(true);
			stopButton           .setDisable(false);
			clearButton          .setDisable(true);
			resolutionSlider     .setDisable(true);
			widthSpinner         .setDisable(true);
			heightSpinner        .setDisable(true);
			toroidalArrayCheckBox.setDisable(true);
			fpsSpinner           .setDisable(true);
			refreshButton        .setDisable(true);
			
			if (timeline != null) timeline.stop();
			
			timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.getKeyFrames().add(
					new KeyFrame(Duration.millis(
							1000 / fpsSpinner.getValue()), e1 -> {
								totalGenerationsLabel.setText(
										totalGenerations.toString());
								totalGenerations++;
								grid.nextGeneration();
								rePaint();
					})
			);
			timeline.playFromStart();
		});
		
		stopButton.setFont(COMMONFONT);
		stopButton.setPadding(new Insets(10));
		stopButton.setRipplerFill(Color.STEELBLUE);
		stopButton.setDisable(true);
		
		stopButton.setOnAction(e -> {
			stopButton           .setDisable(true);
			startButton          .setDisable(false);
			stepButton           .setDisable(false);
			clearButton          .setDisable(false);
			resolutionSlider     .setDisable(false);
			widthSpinner         .setDisable(false);
			heightSpinner        .setDisable(false);
			toroidalArrayCheckBox.setDisable(false);
			fpsSpinner           .setDisable(false);
			refreshButton        .setDisable(false);
			
			timeline.stop();
		});
		
		stepButton.setFont(COMMONFONT);
		stepButton.setPadding(new Insets(10));
		stepButton.setRipplerFill(Color.STEELBLUE);
		
		stepButton.setOnAction(e -> {
			totalGenerations++;
			totalGenerationsLabel.setText(
					totalGenerations.toString());
			grid.nextGeneration();
			rePaint();
		});
		
		clearButton.setFont(COMMONFONT);
		clearButton.setPadding(new Insets(10));
		clearButton.setRipplerFill(Color.STEELBLUE);
		
		clearButton.setOnAction(e -> {
			grid.getArray().clear();
			totalGenerations = 0;
			totalGenerationsLabel.setText(totalGenerations.toString());
			
			rePaint();
		});
		
		fpsLabel.setFont(COMMONFONT);
		fpsLabel.setPadding(new Insets(10, -5, 0, 10));
		
		SpinnerValueFactory svfFPS = new SpinnerValueFactory
				.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		svfFPS.setValue(5);
		
		fpsSpinner.setValueFactory(svfFPS);
		fpsSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		fpsSpinner.setPrefWidth(100);
		fpsSpinner.setEditable(true);
		
		totalGenerationsLabel.setFont(COMMONFONT);
		totalGenerationsLabel.setPadding(new Insets(7, 0, 0, 10));
		
		toroidalArrayCheckBox.setFont(COMMONFONT);
		toroidalArrayCheckBox.setPadding(new Insets(10, 0, 0, 0));
		toroidalArrayCheckBox.setStyle("-fx-padding: 8;");
		
		toroidalArrayCheckBox.setOnAction(e -> 
				grid.setToroidalArray(
						grid.isToroidalArray() ? false : true));

		bottomHBox.setSpacing(38);
		bottomHBox.setPadding(new Insets(10));
		bottomHBox.getChildren().addAll(
				startButton,
				stopButton,
				stepButton,
				clearButton,
				fpsLabel,
				fpsSpinner,
				totalGenerationsLabel,
				toroidalArrayCheckBox);
		
		//Center
		gc = canvas.getGraphicsContext2D();
		rePaint();
		
		canvas.setOnMouseMoved(e -> {
			if (hoverGrid != null) {
				mouseX = e.getX();
				mouseY = e.getY();
				
				rePaint();
			}
		});
		
		canvas.setOnMouseClicked(e -> {
			if (hoverGrid != null) {
				if (e.getButton().toString().equals("PRIMARY")){
					for (Square s : hoverGrid.getArray()) 
						grid.addSquare(
								(int) (s.getX() + mouseX / grid.getResolution()),
								(int) (s.getY() + mouseY / grid.getResolution()));
					
					hoverGrid = null;
				}
				else 
					hoverGrid.rotate();
				
				rePaint();
			}
			else {
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
		
		//Menu Bar
		final Menu fileMenu = new Menu("_File");
		
		MenuItem newMenuItem      = new MenuItem("_New");
		MenuItem openFileMenuItem = new MenuItem("_Open File...");
		MenuItem saveMenuItem     = new MenuItem("_Save");
		MenuItem saveAsMenuItem   = new MenuItem("Save _As...");
		
		fileMenu.getItems().addAll(
				newMenuItem,
				openFileMenuItem,
				new SeparatorMenuItem(),
				saveMenuItem,
				saveAsMenuItem);
		
		final Menu optionsMenu = new Menu("_Options");
		
		MenuItem preferencesMenuItem = new MenuItem("_Preferences...");
		
		optionsMenu.getItems().add(
				preferencesMenuItem);
		
		final Menu insertMenu = new Menu("_Insert");
		
		MenuItem gliderMenuItem = new MenuItem("_Glider");
		MenuItem gospersGliderGunMenuItem = new MenuItem("G_osper's Glider Gun");
		MenuItem lightweightSpaceshipMenuItem = new MenuItem("_Lightweight Spaceship");
		
		gliderMenuItem.setOnAction(e -> 
				hoverGrid = Grid.glider());
		
		gospersGliderGunMenuItem.setOnAction(e -> 
				hoverGrid = Grid.gospersGliderGun());
		
		lightweightSpaceshipMenuItem.setOnAction(e -> 
				hoverGrid = Grid.lightweightSpaceship());
		
		insertMenu.getItems().addAll(
				gliderMenuItem,
				gospersGliderGunMenuItem,
				lightweightSpaceshipMenuItem);
		
		final Menu helpMenu = new Menu("_Help");
		
		MenuItem introductionMenuItem = new MenuItem("_Introduction...");
		MenuItem examplesMenuItem = new MenuItem("_Exaples...");
		MenuItem rulesMenuItem = new MenuItem("_Rules...");
		
		helpMenu.getItems().addAll(
				introductionMenuItem,
				examplesMenuItem,
				rulesMenuItem);
		
		MenuBar menuBar = new MenuBar();
		menuBar.setStyle("-fx-background-color: Whitesmoke;"
				+ "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 16;");
		menuBar.getMenus().addAll(
				fileMenu,
				optionsMenu,
				insertMenu,
				helpMenu);
		
		mainBorderPane.setTop(topHBox);
		mainBorderPane.setCenter(canvas);
		mainBorderPane.setBottom(bottomHBox);
		
		borderPane.setTop(menuBar);
		borderPane.setCenter(mainBorderPane);
		
		Scene scene = new Scene(borderPane);
		window.setScene(scene);
		window.show();
	}
	
	/**
	 * Re-draws grid on canvas.
	 */
	private void rePaint() {
		int resolution = grid.getResolution();
		
		canvas.setWidth (grid.getWidth()  * resolution);
		canvas.setHeight(grid.getHeight() * resolution);
		window.setMinWidth ((grid.getWidth()  * resolution + 100) > 750 
				? grid.getWidth() * resolution + 100 : 750);
		window.setMinHeight(grid.getHeight() * resolution + 210);
		
		topHBox   .setSpacing((window.getWidth() - 750) / 6 + 13);
		bottomHBox.setSpacing((window.getWidth() - 750) / 7 + 10);
		
		gc.setFill(Color.WHITESMOKE);
		gc.fillRect(0, 0, grid.getWidth() * resolution, grid.getHeight() * resolution);
		
		gc.setFill(Color.BLACK);
		for (int y = 0; y < grid.getHeight(); y++){
			for (int x = 0; x < grid.getWidth(); x++){
				if (grid.getValue(x, y))
					gc.fillRect(x * resolution, y * resolution, resolution, resolution);
			}
		}
		
		if (hoverGrid != null) {
			int x = (int) (mouseX / resolution);
			int y = (int) (mouseY / resolution);
			
			for (int i = 0; i < hoverGrid.getHeight(); i++) {
				for (int j = 0; j < hoverGrid.getWidth(); j++) {
					if (hoverGrid.getValue(j, i)) {
						gc.setFill(Color.GREY);
						gc.fillRect((j + x) * resolution, (i + y) * resolution, resolution, resolution);
					}
				}
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
