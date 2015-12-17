package com.leopoldmarx.thegameoflife.view;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSlider.IndicatorPosition;
import com.leopoldmarx.thegameoflife.driver.Program;
import com.leopoldmarx.thegameoflife.file.FileManager;
import com.leopoldmarx.thegameoflife.grid.Grid;
import com.leopoldmarx.thegameoflife.grid.Square;
import com.leopoldmarx.thegameoflife.insert.Insert;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Is the main application of the entire program.
 * 
 * @author Leopold Marx
 */
public class ViewMain extends Application implements Runnable {

	private Stage window;
	private BorderPane borderPane = new BorderPane();
	private BorderPane mainBorderPane = new BorderPane();
	
	private FileManager fileManager = new FileManager();
	private File location;
	
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
	
	private FileChooser fileChooser = new FileChooser();
	
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

		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter(
						"The Game of Life Files (.tgol)",
						"*.tgol"));
		
		//Top
		Label resolutionLabel = new Label("Resolution:");
		JFXSlider resolutionSlider = new JFXSlider(10, 50, 20);
		Label widthLabel = new Label("Width:");
		Spinner<Integer> widthSpinner = new Spinner<>();
		Label heightLabel = new Label("Height:");
		Spinner<Integer> heightSpinner = new Spinner<>();
		JFXButton refreshButton = new JFXButton("Refresh");

		//Bottom
		JFXButton startButton = new JFXButton("Start");
		JFXButton stopButton  = new JFXButton("Stop");
		JFXButton stepButton  = new JFXButton("Step");
		JFXButton clearButton = new JFXButton("Clear");
		Label fpsLabel = new Label("FPS:");
		Spinner<Integer> fpsSpinner = new Spinner<>();
		Label totalGenerationsLabel = new Label(grid.getGenerations().toString());
		JFXCheckBox toroidalArrayCheckBox = new JFXCheckBox("Toroidal Array");
		
		//Menus
		final Menu fileMenu = new Menu("_File");
		
		MenuItem newMenuItem      = new MenuItem("_New");
		MenuItem openFileMenuItem = new MenuItem("_Open File...");
		MenuItem saveMenuItem     = new MenuItem("_Save");
		MenuItem saveAsMenuItem   = new MenuItem("Save _As...");
		
		final Menu insertMenu = new Menu("_Insert");
		Insert insert = Program.getInstance().getInsert();
		
		MenuItem editMenuItem = new MenuItem("_Edit...");
		
		final Menu helpMenu = new Menu("_Help");
		
		MenuItem introductionMenuItem = new MenuItem("_Introduction...");
		MenuItem examplesMenuItem = new MenuItem("_Exaples...");
		MenuItem rulesMenuItem = new MenuItem("_Rules...");
		
		MenuBar menuBar = new MenuBar();
		
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
		
		resolutionSlider.setOnMouseDragReleased(e -> {
			grid.setResolution((int)(resolutionSlider.getValue()));
			rePaint();
		});
		
		widthLabel.setFont(COMMONFONT);
		widthLabel.setPadding(new Insets(7,0,0,0));
		widthLabel.setPrefWidth(60);
		
		SpinnerValueFactory svfW = new SpinnerValueFactory
				.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		svfW.setValue(50);
		
		widthSpinner.setValueFactory(svfW);
		widthSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		widthSpinner.setPrefWidth(100);
		widthSpinner.setEditable(true);
		
		widthSpinner.valueProperty().addListener(e ->
					grid.setWidth(widthSpinner.getValue()));
		
		heightLabel.setFont(COMMONFONT);
		heightLabel.setPadding(new Insets(7,0,0,0));
		heightLabel.setPrefWidth(65);
		
		SpinnerValueFactory svfH = new SpinnerValueFactory
				.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		svfH.setValue(25);
		
		heightSpinner.setValueFactory(svfH);
		heightSpinner.setStyle(
				  "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 20;");
		heightSpinner.setPrefWidth(100);
		heightSpinner.setEditable(true);
		
		heightSpinner.valueProperty().addListener(e ->
				grid.setHeight(heightSpinner.getValue()));
		
		refreshButton.setFont(COMMONFONT);
		refreshButton.setPadding(new Insets(7));
		refreshButton.setRipplerFill(Color.CADETBLUE);
		
		refreshButton.setOnAction(e -> {
			grid.setWidth(widthSpinner.getValue());
			grid.setHeight(heightSpinner.getValue());
			grid.setResolution((int)resolutionSlider.getValue());
			rePaint();
		});
		
		topHBox.setSpacing(13);
		topHBox.setPadding(new Insets(0, 10, 10, 10));
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
		startButton.setRipplerFill(Color.GREEN);
		
		startButton.setOnAction(e -> {
			insertMenu.setDisable(true);
			fileMenu.setDisable(true);
			startButton          .setDisable(true);
			stepButton           .setDisable(true);
			stopButton           .setDisable(false);
			resolutionSlider     .setDisable(true);
			widthSpinner         .setDisable(true);
			heightSpinner        .setDisable(true);
			toroidalArrayCheckBox.setDisable(true);
			fpsSpinner           .setDisable(true);
			refreshButton        .setDisable(true);
			
			timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.getKeyFrames().add(
					new KeyFrame(Duration.millis(
							1000 / fpsSpinner.getValue()), e1 -> {
								totalGenerationsLabel.setText(
										grid.getGenerations().toString());
								grid.incrementGenerations();
								grid.nextGeneration();
								rePaint();
					})
			);
			timeline.playFromStart();
		});
		
		stopButton.setFont(COMMONFONT);
		stopButton.setPadding(new Insets(10));
		stopButton.setRipplerFill(Color.RED);
		stopButton.setDisable(true);
		
		stopButton.setOnAction(e -> {
			insertMenu.setDisable(false);
			fileMenu.setDisable(false);
			stopButton           .setDisable(true);
			startButton          .setDisable(false);
			stepButton           .setDisable(false);
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
		stepButton.setRipplerFill(Color.CADETBLUE);
		
		stepButton.setOnAction(e -> {
			grid.incrementGenerations();
			totalGenerationsLabel.setText(
					grid.getGenerations().toString());
			grid.nextGeneration();
			rePaint();
		});
		
		clearButton.setFont(COMMONFONT);
		clearButton.setPadding(new Insets(10));
		clearButton.setRipplerFill(Color.STEELBLUE);
		
		clearButton.setOnAction(e -> {
			location = null;
			grid.getArray().clear();
			grid.setGenerations(0);
			totalGenerationsLabel.setText(
					grid.getGenerations().toString());
			
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
		toroidalArrayCheckBox.setCheckedColor(Color.CORNFLOWERBLUE);
		
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
				else if (e.getButton().toString().equals("SECONDARY"))
					hoverGrid.rotate();
				else
					hoverGrid.flip();
				
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
		newMenuItem.setAccelerator(
				new KeyCodeCombination(
						KeyCode.N, KeyCombination.CONTROL_DOWN));
		
		newMenuItem.setOnAction(e -> {
			location = null;
			grid.getArray().clear();
			grid.setGenerations(0);
			totalGenerationsLabel.setText(
					grid.getGenerations().toString());
			
			rePaint();
		});
		
		openFileMenuItem.setOnAction(e -> {
			fileChooser.setTitle("Open Resource File");
			
			location = fileChooser.showOpenDialog(window);
			
			if (location != null) {
				fileManager.setLocation(location.toPath());
				grid = fileManager.openFile();
				rePaint();
				
				svfW.setValue(grid.getWidth());
				svfH.setValue(grid.getHeight());
				toroidalArrayCheckBox.setSelected(grid.isToroidalArray());
				totalGenerationsLabel.setText(
						grid.getGenerations().toString());
			}
		});
		
		saveMenuItem.setAccelerator(
				new KeyCodeCombination(
						KeyCode.S, KeyCombination.CONTROL_DOWN));
		
		saveMenuItem.setOnAction(e -> {
			if (location != null) {
				fileManager.setLocation(location.toPath());
				fileManager.saveFile(grid);
			}
			else {
				fileChooser.setTitle("Save as");
				
				location = fileChooser.showSaveDialog(window);
				
				if (location != null) {
					fileManager.setLocation(location.toPath());
					fileManager.saveFile(grid);
				}
			}
		});
		
		saveAsMenuItem.setOnAction(e -> {
			fileChooser.setTitle("Save as");
			
			location = fileChooser.showSaveDialog(window);
			
			if (location != null) {
				fileManager.setLocation(location.toPath());
				fileManager.saveFile(grid);
			}
		});
		
		fileMenu.getItems().addAll(
				newMenuItem,
				openFileMenuItem,
				new SeparatorMenuItem(),
				saveMenuItem,
				saveAsMenuItem);
		
		for (Grid g : insert.getArray()) {
			MenuItem mi = new MenuItem(g.getName());
			
			mi.setOnAction(e -> 
					hoverGrid = g);
			
			insertMenu.getItems().add(mi);
		}
		
		insertMenu.getItems().add(new SeparatorMenuItem());
		insertMenu.getItems().add(editMenuItem);
		
		editMenuItem.setOnAction(e -> {
			ViewEditInsert vei = new ViewEditInsert();
			
			vei.display();
			
			insertMenu.getItems().clear();
			for (Grid g : insert.getArray()) {
				MenuItem mi = new MenuItem(g.getName());
				
				mi.setOnAction(e1 -> 
						hoverGrid = g);
				
				insertMenu.getItems().add(mi);
			}
			
			insertMenu.getItems().add(new SeparatorMenuItem());
			insertMenu.getItems().add(editMenuItem);
		});
		
		helpMenu.getItems().addAll(
				introductionMenuItem,
				examplesMenuItem,
				rulesMenuItem);
		
		menuBar.setStyle("-fx-background-color: Whitesmoke;"
				+ "-fx-font: Devanagari MT;"
				+ "-fx-font-size: 16;");
		
		menuBar.getMenus().addAll(
				fileMenu,
				insertMenu,
				helpMenu);
		
		mainBorderPane.setTop(topHBox);
		mainBorderPane.setCenter(canvas);
		mainBorderPane.setBottom(bottomHBox);
		
		borderPane.setTop(menuBar);
		borderPane.setCenter(mainBorderPane);
		
		window.setOnCloseRequest(e ->
				Program.getInstance().getFileManager().saveInsert());
		
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
		window.setMinHeight(grid.getHeight() * resolution + 200);
		
		topHBox   .setSpacing((window.getWidth() - 750) / 6 + 13);
		bottomHBox.setSpacing((window.getWidth() - 750) / 7 + 10);
		
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
		
		//Hover Grid
		if (hoverGrid != null) {
			int x = (int) (mouseX / resolution);
			int y = (int) (mouseY / resolution);

			gc.setFill(Color.GREY);
			for (int i = 0; i < hoverGrid.getHeight(); i++) {
				for (int j = 0; j < hoverGrid.getWidth(); j++) {
					if (hoverGrid.getValue(j, i))
						gc.fillRect(
								(j + x) * resolution,
								(i + y) * resolution,
								resolution,
								resolution);
				}
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

	@Override
	public void run() {
		launch();
	}
}