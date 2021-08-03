package models;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import main.Game;
import settings.Settings;
import settings.Status;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cell extends StackPane implements Serializable  {
    
    private static final long serialVersionUID = 8188438345270977302L;
	
    private int x;
    
    private int y;
    
    private Status value = Status.COVERED;
    
    public boolean hasBomb;
    
    public boolean hasShield;
   
    private transient  Game game;
    
    private boolean isOpened = false;
    
    private boolean isFlagged = false;
   
    private List<Cell> neighbors;
    
	public List<Cell> getNeighbors() {
		return neighbors;
	}

	
    public transient  Rectangle rectangle = new Rectangle(Settings.CELL_SIZE, Settings.CELL_SIZE);

    public Cell(int x, int y, boolean hasBomb, boolean hasShield, Game game) {
        this.x = x;
        this.y = y;
        this.hasBomb = hasBomb;
        this.hasShield = hasShield;
        if (this.hasBomb) this.value = Status.GRAY_BOMBED;
        this.game = game;
        this.init();
    }
    

    public Cell() {
		super();
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}

    public void init(Cell[][] grid,boolean loaded) {
        this.neighbors = new ArrayList<>();
        // Neighbors dx & dy points
        int[] points = new int[] {
                1, 1,
                1, 0,
                1, -1,
                0, -1,
                -1, -1,
                -1, 0,
                -1, 1,
                0, 1,
        };

        int dx, dy, neighborX, neighborY;
        for (int i = 0; i < points.length; i++) {
            dx = points[i];
            dy = points[++i];
            neighborX = this.x + dx;
            neighborY = this.y + dy;
            if (Settings.isValidCoordinates(neighborX, neighborY)) {
                this.neighbors.add(grid[neighborY][neighborX]);
            }
        }
        if (!this.hasBomb && !loaded) {
            this.setValue(this.neighbors.stream().filter(c -> c.hasBomb).count());
        }
        if(loaded & this.isOpened) {
        	this.rectangle.setFill(Settings.ImageService.getImage(this.value));
        }
        if(loaded & this.isFlagged) {
        	this.rectangle.setFill(Settings.ImageService.getImage(Status.FLAGGED));
        }
        if(loaded & this.hasShield &this.isOpened){
            this.rectangle.setFill(Settings.ImageService.getImage(Status.SHIELD));
        }
    }

    private void init() {
        this.initRectangle();
        this.initPosition();
        this.listenOnMouseEvents();
    }

    private void initRectangle() {
        // Displayed image
        this.rectangle.setFill(Settings.ImageService.getImage(Status.COVERED));
        this.getChildren().add(this.rectangle);
    }

    private void initPosition() {
        this.setTranslateX(this.x * Settings.CELL_SIZE);
        this.setTranslateY((this.y + 3) * Settings.CELL_SIZE);
    }

    public boolean listenToMouse =true; 
    private void listenOnMouseEvents() {
    	if(this.game.isNotReplay) {
    		// On mouse middle button pressed but not released
            this.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.MIDDLE) {
                    if (!this.isOpened && !this.isFlagged) this.rectangle.setFill(Settings.ImageService.getImage(Status.BLANK));
                    this.neighbors.stream().filter(n -> !n.isOpened && !n.isFlagged)
                            .forEach(n -> n.rectangle.setFill(Settings.ImageService.getImage(Status.BLANK)));
                }
            });
            // On mouse middle button released
            this.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.MIDDLE) {
                    if (!this.isOpened && !this.isFlagged) this.rectangle.setFill(Settings.ImageService.getImage(Status.COVERED));
                    this.neighbors.stream().filter(n -> !n.isOpened && !n.isFlagged)
                            .forEach(n -> n.rectangle.setFill(Settings.ImageService.getImage(Status.COVERED)));
                }
            });
            // On left or right mouse clicked
            this.setOnMouseClicked(event -> {
                if (this.game.over) return;
                if (event.getButton() == MouseButton.PRIMARY) {
                    this.onLeftClick();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    this.onRightClick();
                }
            });
    	}
    }

   
    public void onLeftClick() {
    	onCellClicked(true);
        
    }

    private void onCellClicked(boolean b) {
    	if (this.hasBomb) {
    		this.value = Status.BOMBED;
    	}
        if (this.open()) this.game.onCellOpened(this,b);		
	}


    public boolean open() {
        if (this.isFlagged) return false;
        this.isOpened = true;
        if (this.value != Status.BLANK) {
            this.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.MIDDLE) {
                    // Get flagged neighbors count
                    if (this.value.getValue() == this.neighbors.stream().filter(neighbor -> neighbor.isFlagged).count()) {
                        this.neighbors.stream().filter(neighbor -> !neighbor.isOpened).forEach(Cell->Cell.onCellClicked(false));
                    }
                   if(this.game!= null) this.game.changeTurn();
                }
            });
        }

        this.rectangle.setFill(Settings.ImageService.getImage(this.value));
        if(this.hasShield) 
		{
                this.rectangle.setFill(Settings.ImageService.getImage( Status.SHIELD));
        	this.game.currentPlayer.setShieldCount(this.game.currentPlayer.getShieldCount()+1);
        	System.out.println(this.game.currentPlayer.getName() + "'s Shield = " + this.game.currentPlayer.getShieldCount());
		}
        // Flood fill
        if (this.value == Status.BLANK) {
            this.neighbors.stream().filter(neighbor -> !neighbor.isOpened).forEach(Cell::open);
        }
        return true;
    }

    public void onRightClick() {
        this.rectangle.setFill(Settings.ImageService.getImage(this.isFlagged ?
                                                                Status.COVERED : Status.FLAGGED));
        this.isFlagged = !this.isFlagged;
        this.game.onCellFlag(this);
    }

	public boolean isHasBomb() {
		return hasBomb;
	}


	
	public void setHasBomb(boolean hasBomb) {
		this.hasBomb = hasBomb;
	}


	public boolean isHasShield() {
		return hasShield;
	}


	public void setHasShield(boolean hasShield) {
		this.hasShield = hasShield;
	}


	public boolean getIsFlagged() {
        return this.isFlagged;
    }

    public boolean getIsOpened() { return this.isOpened; }

    private void setValue(long value) {
        this.value = Status.getValue((int) value);
    }

    public void setValue(Status status) { this.value = status; }

    public Status getValue() {
        return this.value;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
    
}
