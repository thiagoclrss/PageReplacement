package logic;

public class Page {

    String pageId;
    String type;
    boolean bitR = false;
    boolean bitM = false;

    public Page(String pageId, String type) {
        this.pageId = pageId;
        this.type = type;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBitR() {
        return bitR;
    }

    public void setBitR(boolean bitR) {
        this.bitR = bitR;
    }

    public boolean isBitM() {
        return bitM;
    }

    public void setBitM(boolean bitM) {
        this.bitM = bitM;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageId='" + pageId + '\'' +
                ", type='" + type + '\'' +
                ", bitR='" + bitR + '\''+
                ", bitM='" + bitM + '\''+
                '}';
    }
}
