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

    @Id
    @Column(name = "slotID")
    private int slotID;

    @Id
    @Column(name = "date")
    private Date date;

    public SlotDates() {

    }

    public SlotDates(int slotID, Date date) {
        this.slotID = slotID;
        this.date = date;
    }

    public int getSlotID() {
        return slotID;
    }

    public Date getDate() {
        return date;
    }
}
