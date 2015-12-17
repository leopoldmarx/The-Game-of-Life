package com.leopoldmarx.thegameoflife.view;

import java.util.Set;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.leopoldmarx.thegameoflife.driver.Program;
import com.leopoldmarx.thegameoflife.grid.Grid;
import com.leopoldmarx.thegameoflife.insert.Insert;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewEditInsert {
	
	private Stage window;
	BorderPane borderPane = new BorderPane();
	
	Insert insert = Program.getInstance().getInsert();
	
	GridPane gridPane = new GridPane();
	ScrollPane scrollPane = new ScrollPane();
	
	public void display() {
		
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Edit Inserts");
		
		borderPane.setPadding(new Insets(10));
		
		HBox bottomHBox = new HBox();
		
		gridPane.setVgap(20);
		gridPane.setHgap(10);
		
		scrollPane.setContent(gridPane);
		scrollPane.setStyle("-fx-background-color:transparent;");
		
		reDraw();
		
		JFXButton newInsert = new JFXButton("New");
		JFXButton restoreDefaults = new JFXButton("Restore Defaults");
		
		newInsert.setFont(new Font("Devanagari MT", 18));
		newInsert.setButtonType(ButtonType.RAISED);
		
		newInsert.setOnAction(e -> {
			insert.addInsert(new Grid(4, 4));
			ViewEditInsertGrid veig = new ViewEditInsertGrid(
					insert.getArray().get(
							insert.getArray().size() - 1));
			veig.display();
			if (veig.isSave())
				insert.getArray().set(
						insert.getArray().size() - 1,
						veig.getGrid());
			reDraw();
		});
		
		restoreDefaults.setFont(new Font("Devanagari MT", 18));
		restoreDefaults.setButtonType(ButtonType.RAISED);
		restoreDefaults.setPrefWidth(160);
		restoreDefaults.setTextFill(Color.WHITE);
		restoreDefaults.setStyle(
				"-fx-background-color: rgb(196,67,67);");
		
		restoreDefaults.setOnAction(e -> {
			
			Alert alert = new Alert(
					AlertType.CONFIRMATION,
					"Would you like to restore default inserts?",
					javafx.scene.control.ButtonType.YES,
					javafx.scene.control.ButtonType.NO);
			
			alert.showAndWait();
			
			if (alert.getResult() == javafx.scene.control.ButtonType.YES) {
				insert.restoreDefaults();
				reDraw();
			}
		});
		
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		
		bottomHBox.getChildren().addAll(
				newInsert,
				restoreDefaults);
		
		vBox.getChildren().addAll(
				new Separator(),
				bottomHBox);
		
		window.widthProperty().addListener(e ->
				bottomHBox.setSpacing(window.getWidth() - 160 - 20 - 75));
		
		borderPane.setCenter(scrollPane);
		borderPane.setBottom(vBox);
		
		Scene scene = new Scene(borderPane, 500, 700);
		window.setScene(scene);
		window.showAndWait();
	}
	
	private void reDraw() {
		gridPane.getChildren().clear();
		
		int labelWidth = 0;
		
		for (int i = 0; i < insert.getArray().size(); i++) {
			final int pos = i;
			
			Label l = new Label(insert.getArray().get(i).getName());
			if (l.getWidth() > labelWidth)
				labelWidth = (int) l.getWidth();

			l.setFont(new Font("Devanagari MT", 20));
			l.setPadding(new Insets(
					3,
					window.getWidth() - labelWidth - 432,
					0,
					0));

			JFXButton edit   = new JFXButton("Edit");
			JFXButton remove = new JFXButton("Remove");

			edit  .setFont(new Font("Devanagari MT", 20));
			remove.setFont(new Font("Devanagari MT", 20));
			
			edit.setOnAction(e -> {
				ViewEditInsertGrid veig = new ViewEditInsertGrid(
						insert.getArray().get(pos));
				
				veig.display();
				if (veig.isSave())
					insert.getArray().set(pos, veig.getGrid());
				reDraw();
			});
			
			remove.setButtonType(ButtonType.RAISED);
			remove.setPrefWidth(100);
			remove.setTextFill(Color.WHITE);
			remove.setStyle(
					"-fx-background-color: rgb(196,67,67);");
			
			remove.setOnAction(e -> {
				Alert alert = new Alert(
						AlertType.CONFIRMATION,
						"Would you like to remove " + 
								insert.getArray().get(pos).getName() + "?",
						javafx.scene.control.ButtonType.YES,
						javafx.scene.control.ButtonType.NO);
				
				alert.showAndWait();
				
				if (alert.getResult() == javafx.scene.control.ButtonType.YES) {
					insert.removeInsert(insert.getArray().get(pos));
					reDraw();
				}
			});
			final int actualLabelWidth = labelWidth;
			window.widthProperty().addListener(e ->
				l.setPadding(new Insets(
						3,
						window.getWidth() - actualLabelWidth - 432,
						0,
						0)));
			
			gridPane.add(l, 0, i);
			gridPane.add(remove, 2, i);
			gridPane.add(edit, 1, i);
		}
	}
}
