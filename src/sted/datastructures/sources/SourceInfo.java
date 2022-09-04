package sted.datastructures.sources;

public class SourceInfo {

    private String className;
    private String searchName;
    private String showID;

    public SourceInfo(String className, String searchName,
            String showID) {
        this.className = className;
        this.searchName = searchName;
        this.showID = showID;
    }

    public String getClassName() {
        return this.className;
    }

    public String getSearchName() {
        return searchName;
    }

    public String getShowID() {
        return showID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SourceInfo other = (SourceInfo) obj;
        if ((this.className == null) ? 
            (other.className != null) :
            !this.className.equals(other.className)) {
            return false;
        }
        if ((this.searchName == null) ? 
            (other.searchName != null) :
            !this.searchName.equals(other.searchName)) {
            return false;
        }
        if ((this.showID == null) ? 
            (other.showID != null) :
            !this.showID.equals(other.showID)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.className != null ?
            this.className.hashCode() : 0);
        hash = 59 * hash + (this.searchName != null ?
            this.searchName.hashCode() : 0);
        hash = 59 * hash + (this.showID != null ?
            this.showID.hashCode() : 0);
        return hash;
    }
}
