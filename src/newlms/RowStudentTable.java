/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlms;

/**
 *
 * @author Dell
 */
public class RowStudentTable {
     private String id, title, teacher, term, numOfStudents;

    public RowStudentTable(String id, String title, String teacher, String term, String numOfStudents) {
        this.id = id;
        this.title = title;
        this.teacher = teacher;
        this.term = term;
        this.numOfStudents = numOfStudents;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getTerm() {
        return term;
    }

    public String getNumOfStudents() {
        return numOfStudents;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setNumOfStudents(String numOfStudents) {
        this.numOfStudents = numOfStudents;
    }
     
     
}
