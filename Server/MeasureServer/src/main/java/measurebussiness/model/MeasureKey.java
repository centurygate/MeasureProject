package measurebussiness.model;

public class MeasureKey {
    private Long recordtime;

    private String devaddr;

    public Long getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(Long recordtime) {
        this.recordtime = recordtime;
    }

    public String getDevaddr() {
        return devaddr;
    }

    public void setDevaddr(String devaddr) {
        this.devaddr = devaddr == null ? null : devaddr.trim();
    }
    
    @Override
    public String toString()
    {
        return "MeasureKey{" +
                "recordtime=" + recordtime +
                ", devaddr='" + devaddr + '\'' +
                '}';
    }
}