
/**
 * @author: Lukmanul Hakim
 * description: App
 */

package todo.utils.app;

import todo.utils.manager.*;
import todo.utils.task.*;
import java.io.IOException;
import java.util.Scanner;

public class App {
    TaskManager tasks = new TaskManager();
    Scanner scn = new Scanner(System.in);
    int opsi, deleteTask, markDone;
    String title, description;

    public void run() throws IOException {
        System.out.println("========================================");
        System.out.println("| Selamat datang di aplikasi todolist! |");
        System.out.println("| author: Lukmanul Hakim               |");
        System.out.println("========================================");
        do {
            System.out.println("\n\n=================\n       MENU      \n=================");
            System.out.println(
                    "1. Add Task\n2. Show Tasks\n3. Edit Task\n4. Delete Task\n5. Mark  as Done\n6. Sorting\n7. Search Task\n8. Delete All Tasks\n0. Exit");
            System.out.print("\nPilih (1/2/3/4/5/6/7/8/0) : ");
            opsi = scn.nextInt();
            scn.nextLine();

            switch (opsi) {
                case 1:
                    System.out.print("\nMasukkan Judul Tugas : ");
                    title = scn.nextLine();
                    System.out.print("Masukkan Deskripti Tugas : ");
                    description = scn.nextLine();
                    tasks.clearScreen();
                    tasks.addTask(new Task(title, description));
                    break;

                case 2:
                    tasks.clearScreen();
                    tasks.displayTasks();
                    break;

                case 3:
                    String newTitle, newDescription;
                    int index, option;
                    tasks.clearScreen();
                    tasks.displayTasks();
                    System.out.print("\nMasukkan index task yang akan diedit : ");
                    index = scn.nextInt();
                    index -= 1;
                    scn.nextLine();
                    if (tasks.checkIndex(index) == true) {
                        do {
                            System.out.println("\n===== Menu Edit =====\n1. Edit Title\n2. Edit Description\n0. Exit");
                            System.out.print("Pilih (1/2) : ");
                            option = scn.nextInt();
                            scn.nextLine();
                            switch (option) {
                                case 1:
                                    System.out.print("Masukkan judul baru : ");
                                    newTitle = scn.nextLine();
                                    tasks.editTaskTitle(index, newTitle);
                                    break;
                                case 2:
                                    System.out.print("Masukkan deskripsi baru : ");
                                    newDescription = scn.nextLine();
                                    tasks.clearScreen();
                                    tasks.displayTasks();
                                    tasks.editTaskDescription(index, newDescription);
                                    break;
                                default:
                                    System.out.println("Pilihan tidak tersedia.");
                                    break;
                            }
                            break;
                        } while (option != 0);
                        tasks.saveTask();
                        break;
                    } else {
                        tasks.clearScreen();
                        System.out.println("Index " + index + " tidak ditemukan dalam data.");
                        break;
                    }

                case 4:
                    tasks.clearScreen();
                    tasks.displayTasks();
                    System.out.print("\nMasukkan index tugas yang akan dihapus : ");
                    deleteTask = scn.nextInt();
                    tasks.deleteTask(deleteTask -= 1);
                    break;

                case 5:
                    tasks.clearScreen();
                    tasks.displayTasks();
                    System.out.print("\nMasukkan index task yang akan ditandai sebagai selesai : ");
                    markDone = scn.nextInt();
                    tasks.markAsDone(markDone -= 1);
                    break;

                case 6:
                    do {
                        System.out
                                .println("\n===== Menu Edit =====\n1. Sorting by status\n2. Sorting by date\n0. Exit");
                        System.out.print("Pilih (1/2) : ");
                        option = scn.nextInt();
                        scn.nextLine();
                        switch (option) {
                            case 1:
                                tasks.sortByIsDoneAndCreatedAt();
                                break;
                            case 2:
                                tasks.sortByCreatedAt();
                                break;
                            default:
                                break;
                        }
                        break;
                    } while (option != 0);
                    break;

                case 7:
                    String keyword;
                    System.out.print("Masukkan kata kunci yang dicari : ");
                    keyword = scn.nextLine();
                    tasks.searchTasks(keyword);
                    break;

                case 8:
                tasks.clearScreen();
                    tasks.deleteTaskFile();;
                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    tasks.clearScreen();
                    System.out.println("\u001B[31mOpsi yang anda pilih tidak tersedia!\u001B[0m");
                    break;
            }
        } while (opsi != 0);
        scn.close();
    }

    public void clear() {
        tasks.clearScreen();
    }
}
