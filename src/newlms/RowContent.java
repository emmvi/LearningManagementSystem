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
public class RowContent {
    private String subject, author, date;

    public RowContent(String subject, String author, String date) {
        this.subject = subject;
        this.author = author;
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public String getAuthor() {
        return author;
    }

   
    public String getDate() {
        return date;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    

    public void setDate(String date) {
        this.date = date;
    }
        
        
}
