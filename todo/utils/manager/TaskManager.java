/**
 * @author: Lukmanul Hakim
 * description: Task Manager
 */

package todo.utils.manager;

import todo.utils.task.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TaskManager {
    List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        loadTaskFromFile();
    }

    public void loadTaskFromFile() {
        tasks.clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("list/tasks.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;

                String title = (String) jsonObj.get("title");
                String description = (String) jsonObj.get("description");
                String status = (String) jsonObj.get("status");
                LocalDate createdAt = LocalDate.parse((String) jsonObj.get("created_at"), formatter);
                LocalDate updatedAt = LocalDate.parse((String) jsonObj.get("updated_at"), formatter);

                Task task = new Task(title, description);
                task.setCreatedAt(createdAt);
                task.setUpdatedAt(updatedAt);
                task.markAsDone(status.equalsIgnoreCase("Complete"));

                tasks.add(task);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void addTask(Task task) throws IOException {
        if (!this.tasks.contains(task)) {
            this.tasks.add(task);
            System.out.println("Tugas" + "'" + task + "'" + "berhasi ditambahkan.");
            saveTask();
            displayTasks();
        } else {
            System.out.println("Tugas" + "'" + task + "'" + "sudah ada.");
        }
    }

    public boolean deleteTask(int index) throws IOException {
        try {
            clearScreen();
            this.tasks.remove(index);
            saveTask();
            System.out.printf("Task berhasil dihapus.\n\n");
            displayTasks();
            return true;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index tidak ditemukan.");
            return false;
        }
    }

    public void deleteTaskFile() {
        Path filePath = Paths.get("list/tasks.json");
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                tasks.removeAll(tasks);
                System.out.println("File task.list berhasil dihapus.");
            } else {
                System.out.println("File task.list tidak ditemukan.");
            }
        } catch (IOException e) {
            System.out.println("Gagal menghapus file task.list: " + e.getMessage());
        }
    }

    public void markAsDone(int index) throws IOException {
        clearScreen();
        try {
            Task task = tasks.get(index);
            if (!task.getMarkAsDone()) {
                task.markAsDone(true);
                System.out.println("Ditandai sebagai selesai: " + task);
                task.setUpdatedAt(LocalDate.now());
                saveTask();
                displayTasks();
            } else {
                System.out.println("Task ini sudah ditandai selesai.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index tidak ditemukan.");
        }
    }

    public void editTaskTitle(int index, String newTitle) {
        try {
            Task task = tasks.get(index);
            clearScreen();
            task.setTitle(newTitle);
            task.setUpdatedAt(LocalDate.now());
            index += 1;
            System.out.println("Judul tugas '" + index + "' berhasil dirubah menjadi '" + newTitle + "'.\n");
            displayTasks();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index tidak ditemukan.");
        }
    }

    public void editTaskDescription(int index, String newDescription) {
        clearScreen();
        try {
            Task task = tasks.get(index);
            task.setDescription(newDescription);
            task.setUpdatedAt(LocalDate.now());
            System.out.println("Deskripsi tugas berhasil dirubah  menjadi '" + newDescription + "'.\n");
            displayTasks();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index tidak ditemukan.");
        }
    }

    public void sortByIsDoneAndCreatedAt() {
        clearScreen();
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                int isDoneComparison = Boolean.compare(task1.getMarkAsDone(), task2.getMarkAsDone());
                if (isDoneComparison != 0) {
                    return isDoneComparison;
                } else {
                    return task1.getCreatedAt().compareTo(task2.getCreatedAt());
                }
            }
        });
        displayTasks();
    }

    public void sortByCreatedAt() {
        clearScreen();
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return task2.getCreatedAt().compareTo(task1.getCreatedAt());
            }
        });
        displayTasks();
    }

    public void displayTasks() {
        int maxIndexWidth = String.valueOf(tasks.size()).length() + 3;
        int maxTitleWidth = 0;
        int maxDescriptionWidth = 0;
        int maxStatusWidth = "Not Complete".length();

        if (!tasks.isEmpty()) {
            System.out.println("==============\n| Your Tasks |\n==============\n");
            for (Task task : tasks) {
                maxTitleWidth = Math.max(maxTitleWidth, task.getTitle().length());
                maxDescriptionWidth = Math.max(maxDescriptionWidth, task.getDescription().length());
                String status = task.getMarkAsDone() ? "\u001B[32mComplete\u001B[0m"
                        : "\u001B[31mNot Complete\u001B[0m";
                ;
                maxStatusWidth = Math.max(maxStatusWidth, status.length());
            }

            String format = "%-" + (maxIndexWidth + 2) + "s | %-" + (maxTitleWidth + 2) + "s | %-"
                    + (maxDescriptionWidth + 2) + "s | %-" + (maxStatusWidth + 2) + "s | %12s | %12s%n";

            System.out.printf(format, "Index", "Title", "Description", "\u001B[37mStatus\u001B[0m", "Created at",
                    "Updated at");
            System.out.println("=".repeat(maxIndexWidth + maxTitleWidth + maxDescriptionWidth + maxStatusWidth + 38));

            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                String status = task.getMarkAsDone() ? "\u001B[32mComplete\u001B[0m"
                        : "\u001B[31mNot Complete\u001B[0m";
                ;
                System.out.printf(format, i + 1, task.getTitle(), task.getDescription(), status, task.getCreatedAt(),
                        task.getupdatedAt());
            }
        } else {
            System.out.println("\n\n===================\n| Tidak ada tugas |\n===================");
        }
    }

    public void clearScreen() {
        System.out.print("\033c");
        System.out.flush();
    }

    @SuppressWarnings("unchecked")
    public void saveTask() throws IOException {
        JSONArray jsonArray = new JSONArray();

        for (Task task : tasks) {
            JSONObject jsonTask = new JSONObject();
            jsonTask.put("title", task.getTitle());
            jsonTask.put("description", task.getDescription());
            jsonTask.put("status", task.getMarkAsDone() ? "Complete" : "Not Complete");
            jsonTask.put("updated_at", task.getupdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            jsonTask.put("created_at", task.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            jsonArray.add(jsonTask);
        }

        Files.createDirectories(Paths.get("list"));
        try (FileWriter file = new FileWriter("list/tasks.json")) {
            file.write(jsonArray.toJSONString());
        } catch (IOException e) {
            System.out.println("Error while saving tasks: " + e.getMessage());
        }
    }

    public boolean checkIndex(int index) {
        if (index >= 0 && index < tasks.size()) {
            return true;
        } else {
            return false;
        }
    }

    public void searchTasks(String keyword) {
        clearScreen();
        boolean found = false;
        System.out
                .println(
                        "===========================================================================================\nHasil pencarian untuk \""
                                + keyword + "\" :\n");

        for (Task task : tasks) {
            if (task.getTitle().toLowerCase().contains(keyword)
                    || task.getDescription().toLowerCase().contains(keyword)) {
                System.out.format("%-20s: %s%n", "Title", task.getTitle());
                System.out.format("%-20s: %s%n", "Description", task.getDescription());
                System.out.format("%-20s: %s%n", "Status", (task.getMarkAsDone() ? "Complete" : "Not Complete"));
                System.out.println();
                found = true;
            }
        }
        if (!found) {
            System.out.println("\u001B[31m" + "Tidak ada hasil yang ditemukan." + "\u001B[0m");
        }
        System.out
                .println("===========================================================================================");
    }
}
