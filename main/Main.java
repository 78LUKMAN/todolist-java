package main;

/**
 * ====== masih belajar😵 ======
 * @author: Lukmanul Hakim
 * description: Main
 */

 import todo.utils.app.App;

public class Main {
    public static void main(String[] args) {
        try {
            App app = new App();
            app.clear();
            app.run();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}