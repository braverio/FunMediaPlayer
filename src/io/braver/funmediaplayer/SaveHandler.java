package io.braver.funmediaplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SaveHandler {

	private File currFile; 
	
	public ArrayList<ArrayList<MediaPlayer>> load(Stage stage){
		FileChooser fc = new FileChooser();
		fc.setTitle("Select Data File");
		
		currFile  = fc.showOpenDialog(stage);
		try {
			FileInputStream fis = new FileInputStream(currFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList<ArrayList<MediaPlayer>> input = (ArrayList<ArrayList<MediaPlayer>>) ois.readObject();
			return input;
		} catch (FileNotFoundException e) {;
			return null;
		} catch (IOException e) {
			return null;
		} catch(ClassNotFoundException e){
			
		}
		return null;
	}
	
	public boolean save(ArrayList<ArrayList<MediaPlayer>> data, Stage stage){
		if (currFile == null){
			return saveAs(data, stage);
		}else{
			try {
				FileOutputStream fos = new FileOutputStream(currFile);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(data);
			} catch (FileNotFoundException e){
				return false;
			} catch (IOException e) {
				return false;
			}
			
			return true;
			
		}
	}
	
	public boolean saveAs(ArrayList<ArrayList<MediaPlayer>> data, Stage stage){
		FileChooser fc = new FileChooser();
		fc.setTitle("Select Data File");
		
		currFile  = fc.showOpenDialog(stage);
		
		return save(data, stage);
	}
	
}
