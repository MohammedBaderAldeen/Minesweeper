package models;

import java.io.Serializable;

import settings.Action;
import settings.Status;

public class Score  implements Serializable{
    
	private static final long serialVersionUID = -261413024653120566L;
	private int value;

    Score() {
        this(0);
    }

    public Score(int value) {
        this.value = value;
    }

    public void update(Status status, Action action, boolean hasShield) {
        // Game rules
        if (action == Action.OPEN) {
            if (Status.isNumber(status)) {
                this.value += status.getValue();
            }
            if (status == Status.BLANK) {
                this.value += 10;
            }
            if(status == Status.SHIELD)
            {
            	
            }
            if (status == Status.BOMBED) {
            	if(!hasShield)
            		this.value += -250;
            }
        }
        if (action == Action.FLOOD) {
            this.value += 1;
        }
        if (action == Action.FLAG) {
            if (status == Status.GRAY_BOMBED) {
                this.value += 5;
            } else {
                this.value += -1;
            }
        }
        if (action == Action.UN_FLAG) {
            if (status == Status.GRAY_BOMBED) {
                this.value -= 5;
            }
        }
        if (action == Action.SUPER_SHIELD) {
        	this.value +=1000;
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isPositive() {
        return this.value >= 0;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
