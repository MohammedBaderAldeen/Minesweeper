package main;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameSaverExecuter {
    private final Runnable save = new Runnable() {

        @Override
        public void run() {
            saveGame();
        }
    };
    private  final ExecutorService executor = Executors.newFixedThreadPool(1);
    private final Object state;
    private  String filePath;

    public GameSaverExecuter(Object replay,String filePath) {
        this.state = replay;
        this.filePath = filePath;
    }

    public void save() {
        executor.submit(save);
    }

    public  void close() {
        executor.shutdown();
    }

    private void saveGame() {
        //save your game here
    	WriteObjectToFile(this.state,this.filePath);
    	
    }
    public void WriteObjectToFile(Object serObj,String filepath) {
    	try {
    		
			FileOutputStream fileOut = new FileOutputStream(filepath);
		
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		
			objectOut.writeObject(serObj);
		
			objectOut.close();
		
			System.out.println("The Object  was succesfully written to a file");
			} catch (Exception ex) {
				ex.printStackTrace();
				}
		}

}
