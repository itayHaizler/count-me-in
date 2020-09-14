package server.domain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "slotCapacity")
public class SlotCapacity implements Serializable {

    @Id
    @Column(name = "slotID")
    private int slotID;

    @Column(name = "capacity")
    private int capacity;

    public SlotCapacity(){}

    public SlotCapacity(int slotID, int capacity){
        this.capacity = capacity;
        this.slotID = slotID;
    }

    public int getSlotID() { return this.slotID; }

    public int getCapacity() { return this.capacity; }

    public void setCapacity(int newCapacity) { this.capacity = newCapacity; }

}
