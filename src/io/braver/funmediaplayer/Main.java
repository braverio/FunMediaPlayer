package io.braver.funmediaplayer;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
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
	ListView<String> mediaList, sectionList;
	
	CheckBox mup, ap, pos;
	
	int currSection = -1;
	int currMedia = -1;
	
	final int PADDING = 20;
	final int SPACING = 10;
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Untitled-Show");
		
		initMEP();
		
		runtimePanel = new HBox();
		Button r0 = new Button("Next (->)");
		Button r1 = new Button("Replay (R)");
		Button r2 = new Button("Previous (<-)");
		Button r3 = new Button("BLANK (B)");
		Button r4 = new Button("Play (Enter)");
		Button r5 = new Button("Stop (Space)");
		Button r6 = new Button("Pause (Enter)");
		
		
		runtimePanel.getChildren().addAll(r0, r1, r2, r3, r4, r5, r6);
		for (Node i : runtimePanel.getChildren()){
			((Region) i).prefWidthProperty().bind(runtimePanel.widthProperty());
		}
		
		mediaList = new ListView<String>();
		fillMediaList();
		
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
		control.setTitle("Untitled Show");
		settingsPanel.prefWidthProperty().bind(control.widthProperty().multiply(0.2));
		mediaInfoPanel.prefWidthProperty().bind(control.widthProperty().multiply(0.2));
		
		control.addEventHandler(KeyEvent.KEY_PRESSED, e ->{
			switch(e.getCode()){
			case ENTER:
				if(isPlaying()) r6.fire();
				else r4.fire();
				break;
			case R:
				r1.fire();
				break;
			case B:
				r3.fire();
				break;
			case SPACE:
				r5.fire();
				break;
			case RIGHT:
				r0.fire();
				break;
			case LEFT:
				r2.fire();
				break;
			default:
				break;
				
			}
		});
		
		control.setMaximized(true);
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
		pos = new CheckBox("Play On Select");
		
		
		Label l3 = new Label("System Volume");
		
		settingsPanel.getChildren().addAll(l0, setCntl, l1, setMedia, b1, b2, l2, mup, ap, pos, l3);
		settingsPanel.setPadding(new Insets(PADDING));
		settingsPanel.setSpacing(SPACING);
		
		b1.prefWidthProperty().bind(settingsPanel.widthProperty());
		b2.prefWidthProperty().bind(settingsPanel.widthProperty());
	}
	
	
	private boolean isMUP(){
		return mup.isSelected();
	}
	
	private boolean isAP(){
		return ap.isSelected();
	}
	
	private boolean isPlaying(){
		MediaPlayer mp = sections.get(currSection).get(currMedia);
		return mp.getStatus() == Status.PLAYING;
	}
	
	private void initMEP(){
		mediaEditorPanel = new GridPane();
		
		ColumnConstraints col0 = new ColumnConstraints();
		col0.setPercentWidth(25);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		mediaEditorPanel.getColumnConstraints().addAll(col0, col1, col2, col3);
		
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
		
		mediaEditorPanel.setPadding(new Insets(PADDING));
		mediaEditorPanel.setHgap(SPACING);
		
		b00.prefWidthProperty().bind(mediaEditorPanel.widthProperty());
		b01.prefWidthProperty().bind(mediaEditorPanel.widthProperty());
		b02.prefWidthProperty().bind(mediaEditorPanel.widthProperty());
		
		b11.prefWidthProperty().bind(mediaEditorPanel.widthProperty());
		b12.prefWidthProperty().bind(mediaEditorPanel.widthProperty());
		
		b21.prefWidthProperty().bind(mediaEditorPanel.widthProperty());
		b22.prefWidthProperty().bind(mediaEditorPanel.widthProperty());
		
		b31.prefWidthProperty().bind(mediaEditorPanel.widthProperty());
		b32.prefWidthProperty().bind(mediaEditorPanel.widthProperty());
		
		b31.setOnAction(e ->{
			TextField name = new TextField();
			name.setPromptText("Section Name");
			ObservableList<Node> fields = FXCollections.observableArrayList(name);
			ArrayList<String> data = new DialogBox().data("Create New Section", fields);
			sectionNames.add(data.get(0));
			ArrayList<MediaPlayer> currSectionArr = new ArrayList<MediaPlayer>();
			sections.add(currSectionArr);
			currSection = sections.size()-1;
			fillSectionList();
		});
		
		b32.setOnAction(e -> {
			if(currSection != -1){
				String name = sectionNames.get(currSection);
				if(new DialogBox().confirm("Delete?", "Are you sure you want to delete " + name + "?")){
					sections.remove(currSection);
					sectionNames.remove(currSection);
					currSection--;
					fillSectionList();
					fillMediaList();
				}
			}
		});
	}
	
	private void initMediaInfo(){
		mediaInfoPanel = new VBox();
		
		Label now = new Label();
		updateNow(now);
		
		Label next = new Label();
		updateNext(next);
		
		sectionList = new ListView<String>();
		fillSectionList();
		
		mediaInfoPanel.getChildren().addAll(now, next, sectionList);
		mediaInfoPanel.setPadding(new Insets(PADDING));
		mediaInfoPanel.setSpacing(SPACING);
		
		sectionList.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
			int indexSel = sectionNames.indexOf(newVal);
			currSection = indexSel;
			currMedia = -1;
			fillMediaList();
		});
	}	
		
	
	private void updateNow(Label l){
		if(currSection != -1){
			String now = paths.get(currSection).get(currMedia);
			l.setText("Now playing: " + now);
		}else{
			l.setText("Now Playing: No media is selected.");
		}
	}
	
	private void updateNext(Label l){
		if(currSection != -1 || currMedia != -1){
			ArrayList<String> s = paths.get(currSection);
			if (currMedia + 1 < s.size()){
				String next = s.get(currMedia + 1);
				l.setText("Next: " + next);
			}
		}else{
			l.setText("Next: None");
		}
	}

	private void fillMediaList() {
		if(currSection != -1 && paths.size() > 0){
			ArrayList<String> mps = paths.get(currSection);
			ObservableList<String> items = FXCollections.observableArrayList();
			for(int i = 0; i < mps.size(); i++){
				items.addAll(mps.get(i));
			}
			mediaList.setItems(items);
		}else{
			mediaList.setItems(FXCollections.emptyObservableList());
		}
		
	}
	
	private void fillSectionList() {
		if(currSection != -1){
			ObservableList<String> items = FXCollections.observableArrayList();
			for(int i = 0; i < sectionNames.size(); i++){
				items.add(sectionNames.get(i));
			}
			sectionList.setItems(items);
			sectionList.getSelectionModel().select(currSection);
		}else{
			sectionList.setItems(FXCollections.emptyObservableList());
		}
	}

}
