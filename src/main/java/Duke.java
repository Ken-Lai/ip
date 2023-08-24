import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    private static final String SEPARATION_LINE  = "____________________________________________________________";
    private static final String INDENTATION = "    ";
    private static final String GREETING_TEXT = "Hello, I'm Tasket\n" + INDENTATION + "What can I do for you?";
    private static final String GOODBYE_TEXT = "Bye. Hope to see you again soon!";

    private static String prompt = "";
    private static final ArrayList<Task> list = new ArrayList<>();

    public static void showSeparationLine() {
        System.out.println(INDENTATION + SEPARATION_LINE);
    }

    public static void showGreetingText() {
        showSeparationLine();
        System.out.println(INDENTATION + GREETING_TEXT);
        showSeparationLine();
    }

    public static void showGoodbyeText() {
        System.out.println(INDENTATION + GOODBYE_TEXT);
        showSeparationLine();
    }

    public static void addTask(String text) {
        list.add(new Task(text));
        System.out.println(INDENTATION + "added: " + text);
        showSeparationLine();
    }

    public static void showLists() {
        System.out.println(INDENTATION + "Here are the tasks in your list:");
        for (int i = 1; i <= list.size(); i++) {
            System.out.printf("%s%d. %s\n", INDENTATION, i, list.get(i - 1).toString());
        }
        showSeparationLine();
    }

    public static void markTaskDone(String text) {
        Task selectedTask = list.get(Integer.parseInt(text) - 1);

        selectedTask.markAsDone();

        System.out.println(INDENTATION + "Nice! I've marked this task as done:\n"
                + INDENTATION + "  " + selectedTask.toString());
        showSeparationLine();
    }

    public static void markTaskUndone(String text) {
        Task selectedTask = list.get(Integer.parseInt(text) - 1);

        selectedTask.markAsUndone();

        System.out.println(INDENTATION + "Ok. I've marked this task as not done yet:\n"
                + INDENTATION + "  " + selectedTask.toString());
        showSeparationLine();
    }

    public static void echoInputText(String text) {
        System.out.println(text);
        showSeparationLine();
    }

    public static void parseInput(String text) {
        String[] actions = text.split(" ", 2);

        switch (actions[0]) {
        case "mark":
            markTaskDone(actions[1]);
            break;

        case "unmark":
            markTaskUndone(actions[1]);
            break;

        case "list":
            showLists();
            break;

        case "bye":
            showGoodbyeText();
            break;

        default:
            addTask(text);
            break;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        showGreetingText();

        while (!prompt.equals("bye")) {
            System.out.println();
            prompt = sc.nextLine();
            showSeparationLine();

            parseInput(prompt);
        }

        sc.close();
    }
}
