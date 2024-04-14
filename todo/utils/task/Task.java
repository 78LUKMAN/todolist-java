/**
 * ====== masih belajar😵 ======
 * @author: Lukmanul Hakim
 * description: Task
 */

package todo.utils.task;

import java.time.LocalDate;

public class Task {
    private String title, description;
    private boolean isDone;
    private LocalDate updatedAt, createdAt;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.isDone = false;
        this.updatedAt = LocalDate.now();
        this.createdAt = LocalDate.now();   
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void markAsDone(boolean isDone) {
        if (isDone != this.isDone) { 
            this.isDone = isDone;
            if (isDone) {
                this.updatedAt = LocalDate.now(); 
            } else {
                this.createdAt = LocalDate.now(); 
            }
        } 
    }
    

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title; 
    }

    public boolean getMarkAsDone() {
        return isDone;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getupdatedAt() {
        return updatedAt;
    }
}