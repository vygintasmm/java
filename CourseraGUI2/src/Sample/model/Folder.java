package Sample.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Folder implements Serializable {

  private int id;
  private String name;
  private Date dateModified;
  private ArrayList<File> folderFiles;

  public Folder(String name, Date dateModified, ArrayList<File> folderFiles) {
    this.name = name;
    this.dateModified = dateModified;
    this.folderFiles = folderFiles;
  }

  public Folder(String name, Date dateModified) {
    this.name = name;
    this.dateModified = dateModified;
  }

  public Folder(String name) {
    this.name = name;
    this.dateModified = new Date(System.currentTimeMillis());
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDateModified() {
    return dateModified;
  }

  public void setDateModified(Date dateModified) {
    this.dateModified = dateModified;
  }

  public ArrayList<File> getFolderFiles() {
    return folderFiles;
  }

  public void setFolderFiles(ArrayList<File> folderFiles) {
    this.folderFiles = folderFiles;
  }
}
