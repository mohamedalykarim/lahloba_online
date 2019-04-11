package online.lahloba.www.lahloba.data.model;

public class SubMenuItem {
    String id;
    String parentId;
    String image;
    String title;
    boolean hasChild;

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }
}
