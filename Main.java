import java.util.Scanner;
import java.util.Random;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int secretNumber = random.nextInt(10000)+1;
        int attempts = 0;
        int guess;
        System.out.println("Привет! Я загадал число от 1 до 10000! Угадаешь?");

        while (true) {
            System.out.println("Сюда напиши свою догадку: ");
            guess = scanner.nextInt();
            attempts++;
            if (guess < secretNumber){
                System.out.println("Больше!");
            }else if (guess > secretNumber){
                System.out.println("Меньше!");
            }else{
                System.out.println("Подзравляю, ты угадал число с "+attempts+"-й попытки");
                break;
            }
        }
        scanner.close();
    }
}