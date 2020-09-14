package server.domain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "slotDates")
public class SlotDates implements Serializable {

    @Column(name = "slotID")
    private int slotID;

    @Column(name = "date")
    private Date date;

    @Id
    @Column(name = "slotDateID")
    private int slotDateID;

    public SlotDates() {

    }

    public SlotDates(int slotID, Date date, int slotDateID) {
        this.slotDateID = slotDateID;
        this.slotID = slotID;
        this.date = date;
    }

    public int getSlotDateID() {
        return this.slotDateID;
    }

    public int getSlotID() {
        return slotID;
    }

    public Date getDate() {
        return date;
    }
}
