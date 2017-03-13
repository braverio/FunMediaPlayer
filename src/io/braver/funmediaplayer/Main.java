package io.braver.funmediaplayer;

import java.awt.Window;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

	ArrayList<ArrayList<MediaPlayer>> sections = new ArrayList<ArrayList<MediaPlayer>>();
	ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
	ArrayList<String> sectionNames = new ArrayList<String>();
	
	Stage control, video;
	Scene scene;
	
	BorderPane root;
	GridPane mediaEditorPanel;
	VBox settingsPanel, mediaInfoPanel;
	HBox runtimePanel;
	ListView<String> mediaList;
	
	CheckBox mup, ap;
	
	int currSection = 0;
	int currMedia = 0;
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Untitled-Show");
	
		ArrayList<String> s = new ArrayList<String>();
		s.add("C:\\afnbosdfh");
		s.add("C:\\sdjfpsfj");
		s.add("C:\\jwerh40");
		
		sectionNames.add("dipfn0");
		sectionNames.add("dipfn1");
		sectionNames.add("dipfn45");
		
		paths.add(s);
		
		initMEP();
		
		runtimePanel = new HBox();
		Button r0 = new Button("Next");
		Button r1 = new Button("Replay");
		Button r2 = new Button("Previous");
		Button r3 = new Button("Play");
		Button r4 = new Button("Stop");
		Button r5 = new Button("Pause");
		runtimePanel.getChildren().addAll(r0, r1, r2, r3, r4, r5);
		
		mediaList = new ListView<String>();
		fillMediaList(mediaList);
		
		initMediaInfo();
		initSettingsPanel();
		
		root = new BorderPane();
		root.setTop(runtimePanel);
		root.setBottom(mediaEditorPanel);
		root.setLeft(settingsPanel);
		root.setRight(mediaInfoPanel);
		root.setCenter(mediaList);
		
		scene = new Scene(root);
		control = new Stage();
		control.setScene(scene);
		settingsPanel.prefWidthProperty().bind(control.widthProperty().multiply(0.2));
		mediaInfoPanel.prefWidthProperty().bind(control.widthProperty().multiply(0.2));
		
		control.show();
		
	}
	
	private void initSettingsPanel(){
		settingsPanel = new VBox();
		
		Label l0 = new Label("Set Controller");
		ListView<Screen> setCntl = new ListView<Screen>();
		setCntl.prefHeightProperty().bind(settingsPanel.heightProperty().multiply(0.2));
		
		Label l1 = new Label("Set Media");
		ListView<Screen> setMedia = new ListView<Screen>();
		setMedia.prefHeightProperty().bind(settingsPanel.heightProperty().multiply(0.2));
		
		Button b1 = new Button("Apply Settings");
		setMedia.getSelectionModel().selectedItemProperty().addListener(e -> {
			boolean status = setMedia.getSelectionModel().getSelectedItem().equals(setCntl.getSelectionModel().getSelectedItem());
			b1.setDisable(status);
		});
		setCntl.getSelectionModel().selectedItemProperty().addListener(e -> {
			boolean status = setMedia.getSelectionModel().getSelectedItem().equals(setCntl.getSelectionModel().getSelectedItem());
			b1.setDisable(status);
		});
		
		
		b1.setOnAction(e -> {
			Screen cntl = setCntl.getSelectionModel().getSelectedItem();
			Screen med = setMedia.getSelectionModel().getSelectedItem();
		});
		
		Button b2 = new Button("Refresh Screens");
		setCntl.setItems(Screen.getScreens());
		setMedia.setItems(Screen.getScreens());
		b2.setOnAction(e -> {
			setCntl.setItems(Screen.getScreens());
			setMedia.setItems(Screen.getScreens());
		});
		
		Label l2 = new Label("Runtime Settings");
		mup = new CheckBox("Mute Until Play");
		ap = new CheckBox("Autoplay");
		
		
		Label l3 = new Label("System Volume");
		
		settingsPanel.getChildren().addAll(l0, setCntl, l1, setMedia, b1, b2, l2, mup, ap, l3);
	}
	
	
	private boolean isMUP(){
		return mup.isSelected();
	}
	
	private boolean isAP(){
		return ap.isSelected();
	}
	
	
	
	private void initMEP(){
		mediaEditorPanel = new GridPane();
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		mediaEditorPanel.getColumnConstraints().addAll(col1, col2, col3, col4);
		
		Button b00 = new Button("Load");
		mediaEditorPanel.add(b00, 0, 0);
		Label l1 = new Label("Edit Media");
		mediaEditorPanel.add(l1, 1, 0);
		Label l3 = new Label("Edit Sections");
		mediaEditorPanel.add(l3, 3, 0);
		
		Button b01 = new Button("Save");
		mediaEditorPanel.add(b01, 0, 1);
		Button b02 = new Button("Save As");
		mediaEditorPanel.add(b02, 0, 2);
		Button b11 = new Button("Add Media");
		mediaEditorPanel.add(b11, 1, 1);
		Button b12 = new Button("Remove Media");
		mediaEditorPanel.add(b12, 1, 2);
		Button b21 = new Button("Move Up");
		mediaEditorPanel.add(b21, 2, 1);
		Button b22 = new Button("Move Down");
		mediaEditorPanel.add(b22, 2, 2);
		Button b31 = new Button("New Section");
		mediaEditorPanel.add(b31, 3, 1);
		Button b32 = new Button("Delete Section");
		mediaEditorPanel.add(b32, 3, 2);
	}
	
	private void initMediaInfo(){
		mediaInfoPanel = new VBox();
		
		Label now = new Label();
		updateNow(now);
		
		Label next = new Label();
		updateNext(next);
		
		ListView<String> sectionList = new ListView<String>();
		fillSectionList(sectionList);
		
		mediaInfoPanel.getChildren().addAll(now, next, sectionList);
	}
	
	private void updateNow(Label l){
		String now = paths.get(currSection).get(currMedia);
		l.setText("Now playing: " + now);
	}
	
	private void updateNext(Label l){
		ArrayList<String> s = paths.get(currSection);
		if (currMedia + 1 < s.size()){
			String next = s.get(currMedia + 1);
			l.setText("Next: " + next);
		}
	}

	private void fillMediaList(ListView<String> ml) {
		ArrayList<String> mps = paths.get(currSection);
		ObservableList<String> items = FXCollections.observableArrayList();
		for(int i = 0; i < mps.size(); i++){
			items.addAll(mps.get(i));
		}
		ml.setItems(items);
		
	}
	
	private void fillSectionList(ListView<String> sl) {
		ObservableList<String> items = FXCollections.observableArrayList();
		for(int i = 0; i < sectionNames.size(); i++){
			items.addAll(sectionNames.get(i));
		}
		sl.setItems(items);
		
	}

}
