package io.braver.funmediaplayer;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogBox {
	boolean value = false;
	
	public ArrayList<String> data(String title, ObservableList<Node> items){
		ArrayList<String> values = new ArrayList<String>();
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		VBox layout = new VBox(10);
		layout.setAlignment(Pos.CENTER);
		
		Button submit = new Button("Submit");
		Button cancel = new Button("Cancel");
		
		layout.getChildren().addAll(items);
		layout.getChildren().addAll(submit, cancel);
		
		submit.prefWidthProperty().bind(window.widthProperty().multiply(0.5));
		cancel.prefWidthProperty().bind(window.widthProperty().multiply(0.5));
		
		cancel.setOnAction(e -> window.close());
		submit.setOnAction(e -> {
			for(Node i : layout.getChildren()){
				if(i instanceof TextField){
					values.add(((TextField) i).getText());
				}
			}
			window.close();
		});
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return values;
	}
	
	public boolean confirm(String title, String question){
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		VBox layout = new VBox(10);
		layout.setAlignment(Pos.CENTER);
		
		Label label = new Label(question);
		Button yes = new Button("Yes");
		Button no = new Button("No");
		
		layout.getChildren().addAll(label, yes, no);
		
		no.setOnAction(e -> window.close());
		yes.setOnAction(e -> {
			this.value = true;
			window.close();
		});
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return value;
	}
}
