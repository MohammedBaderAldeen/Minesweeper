package models;

import java.io.Serializable;

import settings.Status;

public class CellStatus implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3069414076559014371L;
	int x,y;
	Status value;
	boolean hasBomb,hasShield,isOpened,isFlagged;
	public CellStatus(int x, int y, Status value, boolean hasBomb, boolean hasShield, boolean isOpened,
			boolean isFlagged) {
		super();
		this.x = x;
		this.y = y;
		this.value = value;
		this.hasBomb = hasBomb;
		this.hasShield = hasShield;
		this.isOpened = isOpened;
		this.isFlagged = isFlagged;
	}
	public CellStatus() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @return the value
	 */
	public Status getValue() {
		return value;
	}
	/**
	 * @return the hasBomb
	 */
	public boolean isHasBomb() {
		return hasBomb;
	}
	/**
	 * @return the hasShield
	 */
	public boolean isHasShield() {
		return hasShield;
	}
	/**
	 * @return the isOpened
	 */
	public boolean isOpened() {
		return isOpened;
	}
	/**
	 * @return the isFlagged
	 */
	public boolean isFlagged() {
		return isFlagged;
	}
	public void setShield(boolean b) {
		this.hasShield=false;
		
	}
}
