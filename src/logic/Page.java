package logic;

public class Page {

    String pageId;
    String type;

    public Page(String pageId) {
        this.pageId = pageId;
    }

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

    @Override
    public String toString() {
        return "Page{" +
                "pageId='" + pageId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
